package com.example.triptracker.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object Route {
    const val LOGIN = "login"
    const val UNDEFINED = "undefined" // TODO remove this once other views are added
}

data class TopLevelDestination(val route: String, val icon: ImageVector, val textId: String)

class Navigation(val navController: NavHostController) {
    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun goBack() {
        navController.popBackStack()
    }
}

val TOP_LEVEL_DESTINATIONS =
    listOf(
        TopLevelDestination(Route.LOGIN, Icons.Default.List, "Login"),
    )
