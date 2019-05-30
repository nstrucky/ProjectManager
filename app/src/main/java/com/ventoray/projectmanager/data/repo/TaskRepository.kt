package com.ventoray.projectmanager.data.repo

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.ventoray.projectmanager.data.dao.TaskDao
import com.ventoray.projectmanager.data.datamodel.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TaskRepository @Inject constructor(val taskDao: TaskDao) {

//    val taskDao: TaskDao = ProjectManagerDB.getDatabase(context).taskDao()
//    private val token: String? = PreferenceUtilK.getClientPasswordToken(context)
//    private val volleySingleton: VolleySingleton = VolleySingleton(context.applicationContext)

    //Coroutine Scope var/vals
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    @WorkerThread
    suspend fun deleteAll(): Int {
        return taskDao.deleteAll()
    }

    @WorkerThread
    suspend fun getProjectTasks(projectId: Int): LiveData<List<Task>> {
        return taskDao.getProjectTasks(projectId)
    }

    @WorkerThread
    suspend fun insert(task: Task): Long {
        return taskDao.insert(task)
    }

    @WorkerThread
    suspend fun insertAll(tasks: List<Task>): List<Long> {
        return taskDao.insertAll(*tasks.toTypedArray())
    }


    /**
     * Downloads tasks from web
     * @param projectId: project ID for which to retrieve tasks from API
     */
//    fun downloadProjectTasks(projectId: Int) : MutableLiveData<List<Task>> {
//        val tasks: MutableLiveData<List<Task>> = MutableLiveData<List<Task>>()
//        val stringRequest: StringRequest = object : StringRequest(
//            Request.Method.GET,
//            APIv1.URL_PROJECTS + "/$projectId/tasks",
//            Response.Listener { response ->
//                val taskType = object : TypeToken<List<Task>>() {}.type
//                val newTasks = Gson().fromJson<List<Task>>(response, taskType)
//                tasks.value = newTasks
//
//                scope.launch(Dispatchers.IO) {
//                    val rows: List<Long>? = insertAll(newTasks)
//                    Log.i("TaskRepository", "Saving Tasks from coroutine ${rows?.size}")
//                }
//            },
//            Response.ErrorListener {
//                Log.e("TaskRepository", "Error" + it?.localizedMessage)
//            }
//        ) {
//            override fun getHeaders(): MutableMap<String, String> {
//                return super.getHeaders()
//            }
//        }
//
//        volleySingleton.addToRequestQueue(stringRequest)
//        return tasks
//    }
}