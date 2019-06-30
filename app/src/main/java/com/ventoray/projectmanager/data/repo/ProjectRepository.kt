package com.ventoray.projectmanager.data.repo

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import android.util.Log
import com.ventoray.projectmanager.api.WebService
import com.ventoray.projectmanager.data.dao.ProjectDao
import com.ventoray.projectmanager.data.datamodel.Project
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(private val projectDao: ProjectDao,
                                            private val webService: WebService) {

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

            override fun saveCallResult(item: List<Project>) {
                projectDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                Log.d("ProjectRepo", "Should Fetch " + data.isNullOrEmpty().toString())
                return data.isNullOrEmpty()
            }

            override fun loadFromDb() = projectDao.searchActiveProjects(query)

            override fun createCall() = webService.getUserProjects("Bearer $token", userId)
        }.asLiveData()

    }

    fun searchCompletedProjects(query: String, userId: Int, token: String): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, List<Project>> () {

            override fun saveCallResult(item: List<Project>) {
                projectDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                Log.d("ProjectRepo", "Should Fetch " + data.isNullOrEmpty().toString())
                return data.isNullOrEmpty()
            }

            override fun loadFromDb() = projectDao.searchCompletedProjects(query)

            override fun createCall() = webService.getUserProjects("Bearer $token", userId)
        }.asLiveData()
    }

    fun loadActiveProjects(userId: Int, token: String): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, List<Project>> () {

            override fun saveCallResult(item: List<Project>) {
                projectDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                Log.d("ProjectRepo", "Should Fetch active " + data.isNullOrEmpty().toString())
               return data.isNullOrEmpty()
            }

            override fun loadFromDb() = projectDao.getAllActiveProjects()

            override fun createCall() =  webService.getUserProjects("Bearer $token", userId)

        }.asLiveData()
    }

    fun loadCompletedProjects(userId: Int, token: String): LiveData<Resource<List<Project>>> {
        return object : NetworkBoundResource<List<Project>, List<Project>> () {

            override fun saveCallResult(item: List<Project>) {
                projectDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Project>?): Boolean {
                Log.d("ProjectRepo", "Should Fetch completed " + data.isNullOrEmpty().toString())
                return data.isNullOrEmpty()
            }

            override fun loadFromDb() = projectDao.getAllCompletedProjects()

            override fun createCall() =  webService.getUserProjects("Bearer $token", userId)

        }.asLiveData()
    }




}