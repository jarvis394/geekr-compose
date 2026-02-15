package com.jarvis394.geekr.ui.articles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import com.jarvis394.geekr.data.model.Article
import com.jarvis394.geekr.ui.MainAppScreenKey
import com.jarvis394.geekr.ui.article.ArticleScreenKey
import com.jarvis394.geekr.ui.composables.AppBar
import com.jarvis394.geekr.ui.composables.ArticleItem.ArticleItem
import com.jarvis394.geekr.ui.composables.ArticlesSwitcherRow
import com.jarvis394.geekr.ui.composables.Navigator
import com.jarvis394.geekr.ui.composables.UserProfileDialog
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.serialization.Serializable

@Serializable
data object ArticlesScreenKey : MainAppScreenKey

fun EntryProviderScope<MainAppScreenKey>.articlesScreenEntry(navigator: Navigator<MainAppScreenKey>) {
    entry<ArticlesScreenKey> { ArticlesScreen(navigator) }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ArticlesScreen(
    navigator: Navigator<MainAppScreenKey>,
    viewModel: ArticlesViewModel = hiltViewModel(),
) {
    var isProfileDialogOpen by remember { mutableStateOf(false) }
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val currentFilter by viewModel.currentFilter.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle(null)
    val hazeState = rememberHazeState()

    fun onFilterSelected(filter: ArticlesFilter) {
        viewModel.setFilter(filter)
    }

    fun onRetry() {
        viewModel.fetchArticles()
    }

    fun onArticleItemClick(article: Article) {
        navigator.navigateTo(ArticleScreenKey(article.id))
    }

    fun onAvatarClick() {
        isProfileDialogOpen = true
    }

    fun handleDialogClose() {
        isProfileDialogOpen = false
    }

    SharedTransitionLayout {
        Scaffold(
            topBar = {
                AppBar(
                    hazeState,
                    currentFilter,
                    profile = profile,
                    isProfileDialogOpen = isProfileDialogOpen,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onAvatarClick = { onAvatarClick() })
            }) { padding ->
            LazyColumn(
                modifier = Modifier
                    .consumeWindowInsets(padding)
                    .hazeSource(hazeState),
                contentPadding = padding
            ) {
                item {
                    ArticlesSwitcherRow(
                        selectedFilter = currentFilter, onFilterSelected = { filter ->
                            onFilterSelected(filter)
                        })
                }

                when (val state = uiState) {
                    is ArticlesUIState.Loading -> {
                        item(key = "loading") {
                            ArticlesLoadingView(
                                modifier = Modifier
                                    .fillParentMaxHeight(0.8f)
                                    .fillMaxWidth()
                                    .animateItem()
                            )
                        }
                    }

                    is ArticlesUIState.Success -> {
                        items(state.articles, key = { it.id }) { article ->
                            ArticleItem(
                                article,
                                modifier = Modifier.animateItem(),
                                onClick = { article -> onArticleItemClick(article) })
                        }
                    }

                    is ArticlesUIState.Error -> {
                        item {
                            ArticlesErrorView(
                                message = state.message,
                                onRetry = { onRetry() },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }

                    is ArticlesUIState.Empty -> {
                        item { ArticlesEmptyView(modifier = Modifier.animateItem()) }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isProfileDialogOpen, enter = fadeIn(tween(300)), exit = fadeOut(tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isProfileDialogOpen = false }, contentAlignment = Alignment.Center
            ) {
                UserProfileDialog(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    profile = profile,
                    navigator = navigator,
                    onClose = { handleDialogClose() }
                )
            }
        }
    }
}

@Composable
fun ArticlesLoadingView(modifier: Modifier) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant, // The background of the loader
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round // Gives the ends of the loader a rounded, modern look
        )
    }
}

@Composable
fun ArticlesErrorView(
    modifier: Modifier = Modifier, message: String, onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun ArticlesEmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 64.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No articles found",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}