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
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val webService: WebService
) {


    fun getUser(token: String, id: Int): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>() {
            override fun onFetchFailed() {
                super.onFetchFailed()
            }

            override fun processResponse(response: User): User {
                return super.processResponse(response)
            }

            override fun saveCallResult(item: User) {
                Log.d("UserRepo", "saving call result $item")
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?): Boolean {
                Log.d("UserRepo", "Should Fetch")
                return true
            }

            override fun loadFromDb(): LiveData<User> {
                Log.d("UserRepo", "Loading from DB")
                return userDao.getUser(id)
            }

            override fun createCall(): LiveData<User> {
                val user = webService.getUser("Bearer $token")
                Log.d("UserRepo", user.toString())
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