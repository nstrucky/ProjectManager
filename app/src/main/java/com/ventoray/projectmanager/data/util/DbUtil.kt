package com.ventoray.projectmanager.data.util

import android.content.Context
import com.ventoray.projectmanager.data.repo.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DbUtil(context: Context) {

    private val context: Context

    init {
        this.context = context
    }

    //Coroutine Scope var/vals
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    /**
     * This function removes all user data from the database through each data model's respective repository
     * @param callback - This is a function which is passed the result message and success/failure boolean
     */
    fun removeAllUserData(callback: (String, Boolean)->Unit) = scope.launch(Dispatchers.IO) {
        var success: Boolean = true
        val deleted: Int = ProjectRepository(context.applicationContext).deleteAll()

        //TODO determine when to set success to 'false'

        var message: String = "\nDeleted $deleted Projects\n${ if (success) "Success" else "Error"} "

        callback(message, success)
    }
}