package com.jarvis394.geekr.ui.newArticle

import com.jarvis394.geekr.data.model.Article

sealed class NewArticleUIState {
    object Loading : NewArticleUIState()
    data class Success(val article: Article) : NewArticleUIState()
    data class Error(val message: String) : NewArticleUIState()
}
