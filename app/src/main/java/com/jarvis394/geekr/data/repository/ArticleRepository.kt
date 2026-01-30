package com.jarvis394.geekr.data.repository

import com.jarvis394.geekr.data.model.Article
import com.jarvis394.geekr.data.model.ArticleParams
import com.jarvis394.geekr.data.model.ArticlesComplexity
import com.jarvis394.geekr.data.model.ArticlesFlow
import com.jarvis394.geekr.data.model.ArticlesModeParam
import com.jarvis394.geekr.data.remote.HabrApiService
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val api: HabrApiService
) {
    suspend fun getArticle(
        id: String
    ): Article {
        return run {
            api.getArticle(id)
        }
    }
}