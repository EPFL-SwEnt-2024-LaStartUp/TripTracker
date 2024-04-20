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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.R
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.viewmodel.UserProfileViewModel
import java.util.Date

/** This composable function displays the user's following list. */
@Composable
fun UserProfileFollowers(
    navigation: Navigation,
    userProfileViewModel: UserProfileViewModel,
    userProfile: UserProfile
) {
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
                    // modifier = Modifier.weight(1f)
                    // .padding(horizontal = 16.dp)
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
          // Display the list of following
          FriendListView(userProfileViewModel, userProfile,false)
        }
      }
}

// TODO: Remove this preview
@Preview
@Composable
fun UserProfileFollowersPreview() {
     val mockUser1 = UserProfile(
        "1",
        "Alice",
        "Smith",
        Date(2021, 1, 1),
        "AliceS",
        "stupid-image-url.com",
        emptyList(),
        emptyList()
    )

     val mockUser2 = UserProfile(
        "2",
        "Bob",
        "Johnson",
        Date(2021, 1, 1),
        "BobJ",
        null,
        emptyList(),
        emptyList()
    )

     val mockUser3 = UserProfile(
        "3",
        "Charlie",
        "Brown",
        Date(2021, 1, 1),
        "CharlieB",
        null,
        listOf(mockUser1, mockUser2),
        listOf(mockUser1, mockUser2)
    )

     val mockUsers =
        listOf(
            mockUser1,
            mockUser2,
            mockUser3
        )
    val dummyViewModel =
      UserProfileViewModel(UserProfileRepository())

  val navController = rememberNavController()
  val navigation = remember(navController) { Navigation(navController) }

  UserProfileFollowers(
      navigation = navigation,
      userProfileViewModel = dummyViewModel,
      userProfile = mockUser3)
}
