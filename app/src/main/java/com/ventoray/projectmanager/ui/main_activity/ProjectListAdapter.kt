package com.ventoray.projectmanager.ui.main_activity

import android.content.Context
import android.graphics.Color
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
        val secondaryTextView: TextView = itemView.findViewById(R.id.secondaryNameTextView)
        val dateMonthDay: TextView = itemView.findViewById(R.id.dueDateDayMonth)
        val dateYear: TextView = itemView.findViewById(R.id.dueDateYear)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_project_item, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val current = projects[position]
        holder.projectTextView.text = current.name
        holder.secondaryTextView.text = current.account_name
        holder.statusTextView.text = current.status

        holder.dateYear.text = current.due_date.substring(0, 4)
        holder.dateMonthDay.text = getDayMonth(current.due_date)
        holder.statusTextView.setBackgroundColor(getColor(current.status))
    }

    internal fun setProjects(projects: List<Project>) {
        this.projects = projects
        notifyDataSetChanged()
    }

    override fun getItemCount() = projects.size
}

/**
 * This function simply returns a color for a predetermined status code.
 *
 * End state goal is to have the user specify colors and status codes they would like to use
 * instead of statically coded values.
 */
private fun getColor(status: String): Int {
    when (status) {
        "In Progress" -> return 0xffFDCA2E.toInt()
        "Impl Complete" -> return 0xff4286f4.toInt()
        "Not Started" -> return 0xff777777.toInt()
        "On Hold" -> return 0xffFD6721.toInt()
        "Completed" -> return 0xff41f471.toInt()
    }

    return 0xffcccccc.toInt()
}

/**
 * Converts the numbers for day and month in a date string e.g. "1900-01-01"
 * to a string in the form of "1 Jan"
 */
private fun getDayMonth(dueDate: String): String {
    val day = dueDate.substring(8)  // 1900-01-01
    val monthNum = dueDate.substring(5, 7)
    var month: String = "Oops"
    when (monthNum) {
        "01" -> month = "Jan"
        "02" -> month = "Feb"
        "03" -> month = "Mar"
        "04" -> month = "Apr"
        "05" -> month = "May"
        "06" -> month = "Jun"
        "07" -> month = "Jul"
        "08" -> month = "Aug"
        "09" -> month = "Sep"
        "10" -> month = "Oct"
        "11" -> month = "Nov"
        "12" -> month = "Dec"
    }

    return "$day $month"

}