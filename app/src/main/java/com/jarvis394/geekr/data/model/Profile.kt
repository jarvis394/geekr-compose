package com.jarvis394.geekr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    val alias: String,
    val avatarUrl: String?,
    val fullname: String?,
    val speciality: String?
)