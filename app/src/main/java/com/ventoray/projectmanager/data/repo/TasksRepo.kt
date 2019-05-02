package com.ventoray.projectmanager.data.repo

import android.content.Context
import android.support.annotation.WorkerThread
import com.ventoray.projectmanager.data.ProjectManagerDB
import com.ventoray.projectmanager.data.dao.TaskDao

class TasksRepo(context: Context) {

    val taskDao: TaskDao = ProjectManagerDB.getDatabase(context).taskDao()

    @WorkerThread
    suspend fun deleteAll(): Int {
        return taskDao.deleteAll()
    }
}