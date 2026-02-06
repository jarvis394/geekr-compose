package com.jarvis394.geekr.ui.article

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis394.geekr.data.repository.ArticleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ArticleViewModel.Factory::class)
class ArticleViewModel @AssistedInject constructor(
    private val repository: ArticleRepository,
    @Assisted private val articleId: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(articleId: String): ArticleViewModel
    }

    var uiState by mutableStateOf<ArticleUIState>(ArticleUIState.Loading)
        private set

    init {
        fetchArticle()
    }

    private fun fetchArticle() {
        viewModelScope.launch {
            try {
                val article = repository.getArticle(articleId)
                uiState = ArticleUIState.Success(article)
            } catch (e: Exception) {
                uiState = ArticleUIState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
