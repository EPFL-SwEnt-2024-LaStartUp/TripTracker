package com.example.triptracker.view.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.profile.AmbientUserProfile
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.home.DisplayItinerary
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel

/**
 * UserProfileMyTrips is a Composable function that displays the user's trips.
 *
 * @param homeViewModel ViewModel for the home screen.
 * @param navigation Navigation object for navigating between screens.
 * @param test Boolean flag for testing.
 */
@Composable
fun UserProfileMyTrips(
    homeViewModel: HomeViewModel = viewModel(),
    navigation: Navigation,
    test: Boolean = false,
    userProfile: MutableUserProfile
) {

  //val profile = AmbientUserProfile.current.userProfile.value
  // Set search filter and query in the ViewModel
  homeViewModel.setSearchFilter(FilterType.USERNAME)
  homeViewModel.setSearchQuery(userProfile.userProfile.value.username)

  // Observe the filtered itinerary list from the ViewModel
  val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())

  // Scaffold for the main layout
  Scaffold(
      topBar = {},
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.testTag("UserProfileMyTripsScreen")) { innerPadding ->
        Box {
          // Display different content based on the filtered list
          when (filteredList) {
            emptyList<Itinerary>() -> {
              // Display a message when the list is empty
              Box(modifier = Modifier.fillMaxWidth().padding(top = 100.dp)) {
                Text(
                    text = "You do not have any trips yet. Create a new trip to get started!",
                    modifier =
                        Modifier.padding(30.dp).align(Alignment.TopCenter).testTag("NoTripsText"),
                    fontSize = 16.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = md_theme_grey)
              }
            }
            else -> {
              // Display the list of itineraries when the list is not empty
              val listState = rememberLazyListState()
              LazyColumn(
                  modifier =
                      Modifier.fillMaxSize()
                          .padding(innerPadding)
                          .padding(top = 20.dp)
                          .testTag("MyTripsList"),
                  contentPadding = PaddingValues(16.dp),
                  state = listState) {
                    items(filteredList) { itinerary ->
                      if (itinerary == filteredList.first())
                          Spacer(modifier = Modifier.height(64.dp))
                      Log.d("ItineraryToDisplay", "Displaying itinerary: $itinerary")
                      DisplayItinerary(
                          itinerary = itinerary,
                          navigation = navigation,
                          onClick = { navigation.navigateTo(Route.MAPS, itinerary.id) },
                          test = test,
                          profile = userProfile,
                      )
                    }
                  }
            }
          }
          // Box for the back button and title
          Box(
              modifier =
                  Modifier.fillMaxWidth()
                      .height(100.dp)
                      .padding(horizontal = 16.dp)
                      .background(Color.White),
              contentAlignment = Alignment.Center) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(vertical = 10.dp)) {
                      // Back button
                      Icon(
                          imageVector = Icons.Default.ArrowBack,
                          contentDescription = "Back",
                          modifier =
                              Modifier.weight(1f)
                                  .clickable { navigation.goBack() }
                                  .align(Alignment.CenterVertically)
                                  .testTag("GoBackButton"),
                      )
                      // Title
                      Text(
                          text = "My Trips",
                          fontSize = 28.sp,
                          fontFamily = Montserrat,
                          fontWeight = FontWeight.SemiBold,
                          color = md_theme_light_dark,
                          modifier = Modifier.fillMaxWidth().weight(8f).testTag("MyTripsTitle"),
                          textAlign = TextAlign.Center)

                      Spacer(modifier = Modifier.weight(1f))
                    }
              }
        }
      }
}
