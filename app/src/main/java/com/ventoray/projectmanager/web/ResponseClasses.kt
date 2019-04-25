package com.ventoray.projectmanager.web

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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

