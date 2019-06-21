package com.ventoray.projectmanager.data.util

import com.ventoray.projectmanager.data.repo.ProjectRepository
import com.ventoray.projectmanager.data.repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DbUtil @Inject constructor(private val projectRepo: ProjectRepository,
                                 private val userRepository: UserRepository
) {

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
        val deleted: Int = projectRepo.deleteAll()
        val uDeleted: Int = userRepository.delete()


        //TODO determine when to set success to 'false'

        var message: String = "\nDeleted $deleted Projects\n${ if (success) "Success" else "Error"} "

        callback(message, success)
    }

    companion object {
        /**
         * Simply returns the number of milliseconds from the lang date given a string from project_manager DB
         * @param date - date string from DB
         */
        fun getLongFromDate(date: String?): Long {
            return SimpleDateFormat("yyyy-MM-dd").parse(date).time
        }
    }

}