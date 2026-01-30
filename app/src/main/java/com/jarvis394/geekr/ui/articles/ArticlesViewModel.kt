package com.jarvis394.geekr.ui.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis394.geekr.data.model.ArticlesComplexity
import com.jarvis394.geekr.data.model.ArticlesModeParam
import com.jarvis394.geekr.data.model.ArticlesPeriod
import com.jarvis394.geekr.data.model.ArticlesRating
import com.jarvis394.geekr.data.repository.ArticlesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ArticlesFilter(val mode: ArticlesModeParam, val label: String, val extendedLabel: String) {
    DAILY(ArticlesPeriod.daily, "Daily", "Top daily articles"),
    WEEKLY(ArticlesPeriod.weekly, "Weekly", "Top weekly articles"),
    MONTHLY(ArticlesPeriod.monthly, "Monthly", "Top monthly articles"),
    RECENT(ArticlesRating.all, "Recent", "Recent articles")
}

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val repository: ArticlesRepository) :
    ViewModel() {
    private val _state = MutableStateFlow<ArticlesUIState>(ArticlesUIState.Loading)
    val state: StateFlow<ArticlesUIState> = _state

    private val _currentFilter = MutableStateFlow(ArticlesFilter.DAILY)
    val currentFilter: StateFlow<ArticlesFilter> = _currentFilter.asStateFlow()

    init {
        fetchArticles()
    }

    fun setFilter(filter: ArticlesFilter) {
        _currentFilter.value = filter
        fetchArticles(filter.mode)
    }

    fun fetchArticles(mode: ArticlesModeParam = _currentFilter.value.mode) {
        viewModelScope.launch {
            _state.value = ArticlesUIState.Loading
            val result = repository.getArticles(
                mode = mode, complexity = ArticlesComplexity.all
            )
            result.onSuccess { articles ->
                if (articles.isEmpty()) {
                    _state.value = ArticlesUIState.Empty
                } else {
                    _state.value = ArticlesUIState.Success(articles)
                }
            }.onFailure { error ->
                _state.value = ArticlesUIState.Error(error.message ?: "Unknown error")
            }
        }
    }
}