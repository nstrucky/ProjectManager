package com.ventoray.projectmanager.ui.projects

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.util.DbUtil
import com.ventoray.projectmanager.databinding.RecyclerviewProjectItemBinding

class ProjectListAdapter internal constructor(): RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>() {

    private var projects = emptyList<Project>() // Cached copy of projects

    inner class ProjectViewHolder(binding: RecyclerviewProjectItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemBinding: RecyclerviewProjectItemBinding = binding

        fun bind(project: Project) {
            with(itemBinding) {
                this.project = project
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: RecyclerviewProjectItemBinding = RecyclerviewProjectItemBinding.inflate(layoutInflater, parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val current = projects[position]
        holder.bind(current)
    }

    internal fun setProjects(projects: List<Project>) {
        this.projects = projects
        notifyDataSetChanged()
    }

    override fun getItemCount() = projects.size
}
