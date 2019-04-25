package com.ventoray.projectmanager.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.data.datamodel.Project

class ProjectListAdapter internal constructor(
    context: Context?
) : RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var projects = emptyList<Project>() // Cached copy of projects

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectTextView: TextView = itemView.findViewById(R.id.projectNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_project_item, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val current = projects[position]
        holder.projectTextView.text = current.name
    }

    internal fun setProjects(projects: List<Project>) {
        this.projects = projects
        notifyDataSetChanged()
    }

    override fun getItemCount() = projects.size
}