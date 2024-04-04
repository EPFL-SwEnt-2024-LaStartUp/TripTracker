package com.example.triptracker.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/** Destinations used in the app. */
object Route {
  /** Login route. */
  const val LOGIN = "login"

  /** Top level routes at the bottom of the screen */
  const val HOME = "home"
  const val MAPS = "maps"
  const val RECORD = "record"
  const val PROFILE = "profile"

  const val UNDEFINED = "undefined" // TODO remove this once other views are added
}

/** Models of the top level destinations for the bottom navigation bar. */
data class TopLevelDestination(val route: String, val icon: ImageVector, val textId: String)

/**
 * Navigation that handles the navigation in the app. Allows to go back to the previous screen in a
 * non blocking way. Allows to navigate to a specific TopLevelDestination.
 */
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

/** Destinations that are displayed at the bottom of the screen. */
val TOP_LEVEL_DESTINATIONS =
    listOf(
        //TopLevelDestination(Route.LOGIN, Icons.Default.List, "Login"),
        TopLevelDestination(Route.HOME, Icons.Default.List, "Home"),
        TopLevelDestination(Route.MAPS, Icons.Default.List, "Maps"),
        TopLevelDestination(Route.RECORD, Icons.Default.List, "Record"),
        TopLevelDestination(Route.PROFILE, Icons.Default.List, "Profile"),
    )
