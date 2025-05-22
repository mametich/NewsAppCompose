package com.example.newsaggregator.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screens(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Main : Screens(route = "Main")

    data object NewsScreen : Screens(
        route = "newsScreen/{guid}",
        navArguments = listOf(navArgument("guid") { type = NavType.StringType })
    ) {
        fun createRoute(guid: String): String {
            return "newsScreen/" + URLEncoder.encode(guid, StandardCharsets.UTF_8.toString())
        }
    }
}