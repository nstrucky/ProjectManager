package com.ventoray.projectmanager.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.ventoray.projectmanager.data.datamodel.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tasks: Task): List<Long>

    @Query("SELECT * FROM tasks WHERE user_id = :userId")
    fun getUserTasks(userId: Int): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE project_id = :projectId")
    fun getProjectTasks(projectId: Int): LiveData<List<Task>>

    @Delete
    fun delete(task: Task): Int

    @Query("DELETE FROM tasks")
    fun deleteAll(): Int

}