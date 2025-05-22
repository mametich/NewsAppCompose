package com.example.newsaggregator.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsaggregator.ui.navigation.NewsAggregatorNavHost
import com.example.newsaggregator.ui.navigation.Screens
import com.example.newsaggregator.ui.theme.NewsAggregatorTheme
import androidx.compose.runtime.getValue
import com.example.newsaggregator.ui.component.NewsAggregatorAppBar

@Composable
fun NewsAggregatorApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = navController.previousBackStackEntry != null

    val title = when (navBackStackEntry?.destination?.route?.substringBefore("/")) {
        Screens.Main.route -> "Главная"
        Screens.NewsScreen.route.substringBefore("/") -> "Новость"
        else -> "News Aggregator"
    }

    NewsAggregatorTheme {
        Scaffold(
            topBar = {
                NewsAggregatorAppBar(
                    title = title,
                    canNavigateBack = canNavigateBack,
                    navigateUp = {
                        navController.popBackStack()
                    }
                )
            }
        ) { innerPadding ->
            NewsAggregatorNavHost(
                navController = navController,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

