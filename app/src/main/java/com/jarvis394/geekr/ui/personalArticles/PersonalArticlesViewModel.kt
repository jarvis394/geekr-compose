package com.jarvis394.geekr.ui.personalArticles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis394.geekr.data.model.PersonalArticle
import com.jarvis394.geekr.data.repository.PersonalArticlesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalArticlesViewModel @Inject constructor(
    private val repository: PersonalArticlesRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<List<PersonalArticle>>(emptyList())
    val articles: StateFlow<List<PersonalArticle>> = _articles

    init {
        viewModelScope.launch {
            repository.getAllArticles().collect { list ->
                _articles.value = list
            }
        }
    }
}
