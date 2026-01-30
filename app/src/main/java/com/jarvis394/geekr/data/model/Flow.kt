package com.jarvis394.geekr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Flow(
    val id: String,
    val name: String? = null,
    val title: String? = null,
    val titleHtml: String? = null,
    val alias: String,
    val url: String? = null
)