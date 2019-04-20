package com.ventoray.projectmanager.web

import java.io.Serializable
import java.sql.Date

data class User(
    var id: Int = 0,
    var username: String = "",
    var first_name: String = "",
    var last_name: String = "",
    var user_role: String = "",
    var email: String = "") : Serializable {

}

data class Project(
    var id: Int = 0,
    var name: String = "",
    var account_name: String = "",
    var account_number: String = "",
    var description: String = "",
    var status: String = "",
    var due_date: Date = Date(0),
    var created_at: Date = Date(0),
    var updated_at: Date = Date(0)
    ) {

}
