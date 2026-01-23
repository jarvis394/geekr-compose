package com.jarvis394.geekr.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Hub(
    val id: String,
    val alias: String,
    val titleHtml: String,
    val imageUrl: String?,
    val descriptionHtml: String?,
    val statistics: HubStatistics
)

@Serializable
data class HubStatistics(
    val subscribersCount: Int,
    val rating: Double,
    val authorsCount: Int,
    val postsCount: Int
)

@Serializable
data class HubArticleLabels(
    val title: String,
    val alias: String,
    val id: String,
    val type: String,
    val titleHtml: String
)