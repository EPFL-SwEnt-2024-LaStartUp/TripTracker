package com.example.triptracker.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.Relationship
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.profile.subviews.FriendListView
import com.example.triptracker.view.profile.subviews.FriendSearchBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * This composable function displays the user's following list.
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param userProfileViewModel : the view model to handle the user profile.
 */
@Composable
fun UserProfileFollowing(
    navigation: Navigation,
    profile: MutableUserProfile,
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
) {
  val userProfile by remember { mutableStateOf(profile) }

  var isSearchActive by remember { mutableStateOf(false) }

  var followingList: List<UserProfile> by remember { mutableStateOf(listOf<UserProfile>()) }
  userProfile.userProfile.value.following.forEach { following ->
    userProfileViewModel.getUserProfile(following) { profile ->
      if (profile != null) {
        // we check that the profile is not already in the following list
        if (!followingList.contains(profile)) {
          followingList += profile
        }
      }
    }
  }

  userProfileViewModel.setListToFilter(followingList)
  var filteredList =
      userProfileViewModel.filteredUserProfileList.observeAsState(initial = emptyList())

  Scaffold(
      topBar = {
        Row(
            modifier =
                Modifier.height((LocalConfiguration.current.screenHeightDp * 0.075).dp)
                    .fillMaxWidth(),
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
                  text = "Following",
                  style =
                      TextStyle(
                          fontSize = (LocalConfiguration.current.screenHeightDp * 0.03f).sp,
                          lineHeight = (LocalConfiguration.current.screenHeightDp * 0.016f).sp,
                          fontFamily = Montserrat,
                          fontWeight = FontWeight(700),
                          color = md_theme_light_dark,
                          textAlign = TextAlign.Left,
                          letterSpacing = (LocalConfiguration.current.screenHeightDp * 0.0005f).sp,
                      ),
                  modifier =
                      Modifier.width((LocalConfiguration.current.screenHeightDp * 0.67f).dp)
                          .wrapContentHeight()
                          .testTag("FollowingTitle"))
            }
      },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.fillMaxSize().testTag("FollowingScreen")) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).testTag("FollowingList")) {
          FriendSearchBar(
              viewModel = userProfileViewModel,
              onSearchActivated = { isActive -> isSearchActive = isActive })
          // Display the list of following
          FriendListView(
              navigation = navigation,
              viewModel = userProfileViewModel,
              profile = profile,
              relationship = Relationship.FOLLOWING,
              friendList = filteredList)
        }
      }
}
