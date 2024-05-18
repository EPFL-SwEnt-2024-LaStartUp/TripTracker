package com.example.triptracker.view.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.WaitingScreen
import com.example.triptracker.view.home.DisplayItinerary
import com.example.triptracker.view.profile.subviews.ProfileCounts
import com.example.triptracker.view.profile.subviews.ProfileInfoView
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * This composable function displays the user profile of the user passed as a parameter. It is used
 * to access more detailed information about a user (as his/her saved and recorded trips)
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param userMail : the user profile to display.
 * @param userProfileViewModel : the view model to handle the user profile.
 */
@Composable
fun UserView(
    navigation: Navigation,
    profile: MutableUserProfile,
    userMail: String,
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    test: Boolean = false
) {
  var readyToDisplay by remember { mutableStateOf(false) }

  val loggedUser by remember { mutableStateOf(profile) }
  var displayedUser by remember { mutableStateOf(UserProfile("")) }

  userProfileViewModel.getUserProfile(userMail) { fetchedUser ->
    if (fetchedUser != null) {
      displayedUser = fetchedUser
      readyToDisplay = true
    }
  }

  homeViewModel.setSearchFilter(FilterType.USERNAME)
  homeViewModel.setSearchQuery(displayedUser.username)

  // Observe the filtered itinerary list from the ViewModel
  val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())

  if (test) {
    readyToDisplay = true
  }

  when (readyToDisplay) {
    false -> {
      WaitingScreen()
    }
    true -> {
      var areConnected by remember {
        mutableStateOf(loggedUser.userProfile.value.following.contains(displayedUser.mail))
      }

      val tripCount = homeViewModel.filteredItineraryList.value?.size

      Scaffold(
          topBar = {
            Row(
                modifier = Modifier.height(100.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start) {
                  Button(
                      onClick = { navigation.goBack() },
                      colors =
                          ButtonDefaults.buttonColors(
                              containerColor = Color.Transparent,
                              contentColor = MaterialTheme.colorScheme.onSurface),
                      modifier = Modifier.testTag("GoBackButton")) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                      }

                  Text(
                      text = displayedUser.username,
                      style =
                          TextStyle(
                              fontSize = 24.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(700),
                              color = MaterialTheme.colorScheme.onSurface,
                              textAlign = TextAlign.Start,
                              letterSpacing = 0.5.sp,
                          ),
                      modifier =
                          Modifier.weight(1f).height(37.dp).padding(5.dp).testTag("UsernameTitle"))
                }
          },
          bottomBar = { NavigationBar(navigation) },
          modifier = Modifier.fillMaxSize().testTag("UserView")) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxHeight().fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                  Column(
                      modifier =
                          Modifier.height((LocalConfiguration.current.screenHeightDp * 0.4).dp),
                      horizontalAlignment = Alignment.CenterHorizontally,
                      verticalArrangement = Arrangement.Center) {
                        // Composable function to display the user profile information
                        ProfileInfoView(
                            navigation = navigation, userProfile = displayedUser, editable = false)
                        // We display the button showing the follow status
                        Row(
                            modifier =
                                Modifier.fillMaxWidth()
                                    .padding(
                                        (LocalConfiguration.current.screenHeightDp * 0.033f).dp),
                            horizontalArrangement = Arrangement.Center) {
                              Button(
                                  onClick = {
                                    if (areConnected) {
                                      userProfileViewModel.removeFollowing(
                                          loggedUser, displayedUser)
                                    } else {
                                      userProfileViewModel.addFollowing(loggedUser, displayedUser)
                                    }
                                    areConnected = !areConnected
                                  },
                                  colors =
                                      if (areConnected) {
                                        ButtonDefaults.buttonColors(
                                            containerColor = md_theme_orange,
                                            contentColor = md_theme_light_onPrimary)
                                      } else {
                                        ButtonDefaults.buttonColors(
                                            containerColor = md_theme_grey,
                                            contentColor = md_theme_light_onPrimary)
                                      },
                                  modifier =
                                      Modifier.height(40.dp)
                                          .fillMaxWidth()
                                          .testTag("FollowingButton"),
                              ) {
                                Text(
                                    text =
                                        if (areConnected) {
                                          "Following"
                                        } else if (displayedUser.following.contains(
                                            loggedUser.userProfile.value.mail)) {
                                          "Follow Back"
                                        } else {
                                          "Follow"
                                        },
                                    style =
                                        TextStyle(
                                            fontSize = 12.sp,
                                            lineHeight = 12.sp,
                                            fontFamily = Montserrat,
                                            fontWeight = FontWeight(500),
                                            color = MaterialTheme.colorScheme.surface,
                                            textAlign = TextAlign.Center,
                                            letterSpacing = 0.5.sp))
                              }
                            }
                        // Display the number of trips, followers and following
                        ProfileCounts(
                            navigation = navigation,
                            profile = displayedUser,
                            tripsCount = tripCount ?: 0,
                            currentUserProfile = false)
                      }
                  Column(
                      modifier = Modifier.fillMaxHeight(),
                      horizontalAlignment = Alignment.CenterHorizontally,
                      verticalArrangement = Arrangement.Center) {
                        when (filteredList) {
                          // If the displayed user doesn't have any itineraries we display a message
                          emptyList<Itinerary>() -> {
                            // Display a message when the list is empty
                            Box(
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                                contentAlignment = Alignment.Center) {
                                  Text(
                                      text =
                                          "${displayedUser.name} ${displayedUser.surname} does not have any trips yet",
                                      modifier =
                                          Modifier.padding(30.dp)
                                              .align(Alignment.Center)
                                              .testTag("NoTripsText"),
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
                                    Modifier.fillMaxWidth().fillMaxHeight().testTag("MyTripsList"),
                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                                state = listState) {
                                  items(filteredList) { itinerary ->
                                    Log.d("ItineraryToDisplay", "Displaying itinerary: $itinerary")
                                    DisplayItinerary(
                                        itinerary = itinerary,
                                        onClick = {
                                          navigation.navigateTo(Route.MAPS, itinerary.id)
                                        },
                                        test = test,
                                    )
                                  }
                                }
                          }
                        }
                      }
                }
          }
    }
  }
}
