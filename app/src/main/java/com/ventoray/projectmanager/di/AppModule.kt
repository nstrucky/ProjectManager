package com.ventoray.projectmanager.di

import android.app.Application
import android.arch.persistence.room.Room
import com.ventoray.projectmanager.ProjectManagerApp
import com.ventoray.projectmanager.data.ProjectManagerDB
import com.ventoray.projectmanager.data.dao.ProjectDao
import com.ventoray.projectmanager.data.dao.TaskDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])

class AppModule {

//    @Singleton
//    @Provides
//    fun provideGithubService(): GithubService {
//        return Retrofit.Builder()
//            .baseUrl("https://api.github.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(LiveDataCallAdapterFactory())
//            .build()
//            .create(GithubService::class.java)
//    }

    @Singleton
    @Provides
    fun provideDb(app: ProjectManagerApp): ProjectManagerDB {
        return Room
            .databaseBuilder(app, ProjectManagerDB::class.java, "projectmanager.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideProjectDao(db: ProjectManagerDB): ProjectDao {
        return db.projectDao()
    }

    @Singleton
    @Provides
    fun provideTaskDao(db: ProjectManagerDB): TaskDao {
        return db.taskDao()
    }
}