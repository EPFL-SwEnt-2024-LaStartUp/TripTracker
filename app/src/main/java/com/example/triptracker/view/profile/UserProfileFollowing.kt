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
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.viewmodel.UserProfileViewModel

/** This composable function displays the user's following list. */
@Composable
fun UserProfileFollowing(
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
                    text = "Following",
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
                    modifier = Modifier.weight(1f).testTag("FollowingTitle"))
                Box(modifier = Modifier.size(60.dp))
              }
            }
      },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.fillMaxSize().testTag("FollowingScreen")) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).testTag("FollowingList")) {
          // Display the list of following
          FriendListView(userProfileViewModel, userProfile, false)
        }
      }
}
