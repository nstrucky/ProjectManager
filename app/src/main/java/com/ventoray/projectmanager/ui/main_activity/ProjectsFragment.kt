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
    private var FRAGMENT_TYPE_ACTIVE = 0
    private var FRAGMENT_TYPE_COMPLETED = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FRAGMENT_TYPE = arguments?.getInt(KEY_FRAGMENT_TYPE, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_projects, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val observable: LiveData<List<Project>>

        adapter = ProjectListAdapter(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        projectViewModel = activity?.run {
            ViewModelProviders.of(this).get(ProjectViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        subscribeUi()

        return view
    }


    fun subscribeUi(query: String? = null): Unit {
        val observable: LiveData<List<Project>>

        if (!query.isNullOrEmpty()) {
            if (FRAGMENT_TYPE == FRAGMENT_TYPE_ACTIVE){
                observable = projectViewModel.searchActiveProjects("%$query%")
            } else {
                observable = projectViewModel.searchCompletedProjects("%$query%")
            }
        } else if (FRAGMENT_TYPE == FRAGMENT_TYPE_ACTIVE) {
            observable = projectViewModel.activeProjects()
        } else {
            observable = projectViewModel.completedProjects()
        }

        observable.observe(this, Observer { projects ->
            projects?.let {
                adapter.setProjects(projects)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
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

        subscribeUi(event.query)

        Toast.makeText(this.context, event.query, Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * @param someText Parameter 1.
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
