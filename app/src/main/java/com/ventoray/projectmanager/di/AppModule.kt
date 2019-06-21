package com.ventoray.projectmanager.di

import android.app.Application
import android.arch.persistence.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ventoray.projectmanager.api.APIv1.Constants.URL_BASE
import com.ventoray.projectmanager.api.LiveDataCallAdapterFactory
import com.ventoray.projectmanager.api.WebService
import com.ventoray.projectmanager.data.ProjectManagerDB
import com.ventoray.projectmanager.data.dao.ProjectDao
import com.ventoray.projectmanager.data.dao.TaskDao
import com.ventoray.projectmanager.data.dao.UserDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])

class AppModule {

    @Singleton
    @Provides
    fun provideWebService(): WebService {

        val gson: Gson = GsonBuilder().setLenient().create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl("$URL_BASE/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(WebService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): ProjectManagerDB {
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

    @Singleton
    @Provides
    fun provideUserDao(db: ProjectManagerDB): UserDao {
        return db.userDao()
    }
}