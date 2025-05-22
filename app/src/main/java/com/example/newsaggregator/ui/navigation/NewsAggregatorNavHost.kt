package com.example.newsaggregator.ui.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsaggregator.ui.screens.mainscreen.MainScreen
import com.example.newsaggregator.ui.screens.DetailScreen.NewsScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NewsAggregatorNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Main.route,
        modifier = modifier
    ) {
        composable(route = Screens.Main.route) {
            MainScreen(
                onNavigateToNews = { guid ->
                    navController.navigate(
                        Screens.NewsScreen.createRoute(guid)
                    )
                },
            )
        }
        composable(
            route = Screens.NewsScreen.route,
            arguments = Screens.NewsScreen.navArguments
        ) {backStackEntry ->
            val rawGuid = backStackEntry.arguments?.getString("guid") ?: ""
            val guid = URLDecoder.decode(rawGuid, StandardCharsets.UTF_8.toString())
            NewsScreen(
                guid = guid,
            )
        }
    }
}

