package com.example.triptracker.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.R
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.Relationship
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.profile.subviews.FriendListView
import com.example.triptracker.view.profile.subviews.FriendSearchBar
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * This composable function displays the user's following list.
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param userProfileViewModel : the view model to handle the user profile.
 */
@Composable
fun UserProfileFollowers(
    navigation: Navigation,
    profile: MutableUserProfile,
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
) {
  val userProfile by remember { mutableStateOf(profile) }

  var isSearchActive by remember { mutableStateOf(false) }

  var followersList: List<UserProfile> by remember { mutableStateOf(listOf<UserProfile>()) }
  userProfile.userProfile.value.followers.forEach { follower ->
    userProfileViewModel.getUserProfile(follower) { profile ->
      if (profile != null) {
        // we check that the profile is not already in the following list
        if (!followersList.contains(profile)) {
          followersList += profile
        }
      }
    }
  }

  userProfileViewModel.setListToFilter(followersList)
  var filteredList =
      userProfileViewModel.filteredUserProfileList.observeAsState(initial = emptyList())

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
                  text = "Followers",
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
                      Modifier.width(250.dp).height(37.dp).padding(5.dp).testTag("FollowersTitle"))
            }
      },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.fillMaxSize().testTag("FollowersScreen")) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).testTag("FollowersList")) {
          FriendSearchBar(
              viewModel = userProfileViewModel,
              onSearchActivated = { isActive -> isSearchActive = isActive })
          // Display the list of following
          FriendListView(
              navigation = navigation,
              viewModel = userProfileViewModel,
              profile = userProfile,
              relationship = Relationship.FOLLOWER,
              friendList = filteredList)
        }
      }
}
