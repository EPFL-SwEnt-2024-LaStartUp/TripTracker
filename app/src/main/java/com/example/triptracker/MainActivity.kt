package com.example.triptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.navigation.LaunchPermissionRequest
import com.example.triptracker.view.HomeScreen
import com.example.triptracker.view.LoginScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.map.MapOverviewPreview
import com.example.triptracker.view.map.RecordScreenPreview
import com.example.triptracker.view.theme.TripTrackerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  // Private boolean that tracks the logging status
  private var isLoggedIn = mutableStateOf(false)

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

          LaunchPermissionRequest(context = this)

          // Start a coroutine to continuously check the isLoggedIn boolean
          LaunchedEffect(Unit) {
            while (true) {
              isLoggedIn.value = navigation.getIsLoggedIn()
              delay(1000)
            }
          }

          /** If logged in, display the app content with the bottom navigation bar */
          if (isLoggedIn.value) {
            Scaffold(bottomBar = { NavigationBar(navigation) }) { innerPadding ->
              // List of destinations for in app navigation
              NavHost(
                  navController = navController,
                  startDestination = Route.HOME,
                  Modifier.padding(innerPadding)) {
                    composable(Route.HOME) { HomeScreen(navigation) }
                    composable(Route.MAPS) { MapOverviewPreview() }
                    composable(Route.RECORD) { RecordScreenPreview() }
                    composable(Route.PROFILE) {
                      // TODO: Call the profile composable
                    }
                  }
            }
          }

          /** If not logged in, display the logging screen without the bottom navigation bar */
          else {
            LoginScreen(navigation)
          }
        }
      }
    }
  }
}
