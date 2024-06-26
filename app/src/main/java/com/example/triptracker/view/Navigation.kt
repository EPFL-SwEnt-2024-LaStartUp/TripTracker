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
import com.example.triptracker.model.network.Connection

/** Destinations used in the app. */
object Route {
  /** Login route. */
  const val LOGIN = "login"

  /** Profile routes */
  const val FAVORITES = "favorites"
  const val FRIENDS = "friends"
  const val MYTRIPS = "mytrips"
  const val SETTINGS = "settings"
  const val EDIT = "edit"
  const val FOLLOWING = "following"
  const val FOLLOWERS = "followers"
  const val USER = "user"

  const val ITINERARY_PREVIEW = "itinerary_preview"

  /** Top level routes at the bottom of the screen */
  const val HOME = "home"
  const val MAPS = "maps"
  const val RECORD = "record"
  const val PROFILE = "profile"

  /** Offline route */
  const val OFFLINE = "offline"
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
 * non blocking way. Allows to navigate to a specific TopLevelDestination. Connection object to
 * check if the device is connected to the internet. Relevant here since the connection influences
 * the navigation behavior.
 */
class Navigation(
    val navController: NavHostController,
    private val connection: Connection = Connection()
) {

  /** Current destination, helpful notably for the navigation bar */
  private var currentDestination: TopLevelDestination = getStartingDestination()

  /** Next destination, used when no internet connection */
  private var nextDestination: TopLevelDestination? = null

  /** Next id, used when no internet connection */
  private var nextId: String? = null

  fun navigateTo(destination: TopLevelDestination, isRetry: Boolean = false) {
    // Check if the destination is the profile screen, which is the only destination allowed in
    // offline mode
    val goToProfile = destination.route == Route.PROFILE
    if (connection.isDeviceConnectedToInternet() || goToProfile) {
      if (isRetry || goToProfile) {
        // Reset next destination
        nextDestination = null
        goBack()
      }

      navController.navigate(destination.route) {
        currentDestination = destination
        // reset the id when navigating normally so that the state is not saved
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
    } else {
      Log.d("Navigation", "No internet connection")
      if (!isRetry) {
        nextDestination = destination
        navController.navigate(Route.OFFLINE)
      }
    }
  }

  // Only use this when navigating to the maps screen
  fun navigateTo(route: String, id: String, isRetry: Boolean = false) {
    val destination = getTopLevelDestinations().find { it.route == "maps" }!!
    Log.d("Navigation", route + id)
    if (connection.isDeviceConnectedToInternet()) {
      if (isRetry) {
        Log.d("Navigation", "Successfully reconnected to the internet")
        // Reset next destination
        nextDestination = null
        nextId = null
        goBack()
      }
      navController.navigate("MAPS?id=$id") {
        currentDestination = destination
        // set the id when navigating with the map
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
    } else {
      Log.d("Navigation", "No internet connection")
      if (!isRetry) {
        nextDestination = destination
        nextId = id
        navController.navigate(Route.OFFLINE)
      }
    }
  }

  /** Retry the navigation to the next destination when not connected to internet */
  fun retryNavigateTo() {
    if (nextDestination != null) {
      if (nextId != null) {
        navigateTo(nextDestination!!.route, nextId!!, isRetry = true)
      } else {
        navigateTo(nextDestination!!, isRetry = true)
      }
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
