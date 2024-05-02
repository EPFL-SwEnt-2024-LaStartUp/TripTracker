package com.example.triptracker.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.viewmodel.UserProfileViewModel

/** This composable function displays the user's following list. */
@Composable
fun UserProfileFollowers(
    navigation: Navigation,
    viewModel: UserProfileViewModel = UserProfileViewModel(),
) {
    var userProfile by remember { mutableStateOf(UserProfile("")) }

    val filteredList by viewModel.filteredUserProfileList.observeAsState(initial = emptyList())
    var isSearchActive by remember { mutableStateOf(false) }
    val isNoResultFound =
        remember(filteredList, isSearchActive) {
            isSearchActive && filteredList.isEmpty() && viewModel.searchQuery.value!!.isNotEmpty()
        }

    // val list = viewModel.userProfileList.value
    val mockUser = viewModel.getUserProfile("barghornjeremy@gmail.com"
    ) { itin ->
        if (itin != null) {
            userProfile = itin
        }
    }

  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.height(100.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
              Column(modifier = Modifier.padding(5.dp).height(60.dp).width(100.dp)) {
                // Button to navigate back to the user profile
                Button(
                    onClick = { navigation.goBack() },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent, contentColor = md_theme_light_dark),
                    modifier = Modifier.testTag("GoBackButton")) {
                      Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
              }
              Column(
                  modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
              ) {
                Text(
                    text = "Followers",
                    style =
                        TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(700),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp,
                        ),
                    modifier =
                        Modifier.width(250.dp)
                            .height(37.dp)
                            .padding(5.dp)
                            .testTag("FollowersTitle"))
                Box(modifier = Modifier.size(60.dp))
              }
            }
      },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.fillMaxSize().testTag("FollowersScreen")) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).testTag("FollowersList")) {
            FriendSearchBar(viewModel = viewModel, onSearchActivated = { isActive -> isSearchActive = isActive })
            // Display the list of following
            FriendListView(viewModel = viewModel, userProfile = userProfile, relationship = Relationship.FOLLOWER, friendList = filteredList)
        }
      }
}

//// TODO: remove this preview
// @Preview(showBackground = true)
// @Composable
// fun UserProfileFollowersPreview() {
//    val viewModel = UserProfileViewModel()
//
//    //val list = viewModel.userProfileList.value
//    //val profile = viewModel.getUserProfile("cleorenaud38@gmail.com")
//
//    val navController = rememberNavController()
//    val navigation = remember(navController) { Navigation(navController) }
//
//    val mockUser1 = UserProfile(
//        mail = "cleorenaud38@gmail.com",
//        name = "Cleo",
//        surname = "Renaud",
//        birthdate = "08-February-2004",
//        username = "Cleoooo",
//        profileImageUrl =
// "https://hips.hearstapps.com/hmg-prod/images/portrait-of-a-red-fluffy-cat-with-big-eyes-in-royalty-free-image-1701455126.jpg",
//        followers = listOf("schifferlitheo@gmail.com", "barghornjeremy@gmail.com"),
//        following = emptyList()
//    )
//
//    UserProfileFollowers(navigation, viewModel, mockUser1)
// }
