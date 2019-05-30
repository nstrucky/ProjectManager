package com.ventoray.projectmanager.ui.main_activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.repo.ProjectRepository
import com.ventoray.projectmanager.data.repo.Resource
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ProjectViewModel @Inject constructor(val projectRepository: ProjectRepository) : ViewModel() {

//    private val _userLogin = MutableLiveData<UserLogin>()
//
//    val projects: LiveData<Resource<List<Project>>> = Transformations
//        .switchMap(_userLogin) { userLogin -> projectRepository.loadProjects(userLogin.userId, userLogin.token)}
//
//    fun setUserLogin(userLogin: UserLogin) {
//        if (_userLogin.value?.userId != userLogin.userId) {
//            _userLogin.value = userLogin
//        }
//    }

    fun getAllProjects(userLogin: UserLogin): LiveData<Resource<List<Project>>> {
        return projectRepository.loadProjects(userLogin.userId, userLogin.token)
    }

    /**
     * @param userId - the user Id for user logged into apop
     * TODO eventually the api should be updated so the password grant token determines user id when sent to server
     * This is not a great design, if the api were ever opened to a public audience, then they could use any user's
     * ID to retrieve project data
     */
//    fun allProjects(userId: Int): LiveData<List<Project>> {
//        return projectRepository.getAllProjects(userId)
//    }
//
//    fun activeProjects(): LiveData<List<Project>> {
//        return projectRepository.getActiveProjects()
//    }
//
//    fun completedProjects(): LiveData<List<Project>> {
//        return projectRepository.getCompletedProjects()
//    }
//
//    /**
//     * Wrapper so repo's insert is completely hidden from UI
//     * New coroutine based on scope defined...DB operations so Dispatchers.IO
//     */
//    fun insert(project: Project) = scope.launch(Dispatchers.IO){
//        projectRepository.insert(project) //suspended function in projectRepository
//    }
//
//    fun searchActiveProjects(query: String): LiveData<List<Project>> {
//        return projectRepository.searchActiveProjects(query)
//    }
//
//    fun searchCompletedProjects(query: String): LiveData<List<Project>> {
//        return projectRepository.searchCompletedProjects(query)
//    }
//
//
//
//    fun getAllProjects(token: String, userId: Int): LiveData<Resource<List<Project>>> {
//        return projectRepository.loadProjects(userId, token)
//    }


    /**
     * ViewModel is no longer used and will be destroyed
     */
//    override fun onCleared() {
//        super.onCleared()
//        parentJob.cancel() //cancels any long running jobs
//    }
}

class UserLogin(val token: String, val userId: Int) {

}


