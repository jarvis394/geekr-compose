package com.jarvis394.geekr.ui.article

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.jarvis394.geekr.ui.MainAppScreenKey
import com.jarvis394.geekr.ui.composables.HtmlText
import com.jarvis394.geekr.ui.composables.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class ArticleScreenKey(val id: String) : MainAppScreenKey

fun EntryProviderScope<MainAppScreenKey>.articleScreenEntry(
    navigator: Navigator<MainAppScreenKey>
) {
    entry<ArticleScreenKey> { key -> ArticleScreen(navigator, key.id) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    navigator: Navigator<MainAppScreenKey>,
    id: String,
    viewModel: ArticleViewModel = hiltViewModel(
        key = id,
        creationCallback = { factory: ArticleViewModel.Factory ->
            factory.create(id)
        }
    )
) {
    val state = viewModel.uiState
    val scrollState = rememberScrollState()
    val topBarTitle = when (state) {
        is ArticleUIState.Success -> state.article.titleHtml
        else -> ""
    }

    fun navigateUp() {
        navigator.navigateBack()
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                topBarTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back"
                )
            }
        })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(padding)
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(scrollState),
        ) {
            when (state) {
                is ArticleUIState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is ArticleUIState.Error -> Text(
                    "Error: ${state.message}",
                    color = MaterialTheme.colorScheme.error
                )

                is ArticleUIState.Success -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = state.article.titleHtml,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        state.article.textHtml?.let { HtmlText(html = it) }
                    }
                }
            }
        }
    }
}