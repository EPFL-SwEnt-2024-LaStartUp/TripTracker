package com.example.triptracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.navigation.LaunchPermissionRequest
import com.example.triptracker.view.LoginScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.view.map.RecordScreen
import com.example.triptracker.view.profile.UserProfileOverview
import com.example.triptracker.view.theme.TripTrackerTheme

class MainActivity : ComponentActivity() {

  init {
    instance = this
  }

  /** Companion object to have a global application context */
  companion object {
    private var instance: MainActivity? = null

    fun applicationContext(): Context {
      return instance!!.applicationContext
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      // Set the trip tracker theme
      TripTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          // Instance of NavController
          val navController = rememberNavController()
          val navigation = remember(navController) { Navigation(navController) }
          val context: Context = applicationContext()

          LaunchPermissionRequest(context)

          // List of destinations for in app navigation
          NavHost(
              navController = navController,
              startDestination = Route.LOGIN,
          ) {
            composable(Route.LOGIN) { LoginScreen(navigation) }
            composable(Route.HOME) { HomeScreen(navigation) }
            composable(Route.MAPS) { MapOverview(context = context, navigation = navigation) }
            composable(Route.MAPS + "/{id}") { backStackEntry ->
              val id = backStackEntry.arguments?.getString("id") ?: ""

              MapOverview(context = context, navigation = navigation, selectedId = id)
            }

            composable(Route.RECORD) { RecordScreen(context, navigation) }
            composable(Route.PROFILE) { UserProfileOverview(navigation = navigation) }
          }
        }
      }
    }
  }
}
