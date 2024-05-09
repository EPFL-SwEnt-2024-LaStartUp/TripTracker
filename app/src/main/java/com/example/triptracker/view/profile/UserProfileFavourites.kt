package com.example.triptracker.view.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel

/**
 * UserProfileFavourite is a Composable function that displays the user's favourite trips.
 *
 * @param homeViewModel ViewModel for the home screen.
 * @param navigation Navigation object for navigating between screens.
 * @param test Boolean flag for testing.
 * @param userProfile MutableUserProfile object for the user's profile.
 */
@Composable
fun UserProfileFavourite(
    homeViewModel: HomeViewModel = viewModel(),
    navigation: Navigation,
    test: Boolean = false,
    userProfile: MutableUserProfile
) {
  UserProfileScreen(
      homeViewModel = homeViewModel,
      navigation = navigation,
      test = test,
      userProfile = userProfile,
      filterType = FilterType.FAVORTIES,
      screenTag = "UserProfileFavouriteScreen",
      noDataText =
          "You do not have any favourite trips yet. Add some trips to your favourites to see them here.",
      titleText = "Favourites")
}
