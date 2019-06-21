package com.ventoray.projectmanager.data.datamodel

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "projects")
data class Project(
    @SerializedName("id")
    @PrimaryKey
    var id: Int,
    @SerializedName("name")
    var name: String?,
    @SerializedName("account_name")
    var account_name: String?,
    @SerializedName("account_number")
    var account_number: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("due_date")
    var due_date: String?,
    @SerializedName("created_at")
    var created_at: String?,
    @SerializedName("updated_at")
    var updated_at: String?
) {

}
