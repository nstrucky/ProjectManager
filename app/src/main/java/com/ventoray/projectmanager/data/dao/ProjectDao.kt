package com.ventoray.projectmanager.data.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import com.ventoray.projectmanager.data.datamodel.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): LiveData<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: Project): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg projects: Project): List<Long>


//    @Query("DELETE FROM projects WHERE id = (:id)")
//    fun deleteProject(id: Int)

    @Delete
    fun delete(project: Project)

    @Query("DELETE FROM projects")
    fun deleteAll()

}