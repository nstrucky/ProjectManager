package com.ventoray.projectmanager.data.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.WorkerThread
import android.util.Log
import com.ventoray.projectmanager.api.WebService
import com.ventoray.projectmanager.data.dao.UserDao
import com.ventoray.projectmanager.data.datamodel.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao,
                                         private val webService: WebService) {

    fun getUser(token: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>() {

            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<User> {
                return userDao.getUser()
            }

            override fun createCall(): LiveData<User> {
                val user = webService.getUser("Bearer $token")
                return  user
            }
        }.asLiveData()
    }

    /**
     * removes user from database
     */
    @WorkerThread
    fun delete() = userDao.deleteUser()


}