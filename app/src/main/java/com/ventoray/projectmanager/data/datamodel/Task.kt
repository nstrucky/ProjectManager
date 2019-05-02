package com.ventoray.projectmanager.data.datamodel

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey var id: Int = 0,
    var status: String,
    var completed: Boolean,
    var title: String,
    var star_date: String,
    var due_date: String,
    var completed_on: String,
    var first_name: String,
    var last_name: String,
    var name: String,
    var user_id: Int,
    var project_id: Int
) {

}