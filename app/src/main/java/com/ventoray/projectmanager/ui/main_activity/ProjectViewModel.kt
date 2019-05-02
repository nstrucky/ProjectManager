package com.ventoray.projectmanager.ui.main_activity

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.repo.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository

    //Coroutine Scope var/vals
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        repository = ProjectRepository(application)
    }

    /**
     * @param userId - the user Id for user logged into apop
     * TODO eventually the api should be updated so the password grant token determines user id when sent to server
     * This is not a great design, if the api were ever opened to a public audience, then they could use any user's
     * ID to retrieve project data
     */
    fun allProjects(userId: Int): LiveData<List<Project>> {
        return repository.getAllProjects(userId)
    }

    fun activeProjects(): LiveData<List<Project>> {
        return repository.getActiveProjects()
    }

    fun completedProjects(): LiveData<List<Project>> {
        return repository.getCompletedProjects()
    }

    /**
     * Wrapper so repo's insert is completely hidden from UI
     * New coroutine based on scope defined...DB operations so Dispatchers.IO
     */
    fun insert(project: Project) = scope.launch(Dispatchers.IO){
        repository.insert(project) //suspended function in repository
    }

    /**
     * ViewModel is no longer used and will be destroyed
     */
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel() //cancels any long running jobs
    }
}