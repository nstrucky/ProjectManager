package com.ventoray.projectmanager.api

import android.arch.lifecycle.LiveData
import com.ventoray.projectmanager.api.APIv1.Constants.API_CURRENT
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.datamodel.User
import retrofit2.http.*

interface WebService {

    @Headers("Accept: application/json")
    @GET("$API_CURRENT/users/{userId}/projects")
    fun getUserProjects(@Header("Authorization") token: String,
                        @Path("userId") userId: Int): LiveData<List<Project>>

    @Headers("Accept: application/json")
    @GET("$API_CURRENT/user")
    fun getUser(@Header("Authorization") token: String): LiveData<User>



    // /oauth/token
//    @Headers("Accept: application/json")
//    @POST("/oauth/token")
//    fun signInRequestToken() :

}
