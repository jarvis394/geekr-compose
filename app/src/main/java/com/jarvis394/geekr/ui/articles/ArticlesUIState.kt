package com.jarvis394.geekr.ui.articles

import com.jarvis394.geekr.data.model.Article

sealed class ArticlesUIState {
    object Loading : ArticlesUIState()
    object Empty : ArticlesUIState()
    data class Success(val articles: List<Article>) : ArticlesUIState()
    data class Error(val message: String) : ArticlesUIState()
}