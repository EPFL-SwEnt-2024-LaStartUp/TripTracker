package com.example.triptracker.view.profile.subviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.profile.bigNumberStyle
import com.example.triptracker.view.profile.categoryTextStyle

/**
 * This component displays the user's trip count, follower count, and following count.
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param profile : the user profile to display.
 * @param tripsCount : the number of trips the user has taken.
 * @param currentUserProfile : a boolean to know if the profile displayed is the user logged in.
 */
@Composable
fun ProfileCounts(
    navigation: Navigation,
    profile: UserProfile,
    tripsCount: Int,
    currentUserProfile: Boolean = true
) {
  Row(
      modifier = Modifier.wrapContentHeight().fillMaxWidth(),
      horizontalArrangement = Arrangement.Center) {
        Column(
            modifier =
                Modifier.align(Alignment.CenterVertically)
                    .wrapContentHeight()
                    .width((LocalConfiguration.current.screenWidthDp * 0.33f).dp)) {
              Text(
                  text = "$tripsCount",
                  modifier = Modifier.align(Alignment.CenterHorizontally).testTag("TripsCount"),
                  style = bigNumberStyle(LocalConfiguration.current.screenHeightDp))
              Text(
                  text = "Trips",
                  modifier = Modifier.align(Alignment.CenterHorizontally).testTag("TripsTitle"),
                  style = categoryTextStyle(LocalConfiguration.current.screenHeightDp))
            }
        Column(
            modifier =
                Modifier.align(Alignment.CenterVertically)
                    .wrapContentHeight()
                    .width((LocalConfiguration.current.screenWidthDp * 0.33f).dp)
                    .padding(vertical = 5.dp)
                    .clickable(enabled = currentUserProfile) {
                      // Navigate to the followers screen if the user displayed is the user
                      // logged-in
                      navigation.navController.navigate(Route.FOLLOWERS)
                    }) {
              Text(
                  text = "${profile.followers.size}",
                  modifier = Modifier.align(Alignment.CenterHorizontally).testTag("FollowersCount"),
                  style = bigNumberStyle(LocalConfiguration.current.screenHeightDp))
              Text(
                  text = "Followers",
                  modifier = Modifier.align(Alignment.CenterHorizontally).testTag("FollowersTitle"),
                  style = categoryTextStyle(LocalConfiguration.current.screenHeightDp))
            }
        Column(
            modifier =
                Modifier.align(Alignment.CenterVertically)
                    .wrapContentHeight()
                    .width((LocalConfiguration.current.screenWidthDp * 0.33f).dp)
                    .padding(vertical = 5.dp)
                    .clickable(enabled = currentUserProfile) {
                      // Navigate to the following screen if the user displayed is the user
                      // logged-in
                      navigation.navController.navigate(Route.FOLLOWING)
                    }) {
              Text(
                  text = "${profile.following.size}",
                  modifier = Modifier.align(Alignment.CenterHorizontally).testTag("FollowingCount"),
                  style = bigNumberStyle(LocalConfiguration.current.screenHeightDp))
              Text(
                  text = "Following",
                  modifier = Modifier.align(Alignment.CenterHorizontally).testTag("FollowingTitle"),
                  style = categoryTextStyle(LocalConfiguration.current.screenHeightDp))
            }
      }
}
