package com.ventoray.projectmanager.ui.main_activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.util.EventBusUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val KEY_FRAGMENT_TYPE = "com.ventoray.projectmanager.ui.main_activity.ProjectsPageAdapter.fragmentTypeKey"

/**
 * A [Fragment] subclass.
 * Use the [ProjectsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProjectsFragment : Fragment() {

    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var adapter: ProjectListAdapter
    private var FRAGMENT_TYPE: Int? = 0
    private val FRAGMENT_TYPE_ACTIVE = 0
    private val FRAGMENT_TYPE_COMPLETED = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FRAGMENT_TYPE = arguments?.getInt(KEY_FRAGMENT_TYPE, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        projectViewModel = activity?.run {
            ViewModelProviders.of(this).get(ProjectViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        if (FRAGMENT_TYPE == FRAGMENT_TYPE_ACTIVE) {
            subscribeUi(projectViewModel.activeProjects())
        } else {
            subscribeUi(projectViewModel.completedProjects())
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchEvent(event: EventBusUtil.SearchEvent) : Unit {
        val liveData: LiveData<List<Project>>
        if (!event.query.isNullOrEmpty()) {
            if (FRAGMENT_TYPE == FRAGMENT_TYPE_ACTIVE){
                liveData = projectViewModel.searchActiveProjects("%${event.query}%")
            } else {
                liveData = projectViewModel.searchCompletedProjects("%${event.query}%")
            }
        } else if (FRAGMENT_TYPE == FRAGMENT_TYPE_ACTIVE) {
            liveData = projectViewModel.activeProjects()
        } else {
            liveData = projectViewModel.completedProjects()
        }

        subscribeUi(liveData)
    }

    fun subscribeUi(liveData: LiveData<List<Project>>): Unit {
        liveData.observe(this, Observer { projects ->
            projects?.let {
                adapter.setProjects(projects)
            }
        })
    }

    companion object {
        /**
         * @param fragmentType - type of fragment, either Active or Completed (Inactive) projects
         * @return A new instance of fragment ProjectsFragment.
         */
        @JvmStatic
        fun newInstance(fragmentType: Int) =
            ProjectsFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_FRAGMENT_TYPE, fragmentType)

                }
            }
    }
}
