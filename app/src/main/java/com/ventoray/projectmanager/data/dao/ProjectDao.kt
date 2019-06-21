package com.ventoray.projectmanager.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.ventoray.projectmanager.data.datamodel.Project


@Dao
abstract class ProjectDao {
    @Query("SELECT * FROM projects")
    abstract fun getAllProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE status = 'Completed'")
    abstract fun getAllCompletedProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE status <> 'Completed'")
    abstract fun getAllActiveProjects(): LiveData<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(project: Project): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(projects: List<Project>): List<Long>

    @Delete
    abstract fun delete(project: Project): Int

    @Query("DELETE FROM projects")
    abstract fun deleteAll(): Int

    @Query("SELECT * FROM projects WHERE (name LIKE :query OR account_name LIKE :query) AND status <> 'Completed'")
    abstract fun searchActiveProjects(query: String): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE (name LIKE :query OR account_name LIKE :query) AND status = 'Completed'")
    abstract fun searchCompletedProjects(query: String): LiveData<List<Project>>
}