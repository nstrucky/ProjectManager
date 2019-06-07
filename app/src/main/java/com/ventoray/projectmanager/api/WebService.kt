package com.ventoray.projectmanager.api

import android.arch.lifecycle.LiveData
import com.ventoray.projectmanager.api.APIv1.Constants.API_CURRENT
import com.ventoray.projectmanager.data.datamodel.Project
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface WebService {

    @Headers("Accept: application/json")
    @GET("$API_CURRENT/users/{userId}/projects")
    fun getUserProjects(@Header("Authorization") token: String,
                        @Path("userId") userId: Int): LiveData<List<Project>>


}
