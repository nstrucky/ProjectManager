package com.ventoray.projectmanager.data.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.WorkerThread
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ventoray.projectmanager.api.APIv1
import com.ventoray.projectmanager.api.WebService
import com.ventoray.projectmanager.data.dao.ProjectDao
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.util.PreferenceUtilK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class ProjectRepository @Inject constructor(private val projectDao: ProjectDao,
                                            private val webService: WebService) {


//    @Inject private val projectDao: ProjectDao = ProjectManagerDB.getDatabase(context).projectDao()
//    private val volleySingleton: VolleySingleton = VolleySingleton(context.applicationContext)
//    private val token: String? = PreferenceUtilK.getClientPasswordToken(context)
    private val activeProjects: LiveData<List<Project>> = projectDao.getAllActiveProjects()
    private val completedProjects: LiveData<List<Project>> = projectDao.getAllCompletedProjects()




    /**
     * WorkerThread annotation indicates method needs to be called from a non-UI thread.
     * Suspend modifier tells compiler that this needs to be called from a coroutine
     *
     * The compiler adds a parameter to suspend functions, a callback: Continuation<T> which has
     * a resume and resumeWithException function
     *
     * @param project: new project to add to table
     */
    @WorkerThread
    suspend fun insert(project: Project): Long {
        return projectDao.insert(project)
    }

    @WorkerThread
    suspend fun deleteAll(): Int {
        return projectDao.deleteAll()
    }

    /**
     * Inserts list of projects into the database
     * @param: projects - list of projects to be inserted into database
     */
    @WorkerThread
    suspend fun insertAll(projects: List<Project>): List<Long> {
        return projectDao.insertAll(projects)
    }

    fun searchActiveProjects(query: String, userId: Int, token: String): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, List<Project>> () {
            override fun onFetchFailed() {
                super.onFetchFailed()
            }

            override fun saveCallResult(item: List<Project>) {
                projectDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                Log.d("Should Fetch", data.isNullOrEmpty().toString())
                return data.isNullOrEmpty()
            }

            override fun loadFromDb() = projectDao.searchActiveProjects(query)

            override fun createCall() = webService.getUserProjects("Bearer $token", userId)
        }.asLiveData()

    }

    fun searchCompletedProjects(query: String, userId: Int, token: String): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, List<Project>> () {
            override fun onFetchFailed() {
                super.onFetchFailed()
            }

            override fun saveCallResult(item: List<Project>) {
                projectDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                Log.d("Should Fetch", data.isNullOrEmpty().toString())
                return data.isNullOrEmpty()
            }

            override fun loadFromDb() = projectDao.searchCompletedProjects(query)

            override fun createCall() = webService.getUserProjects("Bearer $token", userId)
        }.asLiveData()
    }

    /**
     * Retrieves all user's projects
     * @param userId: user ID for user currently logged into app (used in case we need to download from web)
     */
    fun getAllProjects(userId: Int): LiveData<List<Project>> {

        if (activeProjects.value.isNullOrEmpty()) { //TODO come up with condition to download data
            /**
             * Right now this strategy doesn't work.  It just pulls from the web every time (allprojects.value
             * is always not null).  Need to implement time limit or something else to decide when to pull from the web
             */
//            downLoadProjects(userId)
        } else {
            Log.i("ProjectRepo", "Retrieving projects from Database")
        }

        return activeProjects
    }


    fun loadActiveProjects(userId: Int, token: String): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, List<Project>> () {
            override fun onFetchFailed() {
                super.onFetchFailed()
            }

            override fun saveCallResult(item: List<Project>) {
                projectDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                Log.d("Should Fetch", data.isNullOrEmpty().toString())
               return data.isNullOrEmpty()
            }

            override fun loadFromDb() = projectDao.getAllActiveProjects()

            override fun createCall() =  webService.getUserProjects("Bearer $token", userId)

        }.asLiveData()
    }

    fun loadCompletedProjects(userId: Int, token: String): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, List<Project>> () {
            override fun onFetchFailed() {
                super.onFetchFailed()
            }

            override fun saveCallResult(item: List<Project>) {
                projectDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                Log.d("Should Fetch", data.isNullOrEmpty().toString())
                return data.isNullOrEmpty()
            }

            override fun loadFromDb() = projectDao.getAllCompletedProjects()

            override fun createCall() =  webService.getUserProjects("Bearer $token", userId)

        }.asLiveData()
    }




}