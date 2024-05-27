package com.example.triptracker.view.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.network.Connection
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.home.DisplayItinerary
import com.example.triptracker.view.map.StartScreen
import com.example.triptracker.view.profile.subviews.ScaffoldTopBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel

/**
 * UserProfileMyTrips is a Composable function that displays the user's trips.
 *
 * @param connection Connection object for checking the device's internet connection.
 * @param homeViewModel ViewModel for the home screen.
 * @param navigation Navigation object for navigating between screens.
 * @param test Boolean flag for testing.
 * @param userProfile MutableUserProfile object for the user's profile.
 * @param filterType FilterType object for filtering the user's trips.
 * @param screenTag String object for the screen tag.
 * @param noDataText String object for the no data text.
 * @param titleText String object for the title text.
 */
@Composable
fun UserProfileScreen(
    connection: Connection = Connection(),
    homeViewModel: HomeViewModel = viewModel(),
    navigation: Navigation,
    test: Boolean = false,
    userProfile: MutableUserProfile,
    filterType: FilterType,
    screenTag: String,
    noDataText: String,
    titleText: String
) {
  homeViewModel.setSearchFilter(filterType)
  if (filterType == FilterType.FAVOURITES) {
    homeViewModel.setSearchQuery(userProfile.userProfile.value.mail)
  } else {
    homeViewModel.setSearchQuery(userProfile.userProfile.value.username)
  }

  val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())

  /* The itinerary to display the information of when offline */
  var itineraryToDisplay: Itinerary? by remember { mutableStateOf(null) }

  Scaffold(
      topBar = {
        if (itineraryToDisplay == null) {
          ScaffoldTopBar(navigation = navigation, label = titleText)
        }
      },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.testTag(screenTag)) { innerPadding ->
        Box {
          if (itineraryToDisplay != null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
              StartScreen(
                  itinerary = itineraryToDisplay!!,
                  onClick = { itineraryToDisplay = null },
                  userProfile = userProfile,
                  homeViewModel = homeViewModel,
                  offline = true)
            }
          } else {

            when (filteredList) {
              emptyList<Itinerary>() -> {
                Box(modifier = Modifier.fillMaxWidth().padding(innerPadding)) {
                  Text(
                      text = noDataText,
                      modifier =
                          Modifier.padding(30.dp).align(Alignment.TopCenter).testTag("NoDataText"),
                      fontSize = 16.sp,
                      fontFamily = Montserrat,
                      fontWeight = FontWeight.SemiBold,
                      color = md_theme_grey)
                }
              }
              else -> {
                val listState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).testTag("DataList"),
                    contentPadding = PaddingValues(16.dp),
                    state = listState) {
                      items(filteredList) { itinerary ->
                        if (itinerary == filteredList.first())
                            Spacer(modifier = Modifier.height(10.dp))
                        val onClick =
                            if (!connection.isDeviceConnectedToInternet()) {
                              { itineraryToDisplay = itinerary }
                            } else {
                              { navigation.navigateTo(Route.MAPS, itinerary.id) }
                            }
                        Log.d("ItineraryToDisplay", "Displaying itinerary: $itinerary")
                        DisplayItinerary(
                            itinerary = itinerary,
                            onClick = onClick,
                            canBeDeleted = canBeDeleted(filterType),
                            navigation = navigation,
                            onDelete = {
                                homeViewModel.deleteItinerary(itinerary.id) {
                                    homeViewModel.setSearchFilter(filterType)
                                    homeViewModel.setSearchQuery(userProfile.userProfile.value.username)
                                }
                            })
                      }
                    }
              }
            }
          }
        }
      }
}

/**
 * This function checks if the filter type can be deleted.
 *
 * @param filterType FilterType object for filtering the user's trips.
 */
private fun canBeDeleted(filterType: FilterType): Boolean {
  return filterType == FilterType.USERNAME
}
