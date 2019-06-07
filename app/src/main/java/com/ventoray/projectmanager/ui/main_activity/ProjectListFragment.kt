package com.ventoray.projectmanager.ui.main_activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.repo.Resource
import com.ventoray.projectmanager.di.Injectable
import com.ventoray.projectmanager.di.ViewModelFactory
import com.ventoray.projectmanager.util.EventBusUtil
import com.ventoray.projectmanager.util.PreferenceUtilK
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

private const val KEY_FRAGMENT_TYPE = "com.ventoray.projectmanager.ui.main_activity.ProjectsPageAdapter.fragmentTypeKey"

/**
 * A [Fragment] subclass.
 * Use the [ProjectListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProjectListFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var adapter: ProjectListAdapter
    private lateinit var userLogin: UserLogin
    private var FRAGMENT_TYPE: Int? = 0
    private val FRAGMENT_TYPE_ACTIVE = 0
    private val FRAGMENT_TYPE_COMPLETED = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FRAGMENT_TYPE = arguments?.getInt(KEY_FRAGMENT_TYPE, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        projectViewModel = activity?.run {
            projectViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProjectViewModel::class.java)
//        } ?: throw Exception("Invalid Activity")

        var token = PreferenceUtilK.getClientPasswordToken(this.requireContext())

        if (token.isNullOrEmpty()) token = ""
        userLogin = UserLogin(token, 2)//TODO change to signed in user

        /**
         *  This may not be the best solution right now because the WebService will be called for both fragments
         */
        if (FRAGMENT_TYPE == FRAGMENT_TYPE_ACTIVE) {
            subscribeUi(projectViewModel.activeProjects(userLogin))
        } else {
            subscribeUi(projectViewModel.completedProjects(userLogin))
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_projects, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)

        adapter = ProjectListAdapter(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchEvent(event: EventBusUtil.SearchEvent) : Unit {
        var liveData: LiveData<Resource<List<Project>>>? = null
        if (!event.query.isNullOrEmpty()) {
            if (FRAGMENT_TYPE == FRAGMENT_TYPE_ACTIVE){
                liveData = projectViewModel.searchActiveProjects("%${event.query}%", userLogin)
            } else {
                liveData = projectViewModel.searchCompletedProjects("%${event.query}%", userLogin)
            }
        } else if (FRAGMENT_TYPE == FRAGMENT_TYPE_ACTIVE) {
            liveData = projectViewModel.activeProjects(userLogin)
        } else {
            liveData = projectViewModel.completedProjects(userLogin)
        }

        subscribeUi(liveData)
    }

    fun subscribeUi(liveData: LiveData<Resource<List<Project>>>?): Unit {
        Log.d("subscribeUi", "Subscribing the UI")
        liveData?.observe(this, Observer { projects ->
            projects?.let {
                if (projects.data == null) {
                    adapter.setProjects(emptyList())
                } else {
                    adapter.setProjects(projects.data)
                }

            }
        })
    }

    companion object {
        /**
         * @param fragmentType - type of fragment, either Active or Completed (Inactive) projects
         * @return A new instance of fragment ProjectListFragment.
         */
        @JvmStatic
        fun newInstance(fragmentType: Int) =
            ProjectListFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_FRAGMENT_TYPE, fragmentType)

                }
            }
    }
}
