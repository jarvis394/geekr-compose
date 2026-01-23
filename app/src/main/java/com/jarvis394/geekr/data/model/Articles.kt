package com.jarvis394.geekr.data.model

import kotlinx.serialization.Serializable

enum class ArticlesFlow {
    all, develop, design, admin, management, marketing, popsci
}

enum class ArticlesComplexity {
    all, easy, medium, hard
}

@Serializable
data class ArticlesResponse(
    val pagesCount: Int,
    val publicationIds: List<String>,
    val publicationRefs: Map<String, Article>
)
