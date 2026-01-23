package com.jarvis394.geekr.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jarvis394.geekr.ui.articles.ArticlesScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "articles") {
        composable("articles") {
            ArticlesScreen()
        }
    }
}