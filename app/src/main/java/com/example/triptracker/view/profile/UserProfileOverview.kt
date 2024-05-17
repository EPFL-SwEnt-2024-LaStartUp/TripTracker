package com.example.triptracker.view.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.profile.subviews.ProfileButton
import com.example.triptracker.view.profile.subviews.ProfileCounts
import com.example.triptracker.view.profile.subviews.ProfileInfoView
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel

/* Visual preview without any logic */

/**
 * Composable displaying the profile overview featuring information about follower, following. 4
 * clickable buttons are available to access to the MyTrips, Favorites, Friends and settings views.
 *
 * @param userProfileViewModel: The view model of the UserProfile
 * @param navigation: The navigation of the app to switch between different views
 */
@Composable
fun UserProfileOverview(
    navigation: Navigation,
    profile: MutableUserProfile,
    userProfileViewModel: UserProfileViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {

  var isConnected by remember { mutableStateOf(userProfileViewModel.onConnectionRefresh()) }

  val myTripsList = homeViewModel.filteredItineraryList
  var myTripsCount = 0
  myTripsList.observeForever(Observer { list -> myTripsCount = list.size })

  val MAX_PROFILE_NAME_LENGTH = 15

  homeViewModel.setSearchFilter(FilterType.USERNAME)
  homeViewModel.setSearchQuery(
      profile.userProfile.value.username) // Filters the list of trips on user that created it
  var sizeUsername = (LocalConfiguration.current.screenHeightDp * 0.02f).sp
  if (profile.userProfile.value.username.length > MAX_PROFILE_NAME_LENGTH) {
    sizeUsername = (LocalConfiguration.current.screenHeightDp * 0.018f).sp
  }

  // Toggle the visibility of the "No internet connection" text every half second
  var blink by remember { mutableStateOf(true) }
  LaunchedEffect(isConnected) {
    if (!isConnected) {
      blink = false
    }
  }
  val color by
      animateColorAsState(
          targetValue = if (blink) Color.Black else md_theme_grey,
          animationSpec =
              infiniteRepeatable(
                  animation = tween(durationMillis = 1500), repeatMode = RepeatMode.Reverse),
          label = "")

  Scaffold(
      topBar = {},
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.fillMaxSize().testTag("ProfileOverview")) { innerPadding ->
        if (!isConnected) {
          // Display the "No internet connection" text and the refresh icon
          Box(
              modifier = Modifier.fillMaxSize().padding(innerPadding),
              contentAlignment = Alignment.TopCenter) {
                Row(
                    modifier = Modifier.padding(top = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                      Text(
                          text = "You are currently offline",
                          fontFamily = Montserrat,
                          fontWeight = FontWeight.Bold,
                          color = color,
                          fontSize = 15.sp,
                          modifier = Modifier.padding(end = 4.dp))
                      Icon(
                          imageVector = Icons.Default.Refresh,
                          contentDescription = "Refresh",
                          modifier =
                              Modifier.padding(end = 16.dp).size(20.dp).clickable {
                                isConnected = userProfileViewModel.onConnectionRefresh()
                              },
                          tint = color)
                    }
              }
        }
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxHeight().fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
              Column(
                  modifier = Modifier.height((LocalConfiguration.current.screenHeightDp * 0.45).dp),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center) {

                    // Profile picture and user information
                    ProfileInfoView(navigation, profile.userProfile.value)
                    // Number of trips, followers and following when implemented in the data classes
                    ProfileCounts(navigation, profile.userProfile.value, myTripsCount)
                    // Favourites, Friends, Settings and MyTrips tiles
                  }
              Column(
                  modifier = Modifier.height((LocalConfiguration.current.screenHeightDp * 0.55).dp),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center) {
                    Row(
                        modifier =
                            Modifier.padding(
                                bottom = (LocalConfiguration.current.screenWidthDp * 0.05f).dp),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                (LocalConfiguration.current.screenWidthDp * 0.05).dp)) {
                          ProfileButton(
                              label = "Favorites",
                              icon = Icons.Outlined.FavoriteBorder,
                              onClick = { navigation.navController.navigate(Route.FAVORITES) },
                              modifier = Modifier.testTag("FavoritesButton"))
                          ProfileButton(
                              label = "Friends",
                              icon = Icons.Outlined.People,
                              onClick = { navigation.navController.navigate(Route.FRIENDS) },
                              modifier = Modifier.testTag("FriendsButton"))
                        }
                    Row(
                        modifier =
                            Modifier.padding(
                                bottom = (LocalConfiguration.current.screenWidthDp * 0.05f).dp),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                (LocalConfiguration.current.screenWidthDp * 0.05).dp)) {
                          ProfileButton(
                              label = "MyTrips",
                              icon = Icons.Outlined.BookmarkBorder,
                              modifier = Modifier.testTag("MyTripsButton"),
                              onClick = { navigation.navController.navigate(Route.MYTRIPS) })

                          ProfileButton(
                              label = "Settings",
                              icon = Icons.Outlined.Settings,
                              onClick = { navigation.navController.navigate(Route.SETTINGS) },
                              modifier = Modifier.testTag("SettingsButton"))
                        }
                  }
            }
      }
}
