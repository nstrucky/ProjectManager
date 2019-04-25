package com.ventoray.projectmanager.ui.fragment

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

import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.viewmodel.ProjectViewModel
import com.ventoray.projectmanager.ui.adapter.ProjectListAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TEST_TEXT = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProjectsFragment : Fragment() {


    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var adapter: ProjectListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        projectViewModel = activity?.run {
            ViewModelProviders.of(this).get(ProjectViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        projectViewModel.allProjects.observe(this, Observer { projects ->
            projects?.let { adapter.setProjects(it) }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_projects, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        adapter = ProjectListAdapter(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

//        projectViewModel.insert(Project(Math.random().toInt(), "Test Name"))

        return view
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)



    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        /**
         * @param someText Parameter 1.
         * @return A new instance of fragment ProjectsFragment.
         */
        @JvmStatic
        fun newInstance(someText: String) =
            ProjectsFragment().apply {
                arguments = Bundle().apply {
                    putString(TEST_TEXT, someText)
                }
            }
    }
}
