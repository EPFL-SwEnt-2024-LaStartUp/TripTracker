package com.example.triptracker.view

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.triptracker.R

/** Destinations used in the app. */
object Route {
  /** Login route. */
  const val LOGIN = "login"
  const val ITINERARY_PREVIEW = "itinerary_preview"

  /** Top level routes at the bottom of the screen */
  const val HOME = "home"
  const val MAPS = "maps"
  const val RECORD = "record"
  const val PROFILE = "profile"

  const val UNDEFINED = "undefined" // TODO remove this once other views are added
}

/** Models of the top level destinations for the bottom navigation bar. */
data class TopLevelDestination(val route: String, val icon: Int, val textId: String)

/** Destinations that are displayed at the bottom of the screen. */
private val TOP_LEVEL_DESTINATIONS =
    listOf(
        // TopLevelDestination(Route.LOGIN, Icons.Default.List, "Login"),
        TopLevelDestination(Route.HOME, R.drawable.icon_home, "Home"),
        TopLevelDestination(Route.MAPS, R.drawable.icon_map, "Maps"),
        TopLevelDestination(Route.RECORD, R.drawable.icon_record, "Record"),
        TopLevelDestination(Route.PROFILE, R.drawable.icon_profile, "Profile"),
    )

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

  // Getter function to access a copy of the attribute TOP_LEVEL_DESTINATIONS
  fun getTopLevelDestinations(): List<TopLevelDestination> {
    return TOP_LEVEL_DESTINATIONS.toMutableList()
  }
}
