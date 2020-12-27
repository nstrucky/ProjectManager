package com.ventoray.projectmanager.api

import android.arch.lifecycle.LiveData
import android.util.Log
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 *
 *  Basically this allows us to call webService.getAllProjects() and return a LiveData<List<Project>>
 *      because LiveData has a callback function onActive which is called with the first observer is added
 *
 * This was taken from the GithubBrowserSample project
 * @param <R>
 */
class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<R>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): LiveData<R> {
        return object : LiveData<R>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            /**
                             * The example uses an ApiResponse class to determine what to actually
                             *  post here...much better than just passing the body like I'm doing here
                             */
                            Log.d("CallAdapter", response.raw().toString())

                            postValue(response.body())
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            Log.e("CallAdapter", throwable.localizedMessage)
                            Log.e("", throwable.stackTrace.toString())
                            postValue(null)
                        }
                    })
                }
            }
        }
    }
}