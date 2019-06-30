package com.ventoray.projectmanager.com.ventoray.projectmanager.ui.projects

import android.content.Context
import android.content.Intent
import android.databinding.BindingAdapter
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.util.DbUtil
import com.ventoray.projectmanager.ui.project.ProjectActivity
import com.ventoray.projectmanager.ui.project.ProjectActivity.Companion.PROJECT_ID_KEY
import com.ventoray.projectmanager.ui.project.ProjectActivity.Companion.PROJECT_NAME_KEY

object ProjectListBindings {


    /**
     * This function simply returns a color for a predetermined status code.
     *
     * End state goal is to have the user specify colors and status codes they would like to use
     * instead of statically coded values.
     */
    @BindingAdapter("statusColor")
    @JvmStatic
    fun setColor(view: View, status: String) {
        var color: Int = 0xffcccccc.toInt()
        when (status) {
            "In Progress" -> color = 0xffFDCA2E.toInt()
            "Impl Complete" -> color = 0xff4286f4.toInt()
            "Not Started" -> color = 0xff777777.toInt()
            "On Hold" -> color = 0xffFD6721.toInt()
            "Completed" -> color = 0xff41f471.toInt()
        }
        view.setBackgroundColor(color)
    }


    /**
     * Converts the numbers for day and month in a date string e.g. "1900-01-01"
     * to a string in the form of "1 Jan"
     */
    @BindingAdapter("dateText")
    @JvmStatic
    fun convertDate(textView: TextView, date: String) {
        val day = date.substring(8)  // 1900-01-01
        val monthNum = date.substring(5, 7)
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

        textView.text = "$day $month"
    }


    /**
     * This will set the visibility of the warning icon based on due date and status of project
     */
    @BindingAdapter("dueDate", "status", requireAll = true)
    @JvmStatic
    fun showWarning(imageView: ImageView, dueDate: String, status: String) {
        val today: Long = System.currentTimeMillis()
        //Eventually may be a good idea to save the date as Long in the DB instead of String
        val date: Long = DbUtil.getLongFromDate(dueDate)

        if (today > date && status != "Completed") {
            imageView.visibility = VISIBLE
        } else {
            imageView.visibility = GONE
        }
    }


    /**
     * Right now this method only handles clicks from the individual list items
     * in order to transition to a single ProjectActivity
     * @param context - the Activity containing the list (ProjectsActivity)
     */
    class ProjectListClickHandler(private val context: Context?) {

        fun onClickProject(view: View, project: Project) {
            val name = project.name
            val id = project.id
            Log.d("Handler", "Project name: $name")

            if (context != null) {
                val intent = Intent().apply {
                    putExtra(PROJECT_ID_KEY, id)
                    putExtra(PROJECT_NAME_KEY, name)
                    setClass(context, ProjectActivity::class.java)
                }
                context.startActivity(intent)
            } else {
                Log.e("ProjectBinding", "Context not provided to handler in order to start activity")
            }
        }
    }


}