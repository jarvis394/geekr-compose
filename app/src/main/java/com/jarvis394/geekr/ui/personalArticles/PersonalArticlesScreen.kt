package com.jarvis394.geekr.ui.personalArticles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.jarvis394.geekr.ui.MainAppScreenKey
import com.jarvis394.geekr.ui.composables.Navigator
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data object PersonalArticlesScreenKey : MainAppScreenKey

fun EntryProviderScope<MainAppScreenKey>.personalArticlesScreenEntry(
    navigator: Navigator<MainAppScreenKey>
) {
    entry<PersonalArticlesScreenKey> { PersonalArticlesScreen(navigator) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalArticlesScreen(
    navigator: Navigator<MainAppScreenKey>,
    viewModel: PersonalArticlesViewModel = hiltViewModel()
) {
    val articles by viewModel.articles.collectAsState()

    fun navigateUp() {
        navigator.navigateBack()
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("My Articles", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }, navigationIcon = {
            IconButton(onClick = { navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back"
                )
            }
        })
    }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(articles) { article ->
                ListItem(
                    headlineContent = { Text(article.title) },
                    supportingContent = {
                        Text(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(article.createdAt)))
                    },
                    modifier = Modifier.clickable {
                        navigator.navigateTo(PersonalArticleScreenKey(article.id))
                    }
                )
                HorizontalDivider()
            }
            if (articles.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("No articles saved yet")
                    }
                }
            }
        }
    }
}
