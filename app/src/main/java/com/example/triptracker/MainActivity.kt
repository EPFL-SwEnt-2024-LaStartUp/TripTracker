package com.example.triptracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.triptracker.authentication.GoogleAuthenticator
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.ProvideUserProfile
import com.example.triptracker.navigation.LaunchPermissionRequest
import com.example.triptracker.view.LoginScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.view.map.RecordScreen
import com.example.triptracker.view.profile.UserProfileEditScreen
import com.example.triptracker.view.profile.UserProfileFavourite
import com.example.triptracker.view.profile.UserProfileFollowers
import com.example.triptracker.view.profile.UserProfileFollowing
import com.example.triptracker.view.profile.UserProfileFriends
import com.example.triptracker.view.profile.UserProfileMyTrips
import com.example.triptracker.view.profile.UserProfileOverview
import com.example.triptracker.view.profile.UserProfileSettings
import com.example.triptracker.view.theme.TripTrackerTheme
import com.example.triptracker.viewmodel.UserProfileViewModel

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

      // Get the last signed in account
      val lastSignIn = GoogleAuthenticator().getSignedInAccount(applicationContext())

      // Create an empty profile
      val profile = MutableUserProfile()

      if (lastSignIn != null) {
        // Fetch the user profile from the DB
        UserProfileViewModel().getUserProfile(lastSignIn.email.toString()) {
          if (it != null) {
            profile.userProfile.value = it
          }
        }
      }
      // Create ambient profile
      ProvideUserProfile(userProfileState = profile) {

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
              composable(Route.LOGIN) { LoginScreen(navigation, profile) }
              composable(Route.HOME) { HomeScreen(navigation, profile) }
              //            composable(Route.MAPS) { MapOverview(context = context, navigation =
              // navigation, selectedId = "") }
              composable(
                  "MAPS?id={id}", arguments = listOf(navArgument("id") { defaultValue = "" })) {
                      backStackEntry ->
                    MapOverview(
                        context = context,
                        navigation = navigation,
                        selectedId = backStackEntry.arguments?.getString("id") ?: "",
                        userProfile = profile)
                  }

              composable(Route.RECORD) { RecordScreen(context, navigation) }
              composable(Route.PROFILE) {
                UserProfileOverview(navigation = navigation, profile = profile)
              }
              composable(Route.FRIENDS) { UserProfileFriends(navigation = navigation) }
              composable(Route.FOLLOWERS) { UserProfileFollowers(navigation = navigation) }
              composable(Route.FOLLOWING) { UserProfileFollowing(navigation = navigation) }

              // add argument to the composable (username)
              composable(Route.MYTRIPS) {
                UserProfileMyTrips(
                    navigation = navigation,
                    userProfile = profile,
                )
              }

              composable(Route.FAVORITES) {
                UserProfileFavourite(navigation = navigation, userProfile = profile)
              }
              composable(Route.EDIT) {
                UserProfileEditScreen(navigation = navigation, profile = profile)
              }
              composable(Route.SETTINGS) { UserProfileSettings(navigation) }
            }
          }
        }
      }
    }
  }
}
