package com.ventoray.projectmanager.ui.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.ventoray.projectmanager.data.datamodel.User
import com.ventoray.projectmanager.data.repo.Resource
import com.ventoray.projectmanager.data.repo.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(val userRepository: UserRepository): ViewModel() {

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token
//
//    val user: LiveData<Resource<User>> = Transformations.switchMap(_token) { token ->
//        Log.d("TOKEN", token)
//        userRepository.retrieveUser(token)
//    }

    fun getUser(token: String, id: Int): LiveData<Resource<User>> {
        return userRepository.getUser(token = token, id = id)
    }

    fun setTokenData(token: String?) {
        Log.d("TOKEN2", token)
        if (_token.value != token) {
            Log.d("TOKEN", "Did not match!")
            _token.value = token
        }
    }
}