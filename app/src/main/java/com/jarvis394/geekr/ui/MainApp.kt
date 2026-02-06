package com.jarvis394.geekr.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.jarvis394.geekr.ui.article.articleScreenEntry
import com.jarvis394.geekr.ui.articles.ArticlesScreenKey
import com.jarvis394.geekr.ui.articles.articlesScreenEntry
import com.jarvis394.geekr.ui.composables.ScreenNavigator
import com.jarvis394.geekr.ui.profile.profileScreenEntry
import com.jarvis394.geekr.utils.sharedAxisXPopTransitionSpec
import com.jarvis394.geekr.utils.sharedAxisXTransitionSpec

interface MainAppScreenKey : NavKey

@Composable
fun MainApp() {
    @Suppress("UNCHECKED_CAST")
    val backStack = rememberNavBackStack(ArticlesScreenKey) as NavBackStack<MainAppScreenKey>
    val navigator = remember { ScreenNavigator(backStack) }

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = { sharedAxisXTransitionSpec() },
        popTransitionSpec = { sharedAxisXPopTransitionSpec() },
        predictivePopTransitionSpec = { sharedAxisXPopTransitionSpec() },
        entryProvider = entryProvider {
            articlesScreenEntry(navigator)
            articleScreenEntry(navigator)
            profileScreenEntry(navigator)
        })
}