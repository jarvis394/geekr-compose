package com.jarvis394.geekr.ui.article

import com.jarvis394.geekr.data.model.Article

sealed class ArticleUIState {
    object Loading : ArticleUIState()
    data class Success(val article: Article) : ArticleUIState()
    data class Error(val message: String) : ArticleUIState()
}
