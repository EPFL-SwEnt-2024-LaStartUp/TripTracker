package com.example.triptracker

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
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.view.HomeScreen
import com.example.triptracker.view.LoginScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.theme.TripTrackerTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    FirebaseApp.initializeApp(applicationContext)
    auth = FirebaseAuth.getInstance()
    val repository = ItineraryRepository()

    // add some Itinerary data by hand to test
    val itinerary_1 =
        Itinerary(
            "1",
            "My NYC Trip",
            "Lomimi",
            Location(0.0, 0.0, "NYC baby"),
            3504,
            "2024-03-25T13:51:50",
            "2024-03-25T17:31:10",
            listOf(Pin(60.4, 58.2, "Empire State Building", "Beautiful", "stupid-image-url.com")),
            "Many places seen")
    val itinerary_2 =
        Itinerary(
            "2",
            "My NYC Barathon",
            "La Schifferlance",
            Location(0.0, 0.0, "NYC baby"),
            1000,
            "2024-03-25T08:03:01",
            "2024-03-25T19:41:06",
            listOf(
                Pin(40.3, 30.2, "The Green Gauntlet", "Was soooo coool", "stupid-image-url.com"),
                Pin(-36.1, 43.4, "Raines Law Room", "Speakeasy ", "stupid-image-url.com"),
                Pin(-32.1, 40.4, "Forever 21", "Meh, but still cool", "stupid-image-url.com"),
                Pin(-32.1, 40.4, "The roadrunner", "Cute little bar", "stupid-image-url.com")),
            "Got a bit too wasted wasted")

    val itinerary_3 =
        Itinerary(
            "3",
            "My Favorite NYC clubs",
            "GEHREMY",
            Location(0.0, 0.0, "NYC baby"),
            75043,
            "2024-03-25T10:23:13",
            "2024-03-25T12:21:23",
            listOf(
                Pin(-32.1, 40.4, "Basement", "Cool underground club", "stupid-image-url.com"),
                Pin(
                    -32.1,
                    40.4,
                    "Nowadays",
                    "The non stop parties are wild",
                    "stupid-image-url.com")),
            "I'm tired")

    // repository.addNewItinerary(itinerary_1)
    // repository.addNewItinerary(itinerary_2)
    // repository.addNewItinerary(itinerary_3)

    setContent {
      // Set the trip tracker theme
      TripTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          // Instance of NavController
          val navController = rememberNavController()
          val navigation = remember(navController) { Navigation(navController) }

          // List of destinations for in app navigation
          NavHost(navController = navController, startDestination = Route.LOGIN) {
            composable(Route.LOGIN) {
              LoginScreen(navigation) // TODO change this once more screens are added
            }
            composable(Route.HOME) { HomeScreen(navigation) }
          }
        }
      }
    }
  }
}
