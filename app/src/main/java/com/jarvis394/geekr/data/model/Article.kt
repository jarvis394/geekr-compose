package com.jarvis394.geekr.data.model

import ArticleLabel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class ArticleComplexity {
    low, medium, high
}

enum class ArticleFormat {
    review, opinion, analytics, retrospective, tutorial,
    faq, roadmap, digest, case, interview, reportage,
    habrItMedia, megapostType, season, targetAudience
}

enum class PostType {
    article, news, post, voice
}

@Serializable
data class ArticleMetadata(
    val metaDescription: String,
    val schemaJsonLd: String,
    val shareImageUrl: String,
    val shareImageWidth: Int,
    val shareImageHeight: Int,
    val vkShareImageUrl: String
)

@Serializable
data class SchemaJsonLd(
    @SerialName("@context") val context: String,
    @SerialName("@type") val type: String,
    val headline: String,
    val datePublished: String,
    val author: JsonLdAuthor,
    val description: String,
    val url: String
)

@Serializable
data class JsonLdAuthor(
    @SerialName("@type") val type: String,
    val name: String
)

@Serializable
data class Article(
    val id: String,
    val author: Profile,
    val commentsEnabled: CommentsEnabled? = null,
    val complexity: ArticleComplexity?,
    val format: ArticleFormat?,
    val editorVersion: String,
    val flows: List<Flow>? = null,
    val hubs: List<HubArticleLabels>? = null,
    val isCorporative: Boolean? = null,
    val isInBlacklist: Boolean? = null,
    val isEditorial: Boolean? = null,
    val lang: String,
    val leadData: LeadData? = null,
    val metadata: ArticleMetadata? = null,
    val postLabels: List<ArticleLabel>,
    val postType: PostType,
    val readingTime: Int,
    val statistics: ArticleStatistics,
    val tags: List<Tag>? = null,
    val timePublished: String,
    val titleHtml: String,
    val textHtml: String? = null,
    val translationData: TranslationData? = null,
    val leadImage: String? = null
)

@Serializable
data class CommentsEnabled(
    val status: Boolean,
    val reason: String? = null
)

@Serializable
data class TranslationData(
    val originalAuthorName: String,
    val originalUrl: String
)

@Serializable
data class ArticleStatistics(
    val commentsCount: Int,
    val favoritesCount: Int,
    val readingCount: Int,
    val score: Int,
    val votesCount: Int,
    val votesCountMinus: Int,
    val votesCountPlus: Int
)

@Serializable
data class Tag(val titleHtml: String)

@Serializable
data class LeadData(
    val buttonTextHtml: String? = null,
    val imageUrl: String? = null,
    val textHtml: String,
    val image: LeadDataImage? = null,
)

@Serializable
data class LeadDataImage(
    val fit: String = "cover",
    val positionX: Int,
    val positionY: Int,
    val url: String
)