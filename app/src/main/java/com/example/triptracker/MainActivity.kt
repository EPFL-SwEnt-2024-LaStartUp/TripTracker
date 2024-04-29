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
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.navigation.LaunchPermissionRequest
import com.example.triptracker.view.Itinerary
import com.example.triptracker.view.LoginScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.map.DEFAULT_LOCATION
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.view.map.RecordScreen
import com.example.triptracker.view.profile.UserProfileOverview
import com.example.triptracker.view.theme.TripTrackerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.google.android.gms.maps.model.LatLng

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
          val userRepo = UserProfileRepository()
          val itineraryRepo = ItineraryRepository()
          val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
          val theoProfile =
              UserProfile(
                  "schifferlitheo@gmail.com",
                  "Theo",
                  "Schifferli",
                  LocalDate.of(1946, 10, 15).format(formatter),
                  "Tete la malice",
                  "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcS5n-VN2sv2jYgbMF3kVQWkYZQtdlQzje7_-9SYrgFe6w6gUQmL",
                  listOf(),
                  listOf())
          val loloProfile =
              UserProfile(
                  "misentaloic@gmail.com",
                  "Loic",
                  "Misenta",
                  LocalDate.of(1936, 12, 10).format(formatter),
                  "Lomimi",
                  "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRrfhlD13rGYJRmde04FRLNn2AT3uApwbvOcEfa5pAdMkQA3q8w",
                  listOf(),
                  listOf())
          val gehreProfile =
              UserProfile(
                  "barghornjeremy@gmail.com",
                  "Jeremy",
                  "Barghorn",
                  LocalDate.of(1939, 4, 2).format(formatter),
                  "GEHREMY",
                  "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQKS6BRrbH2Pj2QZeMJX_EVsnQcO89myNj1cxHeh2KRLMiamzmL",
                  listOf(),
                  listOf())
          val cleoProfile =
              UserProfile(
                  "cleorenaud38@gmail.com",
                  "Cleo",
                  "Renaud",
                  LocalDate.of(2004, 2, 8).format(formatter),
                  "Cleoooo",
                  "https://hips.hearstapps.com/hmg-prod/images/portrait-of-a-red-fluffy-cat-with-big-eyes-in-royalty-free-image-1701455126.jpg",
                  listOf(),
                  listOf())
          val leopoldProfile =
              UserProfile(
                  "leopold.galhaud@gmail.com",
                  "Leopold",
                  "Galhaud",
                  LocalDate.of(2017, 12, 20).format(formatter),
                  "LeopoldinhoDoBrazil",
                  "https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcRviVquGNsci2DsyGkO0Wf8vlR9m_L0mT2jskinyr7YD6L3CKw_kO6Vdv0D7gFmmFena5SNPDdB0J9R6x8",
                  listOf(),
                  listOf())
          val polProfile =
              UserProfile(
                  "polfuentescam@gmail.com",
                  "Pol",
                  "Fuentes",
                  LocalDate.of(1922, 9, 27).format(formatter),
                  "Polfuegoooo",
                  "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Maradona-Mundial_86_con_la_copa.JPG/1200px-Maradona-Mundial_86_con_la_copa.JPG",
                  listOf(),
                  listOf())
          val jeremyProfile =
              UserProfile(
                  "chaverotjrmy7@gmail.com",
                  "Jeremy",
                  "Chaverot",
                  LocalDate.of(1948, 11, 17).format(formatter),
                  "JeremyyyTheTigerKing",
                  "https://cloudfront-us-east-1.images.arcpublishing.com/advancelocal/6AREHOEFLBGB3EHADZJ4AQCD4A.jpg",
                  listOf(),
                  listOf())

          val itinerary =
              Itinerary(
                  "7",
                  "Theo's trip",
                  "schifferlitheo@gmail.com",
                  Location(0.0, 0.0, "Theo's house"),
                  0,
                  "2022-12-31T23:59:59",
                  "2023-01-01T00:00:00",
                  listOf(),
                  "Theo's trip",
                  listOf())

          // userRepo.addNewUserProfile(theoProfile)

          // List of destinations for in app navigation
          NavHost(
              navController = navController,
              startDestination = Route.LOGIN,
          ) {
            composable(Route.LOGIN) { LoginScreen(navigation) }
            composable(Route.HOME) { HomeScreen(navigation) }
            composable(Route.MAPS) { MapOverview(context = context, navigation = navigation) }
            composable(Route.MAPS + "/{lat}" + "/{lon}") { backStackEntry ->
              val lat =
                  backStackEntry.arguments?.getString("lat") ?: DEFAULT_LOCATION.latitude.toString()
              val lon =
                  backStackEntry.arguments?.getString("lon")
                      ?: DEFAULT_LOCATION.longitude.toString()
              MapOverview(
                  context = context,
                  navigation = navigation,
                  startLocation = LatLng(lat.toDouble(), lon.toDouble()))
            }

            composable(Route.RECORD) { RecordScreen(context, navigation) }
            composable(Route.PROFILE) { UserProfileOverview(navigation = navigation) }
          }
        }
      }
    }
  }
}
