package com.ventoray.projectmanager.data.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.support.annotation.WorkerThread
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ventoray.projectmanager.data.ProjectManagerDB
import com.ventoray.projectmanager.data.dao.ProjectDao
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.util.PreferenceUtilK
import com.ventoray.projectmanager.web.APIv1
import com.ventoray.projectmanager.web.VolleySingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProjectRepository(context: Context) {

    private val projectDao: ProjectDao = ProjectManagerDB.getDatabase(context).projectDao()
    private val volleySingleton: VolleySingleton = VolleySingleton(context.applicationContext)
    private val token: String? = PreferenceUtilK.getClientPasswordToken(context)
    private val allProjects: LiveData<List<Project>> = projectDao.getAllProjects()

    //Coroutine Scope var/vals
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


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
     * See this post for info on * operator
     *      https://stackoverflow.com/questions/39389003/kotlin-asterisk-operator-before-variable-name-or-spread-operator-in-kotlin
     * @param: projects - list of projects to be inserted into database
     */
    @WorkerThread
    suspend fun insertAll(projects: List<Project>): List<Long> {
        return projectDao.insertAll(*projects.toTypedArray())
    }

    /**
     * Retrieves all user's projects
     * @param userId: user ID for user currently logged into app (used in case we need to download from web)
     */
    fun getAllProjects(userId: Int): LiveData<List<Project>> {

        if (allProjects.value.isNullOrEmpty()) { //TODO come up with condition to download data
            /**
             * Right now this strategy doesn't work.  It just pulls from the web every time (allprojects.value
             * is always not null).  Need to implement time limit or something else to decide when to pull from the web
             */
            downLoadProjects(userId)
        } else {
            Log.i("ProjectRepo", "Retrieving projects from Database")
        }

        return allProjects
    }


    /**
     * Downloads projects from web
     * @param userId: user ID for user currently logged into app
     */
    fun downLoadProjects(userId: Int): MutableLiveData<List<Project>> {
        val projects: MutableLiveData<List<Project>> = MutableLiveData<List<Project>>()
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET,
            APIv1.URL_USERS + "/$userId/projects",
            Response.Listener { response ->
                Log.d("ProjectRepo", response)
                val projectType = object : TypeToken<List<Project>>() {}.type
                val newProjects = Gson().fromJson<List<Project>>(response, projectType)// https://stackoverflow.com/questions/33381384/how-to-use-typetoken-generics-with-gson-in-kotlin
                projects.value = newProjects

                scope.launch(Dispatchers.IO) {
                    val rows: List<Long>? = insertAll(newProjects)//Need to pass newProjects here because projects.value is mutable and may have changed
                    Log.d("ProjectRepo", "Saving Projects from coroutine ${rows?.size}")
                }
            },
            Response.ErrorListener {
                Log.d("ProjectRepo", "Error" + it?.localizedMessage)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply { put(APIv1.HEADER_AUTHORIZATION, "Bearer $token") }
            }
        }

        volleySingleton.addToRequestQueue(stringRequest)
        Log.d("ProjectRepo", "retrieved projects list from Web")
        return projects
    }

}