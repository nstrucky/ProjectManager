package com.ventoray.projectmanager.data.datamodel

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @SerializedName("id")
    @PrimaryKey
    var id: Int,
    @SerializedName("username")
    var username: String?,
    @SerializedName("first_name")
    var first_name: String?,
    @SerializedName("last_name")
    var last_name: String?,
    @SerializedName("user_role")
    var user_role: String?,
    @SerializedName("email")
    var email: String?) {

}

