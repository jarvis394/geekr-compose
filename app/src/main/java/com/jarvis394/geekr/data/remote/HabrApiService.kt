package com.jarvis394.geekr.data.remote

import com.jarvis394.geekr.data.model.Article
import com.jarvis394.geekr.data.model.ArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface HabrApiService {
    @GET("v2/articles")
    suspend fun getArticles(
        @QueryMap params: Map<String, String?>
    ): ArticlesResponse

    @GET("v2/articles/{id}")
    suspend fun getArticle(
        @Path("id") articleId: String
    ): Article
}