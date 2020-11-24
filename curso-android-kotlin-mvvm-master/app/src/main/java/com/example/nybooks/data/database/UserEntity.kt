package com.example.nybooks.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var deviceId: String = "",
    var username: String = "",
    var email: String = "",
    var name: String = "")