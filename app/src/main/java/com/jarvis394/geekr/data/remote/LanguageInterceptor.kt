package com.jarvis394.geekr.data.remote

import okhttp3.Interceptor
import okhttp3.Response

enum class FeedLanguage(val code: String) {
    RUSSIAN("ru"),
    ENGLISH("en"),
    ALL("ru,en"),
}

class LanguageInterceptor(private val feedLanguage: FeedLanguage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("fl", feedLanguage.code)
            .addQueryParameter("hl", feedLanguage.code)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}