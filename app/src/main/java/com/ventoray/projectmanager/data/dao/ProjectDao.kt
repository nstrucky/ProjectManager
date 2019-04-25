package com.ventoray.projectmanager.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.ventoray.projectmanager.data.datamodel.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY due_date ASC")
    fun getAllProjects(): LiveData<List<Project>>

    @Insert
    fun insert(project: Project)

//    @Query("DELETE FROM projects WHERE id = (:id)")
//    fun deleteProject(id: Int)

    @Delete
    fun delete(project: Project)

    @Query("DELETE FROM projects")
    fun deleteAll()


}