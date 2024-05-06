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
import com.example.triptracker.model.profile.Relationship
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.viewmodel.UserProfileViewModel
import com.example.triptracker.viewmodel.loggedUser

/** This composable function displays the user's following list.
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param userProfileViewModel : the view model to handle the user profile.
 */
@Composable
fun UserProfileFollowing(
    navigation: Navigation,
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
) {
  val userMail: String = loggedUser.email ?: ""
  var userProfile by remember { mutableStateOf(UserProfile("")) }
  var readyToDisplay by remember { mutableStateOf(false) }
  var isSearchActive by remember { mutableStateOf(false) }

  // val list = viewModel.userProfileList.value
  userProfileViewModel.getUserProfile(userMail) { profile ->
    if (profile != null) {
      userProfile = profile
      readyToDisplay = true
    }
  }

  when (readyToDisplay) {
    false -> {
      // Display a loading screen while the user profile is being fetched
      Text("Loading...")
    }
    true -> {
      var followingList: List<UserProfile> by remember { mutableStateOf(listOf<UserProfile>()) }
      userProfile.following.forEach { following ->
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
                      text = "Following",
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
                          Modifier.width(250.dp)
                              .height(37.dp)
                              .padding(5.dp)
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
                  viewModel = userProfileViewModel,
                  userProfile = userProfile,
                  relationship = Relationship.FOLLOWING,
                  friendList = filteredList)
            }
          }
    }
  }
}
