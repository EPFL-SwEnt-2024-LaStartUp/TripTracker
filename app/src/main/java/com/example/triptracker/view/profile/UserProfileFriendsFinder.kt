package com.example.triptracker.view.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.Relationship
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.profile.subviews.FriendListView
import com.example.triptracker.view.profile.subviews.FriendSearchBar
import com.example.triptracker.view.profile.subviews.ScaffoldTopBar
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * This composable function displays the friends search view
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param userProfileViewModel : the view model to handle the user profile.
 */
@Composable
fun UserProfileFriendsFinder(
    navigation: Navigation,
    profile: MutableUserProfile,
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
) {
  val userProfile by remember { mutableStateOf(profile) }

  var isSearchActive by remember { mutableStateOf(false) }

  val usersList by userProfileViewModel.userProfileList.observeAsState(initial = emptyList())

  userProfileViewModel.setListToFilter(usersList)
  val filteredList =
      userProfileViewModel.filteredUserProfileList.observeAsState(initial = emptyList())

  Scaffold(
      topBar = { ScaffoldTopBar(navigation = navigation, label = "Friends Finder") },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.fillMaxSize().testTag("FriendsFinderScreen")) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().testTag("FriendsList")) {
          FriendSearchBar(
              viewModel = userProfileViewModel,
              onSearchActivated = { isActive -> isSearchActive = isActive })
          FriendListView(
              navigation = navigation,
              viewModel = userProfileViewModel,
              profile = userProfile,
              relationship = Relationship.FRIENDS,
              friendList = filteredList)
        }
      }
}
