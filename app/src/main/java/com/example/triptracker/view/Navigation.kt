package com.example.triptracker.view

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/** Destinations used in the app. */
object Route {
  /** Login route. */
  const val LOGIN = "login"

  /** Profile routes */
  const val FAVORITES = "favorites"
  const val FRIENDS = "friends"
  const val MYTRIPS = "mytrips"
  const val SETTINGS = "settings"

  const val ITINERARY_PREVIEW = "itinerary_preview"

  /** Top level routes at the bottom of the screen */
  const val HOME = "home"
  const val MAPS = "maps"
  const val RECORD = "record"
  const val PROFILE = "profile"
}

/** Models of the top level destinations for the bottom navigation bar. */
data class TopLevelDestination(val route: String, val icon: ImageVector, val textId: String)

/** Destinations that are displayed at the bottom of the screen. */
private val TOP_LEVEL_DESTINATIONS =
    listOf(
        // TopLevelDestination(Route.LOGIN, Icons.Default.List, "Login"),
        TopLevelDestination(Route.HOME, Icons.Outlined.Home, "Home"),
        TopLevelDestination(Route.MAPS, Icons.Outlined.Place, "Maps"),
        TopLevelDestination(Route.RECORD, Icons.Outlined.RadioButtonChecked, "Record"),
        TopLevelDestination(Route.PROFILE, Icons.Outlined.Person, "Profile"),
    )

/**
 * Navigation that handles the navigation in the app. Allows to go back to the previous screen in a
 * non blocking way. Allows to navigate to a specific TopLevelDestination.
 */
class Navigation(val navController: NavHostController) {

  /** Current destination, helpful notably for the navigation bar */
  private var currentDestination: TopLevelDestination = getStartingDestination()

  fun navigateTo(destination: TopLevelDestination) {
    navController.navigate(destination.route) {
      currentDestination = destination
      navController.currentBackStackEntry?.arguments?.putString("id", "")
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

  fun navigateTo(route: String, id: String) {
    Log.d("Navigation", route + id)
    navController.navigate("MAPS?id=$id") {
      currentDestination = getTopLevelDestinations().find { it.route == "maps" }!!

      navController.currentBackStackEntry?.arguments?.putString("id", id)
      // Pop up to the start destination of the graph to
      // avoid building up a large stack of destinations
      // on the back stack as users select items
      popUpTo(navController.graph.findStartDestination().id) { saveState = false }
      // Avoid multiple copies of the same destination when
      // reselecting the same item
      //      launchSingleTop = true
      // Restore state when reselecting a previously selected item
      //      restoreState = true
    }
  }

  fun goBack() {
    navController.popBackStack()
  }

  /** Getter function to access a copy of the attribute TOP_LEVEL_DESTINATIONS */
  fun getTopLevelDestinations(): List<TopLevelDestination> {
    return TOP_LEVEL_DESTINATIONS.toMutableList()
  }

  /** Retrieve the app starting destination once logged in */
  fun getStartingDestination(): TopLevelDestination {
    return TOP_LEVEL_DESTINATIONS[0]
  }

  /** Retrieve the current destination once logged in */
  fun getCurrentDestination(): TopLevelDestination {
    return currentDestination
  }
}
