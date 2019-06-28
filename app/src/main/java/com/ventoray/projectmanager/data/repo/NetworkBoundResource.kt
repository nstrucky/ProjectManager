package com.ventoray.projectmanager.data.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    //Coroutine Scope var/vals
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        result.value = Resource.loading(null)
        Log.d("NBR", "init")
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        Log.d("NBR - dbSource", dbSource.toString())
        result.addSource(dbSource) { data ->
            Log.d("NBR - data", data.toString())
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                Log.d("NBR", "should fetch")
                fetchFromNetwork(dbSource)
            } else {
                Log.d("NBR", "should not fetch")
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        //this is ok because we are using LiveData
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
/*
//        This is all just genius
            when (response) {
                is ApiSuccessResponse -> {
                }

                is ApiErrorResponse -> {
                }

                is ApiEmptyResponse -> {
                }
            }*/

            //not a good idea, may not just be null or not null outcomes
            if (response != null) {
                scope.launch(Dispatchers.IO) {
                    saveCallResult(processResponse(response))
                }

                scope.launch(Dispatchers.Main) {
                    // we specially request a new live data,
                    // otherwise we will get immediately last cached value,
                    // which may not be updated with latest results received from network.
                    result.addSource(loadFromDb()) { newData ->
                        setValue(Resource.success(newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    /**
     * Does nothing for now
     */
    @WorkerThread
    protected open fun processResponse(response: RequestType) = response/*response.body*/

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<RequestType>
}
