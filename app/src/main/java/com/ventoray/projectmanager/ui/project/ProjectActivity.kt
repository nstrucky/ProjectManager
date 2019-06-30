package com.ventoray.projectmanager.ui.project

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ventoray.projectmanager.R

class ProjectActivity : AppCompatActivity() {

    private var projectName: String? = null
    private var projectId: Int = 0

    companion object {
        val PROJECT_NAME_KEY = "com.ventoray.projectmanager.ui.project.ProjectActivity.PROJECT_NAME_KEY"
        val PROJECT_ID_KEY = "com.ventoray.projectmanager.ui.project.ProjectActivity.PROJECT_ID_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getAndSetIntentData()
    }

    private fun getAndSetIntentData() {
        val extras = intent.extras

        if (extras != null) {
            projectId = extras.getInt(PROJECT_ID_KEY)
            projectName = extras.getString(PROJECT_NAME_KEY)

            supportActionBar?.title = projectName

            Log.d("ProjectActivity", "\nID: $projectId \nProject Name: $projectName")
        } else {
            Log.d("ProjectActivity", "No data passed to activity")
        }
    }
}
