package com.jarvis394.geekr.ui.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val alias: String,
    val fullname: String?,
    val avatarUrl: String?
)