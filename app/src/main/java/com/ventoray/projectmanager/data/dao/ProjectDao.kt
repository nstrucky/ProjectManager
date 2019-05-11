package com.ventoray.projectmanager.data.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import com.ventoray.projectmanager.data.datamodel.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE status = 'Completed'")
    fun getAllCompletedProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE status <> 'Completed'")
    fun getAllActiveProjects(): LiveData<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: Project): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg projects: Project): List<Long>

    @Delete
    fun delete(project: Project): Int

    @Query("DELETE FROM projects")
    fun deleteAll(): Int

    @Query("SELECT * FROM projects WHERE (name LIKE :query OR account_name LIKE :query) AND status <> 'Completed'")
    fun searchActiveProjects(query: String): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE (name LIKE :query OR account_name LIKE :query) AND status = 'Completed'")
    fun searchCompletedProjects(query: String): LiveData<List<Project>>
}