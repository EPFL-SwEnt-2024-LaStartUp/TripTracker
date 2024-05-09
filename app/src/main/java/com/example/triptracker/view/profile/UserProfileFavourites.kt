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
 * UserProfileFavourite is a Composable function that displays the user's favourite trips.
 *
 * @param homeViewModel ViewModel for the home screen.
 * @param navigation Navigation object for navigating between screens.
 * @param test Boolean flag for testing.
 */
@Composable
fun UserProfileFavourite(
    homeViewModel: HomeViewModel = viewModel(),
    navigation: Navigation,
    test: Boolean = false,
    userProfile: MutableUserProfile
) {
  // Log for debugging
  Log.d("HomeScreen", "Rendering HomeScreen")

  // TODO Not implemented yet -> next sprint
  homeViewModel.setSearchFilter(FilterType.FAVORTIES) // later add the favorties filter
  homeViewModel.setSearchQuery(userProfile.userProfile.value.mail)
  val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())
  // Currently, the filtered list is empty as the feature is not implemented yet
  // val favoritesPaths = remember { userProfile.userProfile.value.favoritesPaths }

  // Scaffold for the main layout
  Scaffold(
      topBar = {},
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.testTag("UserProfileFavouriteScreen")) { innerPadding ->
        Box {
          // Display a message as the feature is not implemented yet

          when (filteredList) {
            emptyList<Itinerary>() -> {
              // Display a message when the list is empty
              Box(modifier = Modifier.fillMaxWidth().padding(top = 100.dp)) {
                Text(
                    text =
                        "You do not have any favourite trips yet. Add some trips to your favourites to see them here.",
                    modifier =
                        Modifier.padding(30.dp)
                            .align(Alignment.TopCenter)
                            .testTag("NoFavouritesText"),
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
                          .testTag("FavouritesList"),
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
                  modifier = Modifier.padding(vertical = 10.dp)) { // Row for the back button
                    // Back button
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier =
                            Modifier.weight(1f)
                                .testTag("FavouritesBackButton")
                                .clickable { navigation.goBack() }
                                .align(Alignment.CenterVertically),
                    )
                    // Title
                    Text(
                        text = "Favourites",
                        fontSize = 28.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = md_theme_light_dark,
                        modifier = Modifier.fillMaxWidth().weight(8f).testTag("FavouritesTitle"),
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.weight(1f))
                  }
            }
      }
}
