package com.ventoray.projectmanager.data.repo

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.ventoray.projectmanager.data.dao.ProjectDao
import com.ventoray.projectmanager.data.datamodel.Project

class ProjectRepository(private val projectDao: ProjectDao) {

    val projects: LiveData<List<Project>> = projectDao.getAllProjects()

    /**
     * WorkerThread annotation indicates method needs to be called from a non-UI thread.
     * Suspend modifier tells compiler that this needs to be called from a coroutine
     *
     * @param project: new project to add to table
     */
    @WorkerThread
    suspend fun insert(project: Project) {
        projectDao.insert(project)
    }

}