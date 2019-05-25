package com.ventoray.projectmanager.data.datamodel

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey var id: Int = 0,
    var name: String = "",
    var account_name: String = "",
    var account_number: String = "",
    var description: String = "",
    var status: String = "",
    var due_date: String = "",
    var created_at: String = "",
    var updated_at: String = ""
) {

}
