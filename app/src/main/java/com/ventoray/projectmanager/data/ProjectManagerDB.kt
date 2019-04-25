package com.ventoray.projectmanager.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.ventoray.projectmanager.data.dao.ProjectDao
import com.ventoray.projectmanager.data.datamodel.Project

@Database(entities = [Project::class], version = 1)
public abstract class ProjectManagerDB : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: ProjectManagerDB? = null

        fun getDatabase(context: Context): ProjectManagerDB {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProjectManagerDB::class.java,
                    "Project_Manager_Database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }


    abstract fun projectDao(): ProjectDao



}