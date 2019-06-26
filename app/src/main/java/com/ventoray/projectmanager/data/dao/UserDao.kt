package com.ventoray.projectmanager.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.ventoray.projectmanager.data.datamodel.User

@Dao
abstract class UserDao {

    /**
     *   Selects the user saved in database
     *   Note: there should only be one ever saved
     */
    @Query("SELECT * FROM user ORDER BY id LIMIT 1")
    abstract fun getUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: User): Long

    /**
     *  Removes the one user that should be in the database
     */
    @Query("DELETE FROM user")
    abstract fun deleteUser(): Int

}