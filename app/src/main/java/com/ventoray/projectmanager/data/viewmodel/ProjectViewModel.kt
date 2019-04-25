package com.ventoray.projectmanager.data.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.ventoray.projectmanager.data.ProjectManagerDB
import com.ventoray.projectmanager.data.dao.ProjectDao
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.repo.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    val allProjects: LiveData<List<Project>>

    private val repository: ProjectRepository
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        val projectDao: ProjectDao = ProjectManagerDB.getDatabase(application).projectDao()
        repository = ProjectRepository(projectDao)
        allProjects = repository.projects

    }

    /**
     * Wrapper so repo's insert is completely hidden from UI
     * New coroutine based on scope defined...DB operations so Dispatchers.IO
     */
    fun insert(project: Project) = scope.launch(Dispatchers.IO){
        repository.insert(project)
    }

    /**
     * ViewModel is no longer used and will be destroyed
     */
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel() //cancels any long running jobs
    }
}