package com.jarvis394.geekr.data.model

sealed interface ArticlesModeParam

enum class ArticlesRating : ArticlesModeParam {
    all, rated0, rated10, rated25, rated50, rated100
}

enum class ArticlesPeriod : ArticlesModeParam {
    daily, weekly, monthly, yearly, alltime
}

object ArticleParams {
    private val modeConfig: Map<ArticlesModeParam, Map<String, String>> = mapOf(
        ArticlesRating.all to mapOf("sort" to "rating"),
        ArticlesRating.rated0 to mapOf("sort" to "rating", "score" to "0"),
        ArticlesRating.rated10 to mapOf("sort" to "rating", "score" to "10"),
        ArticlesRating.rated25 to mapOf("sort" to "rating", "score" to "25"),
        ArticlesRating.rated50 to mapOf("sort" to "rating", "score" to "50"),
        ArticlesRating.rated100 to mapOf("sort" to "rating", "score" to "100"),
        ArticlesPeriod.daily to mapOf("sort" to "date", "period" to "daily"),
        ArticlesPeriod.weekly to mapOf("sort" to "date", "period" to "weekly"),
        ArticlesPeriod.monthly to mapOf("sort" to "date", "period" to "monthly"),
        ArticlesPeriod.yearly to mapOf("sort" to "date", "period" to "yearly"),
        ArticlesPeriod.alltime to mapOf("sort" to "date", "period" to "alltime")
    )

    // 4. Use the sealed interface as the function parameter type
    fun getParamsForMode(mode: ArticlesModeParam): Map<String, String> {
        return modeConfig[mode] ?: emptyMap()
    }
}