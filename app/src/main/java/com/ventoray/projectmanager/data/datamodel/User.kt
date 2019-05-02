package com.ventoray.projectmanager.data.datamodel

import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

data class User(
    var id: Int = 0,
    var username: String = "",
    var first_name: String = "",
    var last_name: String = "",
    var user_role: String = "",
    var email: String = "") : Serializable {

}

