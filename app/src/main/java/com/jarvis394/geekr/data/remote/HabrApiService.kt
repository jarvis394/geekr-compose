package com.jarvis394.geekr.data.remote

import com.jarvis394.geekr.data.model.Article
import com.jarvis394.geekr.data.model.ArticlesResponse
import com.jarvis394.geekr.data.model.Upload
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @Multipart
    @POST("v2/publication/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("t") type: RequestBody = "direct".toRequestBody("text/plain".toMediaType())
    ): Upload
}