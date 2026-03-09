package com.jarvis394.geekr.ui.personalArticles

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis394.geekr.data.model.PersonalArticle
import com.jarvis394.geekr.data.repository.PersonalArticlesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = PersonalArticleViewModel.Factory::class)
class PersonalArticleViewModel @AssistedInject constructor(
    private val repository: PersonalArticlesRepository,
    @Assisted private val articleId: Long
) : ViewModel() {
    var uiState by mutableStateOf<PersonalArticle?>(null)
        private set

    init {
        viewModelScope.launch {
            repository.getArticleStreamById(articleId).collect { article ->
                uiState = article
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(articleId: Long): PersonalArticleViewModel
    }

    fun deleteArticle(onDeleted: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArticle(articleId)
            withContext(Dispatchers.Main) {
                onDeleted()
            }
        }
    }
}
