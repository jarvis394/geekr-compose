package com.jarvis394.geekr.ui.personalArticles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
data class PersonalArticleScreenKey(val id: Long, val showSavedSnackbar: Boolean = false) : MainAppScreenKey

fun EntryProviderScope<MainAppScreenKey>.personalArticleScreenEntry(
    navigator: Navigator<MainAppScreenKey>
) {
    entry<PersonalArticleScreenKey> { key -> PersonalArticleScreen(navigator, key.id, key.showSavedSnackbar) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalArticleScreen(
    navigator: Navigator<MainAppScreenKey>,
    id: Long,
    showSavedSnackbar: Boolean = false,
    viewModel: PersonalArticleViewModel = hiltViewModel(
        key = id.toString(),
        creationCallback = { factory: PersonalArticleViewModel.Factory ->
            factory.create(id)
        }
    )
) {
    val state = viewModel.uiState
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSavedSnackbar) {
        if (showSavedSnackbar) {
            snackbarHostState.showSnackbar("Article saved successfully")
        }
    }

    fun navigateUp() {
        navigator.navigateBack()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
        TopAppBar(title = {
            Text(
                state?.title ?: "Loading...",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back"
                )
            }
        }, actions = {
            IconButton(onClick = {
                state?.id?.let {
                    navigator.navigateTo(com.jarvis394.geekr.ui.newArticle.NewArticleScreenKey(it))
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Edit, contentDescription = "Edit"
                )
            }
            IconButton(onClick = {
                viewModel.deleteArticle {
                    navigateUp()
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Delete, contentDescription = "Delete"
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
            if (state == null) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = state.title,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    HtmlText(
                        html = state.content,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
