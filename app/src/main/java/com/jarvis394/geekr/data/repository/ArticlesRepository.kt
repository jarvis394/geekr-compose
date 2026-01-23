package com.jarvis394.geekr.data.repository

import com.jarvis394.geekr.data.model.Article
import com.jarvis394.geekr.data.model.ArticleParams
import com.jarvis394.geekr.data.model.ArticlesComplexity
import com.jarvis394.geekr.data.model.ArticlesFlow
import com.jarvis394.geekr.data.model.ArticlesModeParam
import com.jarvis394.geekr.data.remote.HabrApiService
import javax.inject.Inject

class ArticlesRepository @Inject constructor(
    private val api: HabrApiService
) {
    suspend fun getArticles(
        mode: ArticlesModeParam,
        page: Int = 1,
        flow: ArticlesFlow? = ArticlesFlow.all,
        complexity: ArticlesComplexity? = ArticlesComplexity.all,
        hubAlias: String? = null,
        perPage: Int = 20,
        news: Boolean = false,
        posts: Boolean = false
    ): Result<List<Article>> {
        return runCatching {
            val params = mutableMapOf<String, String?>()

            val modeParams = ArticleParams.getParamsForMode(mode)
            params.putAll(modeParams)

            params["sort"] = if (flow == ArticlesFlow.all) modeParams["sort"] else "all"

            params["page"] = page.toString()
            params["perPage"] = perPage.toString()
            params["hub"] = hubAlias ?: ""
            params["flow"] = if (flow == ArticlesFlow.all) "" else flow.toString()

            if (complexity != null && complexity != ArticlesComplexity.all) {
                params["complexity"] = complexity.toString()
            }
            if (news) params["news"] = "true"
            if (posts) params["posts"] = "true"

            val response = api.getArticles(params)

            response.publicationIds.mapNotNull { id ->
                response.publicationRefs[id]
            }
        }
    }
}