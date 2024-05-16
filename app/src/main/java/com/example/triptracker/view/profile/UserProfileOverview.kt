package com.example.triptracker.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.theme.md_theme_light_outlineVariant
import com.example.triptracker.view.theme.md_theme_light_primary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel

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
    homeViewModel: HomeViewModel = viewModel()
) {
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

  Scaffold(
      topBar = {},
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.fillMaxSize().testTag("ProfileOverview")) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
          Row(
              modifier =
                  Modifier.height((LocalConfiguration.current.screenHeightDp * 0.098f).dp)
                      .fillMaxSize()) {}

          // Profile picture and name (later maybe more informations depending of data classes
          // updates
          Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            // Profile picture
            Column() {
              AsyncImage(
                  model = profile.userProfile.value.profileImageUrl,
                  contentDescription = "Profile picture",
                  placeholder = painterResource(id = R.drawable.blankprofile),
                  modifier =
                      Modifier.shadow(
                              elevation = 15.dp,
                              shape = CircleShape,
                              ambientColor = md_theme_light_primary,
                              spotColor = md_theme_light_primary)
                          .padding(start = 15.dp)
                          .size((LocalConfiguration.current.screenHeightDp * 0.11f).dp)
                          .clip(CircleShape),
                  contentScale = ContentScale.Crop)
            }

            // Other informations
            Column() {
              Row() {
                IconButton(onClick = { navigation.navController.navigate(Route.EDIT) }) {
                  Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit")
                }
                Text(
                    text =
                        profile.userProfile.value
                            .username, // I think we only show the pseudo here and keep
                    // birthdate
                    // name and surname private.
                    style =
                        TextStyle(
                            fontSize = sizeUsername,
                            lineHeight = (LocalConfiguration.current.screenHeightDp * 0.016f).sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(700),
                            color = MaterialTheme.colorScheme.inverseSurface,
                            textAlign = TextAlign.Right,
                            letterSpacing =
                                (LocalConfiguration.current.screenHeightDp * 0.0005f).sp,
                        ),
                    modifier =
                        Modifier.width((LocalConfiguration.current.screenHeightDp * 0.67f).dp)
                            .height((LocalConfiguration.current.screenHeightDp * 0.05f).dp)
                            .padding(
                                top = (LocalConfiguration.current.screenHeightDp * 0.012f).dp,
                                end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp))
              }
              Text(
                  text = "Interests",
                  style = secondaryTitleStyle(LocalConfiguration.current.screenHeightDp),
                  modifier =
                      Modifier.align(Alignment.End)
                          .padding(end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp))
              Text(
                  text = "Hiking, Photography", // profile.interestsList
                  style = secondaryContentStyle(LocalConfiguration.current.screenHeightDp),
                  modifier =
                      Modifier.align(Alignment.End)
                          .padding(end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp))

              /*add more informations later if UserProfile is udpated*/
            }
          }
          // Number of trips, followers and following when implemented in the data classes
          Row(
              modifier =
                  Modifier.height((LocalConfiguration.current.screenHeightDp * 0.3f).dp)
                      .align(Alignment.CenterHorizontally)) {
                Column(
                    modifier =
                        Modifier.align(Alignment.CenterVertically)
                            .padding(
                                horizontal =
                                    (LocalConfiguration.current.screenWidthDp * 0.067f).dp)) {
                      Text(
                          text = "${myTripsCount}", // Call to the filtered Itinerarylist
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = bigNumberStyle(LocalConfiguration.current.screenHeightDp))
                      Text(
                          text = "Trips",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = categoryTextStyle(LocalConfiguration.current.screenHeightDp))
                    }
                Column(
                    modifier =
                        Modifier.align(Alignment.CenterVertically)
                            .padding(
                                horizontal = (LocalConfiguration.current.screenWidthDp * 0.067f).dp)
                            .clickable { navigation.navController.navigate(Route.FOLLOWERS) }) {
                      Text(
                          text = "${profile.userProfile.value.followers.size}",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = bigNumberStyle(LocalConfiguration.current.screenHeightDp))
                      Text(
                          text = "Followers",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = categoryTextStyle(LocalConfiguration.current.screenHeightDp))
                    }
                Column(
                    modifier =
                        Modifier.align(Alignment.CenterVertically)
                            .padding(
                                horizontal = (LocalConfiguration.current.screenWidthDp * 0.067f).dp)
                            .clickable { navigation.navController.navigate(Route.FOLLOWING) }) {
                      Text(
                          text = "${profile.userProfile.value.following.size}",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = bigNumberStyle(LocalConfiguration.current.screenHeightDp))
                      Text(
                          text = "Following",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = categoryTextStyle(LocalConfiguration.current.screenHeightDp))
                    }
              }
          // Favourites, Friends, Settings and MyTrips tiles
          Box(
              modifier =
                  Modifier.height((LocalConfiguration.current.screenHeightDp * 0.37f).dp)
                      .width((LocalConfiguration.current.screenWidthDp * 0.9f).dp)
                      .align(Alignment.CenterHorizontally)) {
                ProfileButton(
                    label = "Favorites",
                    icon = Icons.Outlined.FavoriteBorder,
                    onClick = { navigation.navController.navigate(Route.FAVORITES) },
                    modifier = Modifier.align(Alignment.TopStart).testTag("FavoritesButton"))
                ProfileButton(
                    label = "Friends",
                    icon = Icons.Outlined.People,
                    onClick = { navigation.navController.navigate(Route.FRIENDS) },
                    modifier = Modifier.align(Alignment.TopEnd).testTag("FriendsButton"))
                ProfileButton(
                    label = "MyTrips",
                    icon = Icons.Outlined.BookmarkBorder,
                    modifier = Modifier.align(Alignment.BottomStart).testTag("MyTripsButton"),
                    onClick = { navigation.navController.navigate(Route.MYTRIPS) })

                ProfileButton(
                    label = "Settings",
                    icon = Icons.Outlined.Settings,
                    onClick = { navigation.navController.navigate(Route.SETTINGS) },
                    modifier = Modifier.align(Alignment.BottomEnd).testTag("SettingsButton"))
              }
        }
      }
}

@Composable
fun bigNumberStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.024f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.inverseSurface,
      textAlign = TextAlign.Center,
      letterSpacing = (size * 0.0005f).sp)
}

@Composable
fun categoryTextStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.012f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.Light,
      color = MaterialTheme.colorScheme.inverseSurface,
      textAlign = TextAlign.Center,
      letterSpacing = (size * 0.0005f).sp)
}

@Composable
fun buttonTextStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.020f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.SemiBold,
      color = md_theme_light_outlineVariant,
      letterSpacing = (size * 0.0005f).sp)
}

@Composable
fun secondaryTitleStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.014f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight(400),
      color = MaterialTheme.colorScheme.inverseSurface,
      textAlign = TextAlign.Right,
      letterSpacing = (size * 0.0005f).sp)
}

@Composable
fun secondaryContentStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.012f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight(400),
      color = MaterialTheme.colorScheme.inverseSurface,
      textAlign = TextAlign.Right,
      letterSpacing = (size * 0.0005f).sp)
}

@Composable
fun ProfileButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
  Button(
      onClick = onClick,
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
      modifier =
          modifier
              .height((LocalConfiguration.current.screenHeightDp * 0.17f).dp)
              .width((LocalConfiguration.current.screenWidthDp * 0.425f).dp)
              .testTag("ProfileButton")
              .background(
                  color = MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(16.dp))) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Icon(
              icon,
              contentDescription = "$label icon",
              tint = md_theme_orange,
              modifier = Modifier.size((LocalConfiguration.current.screenHeightDp * 0.04f).dp))

          Text(
              text = label,
              style = buttonTextStyle(LocalConfiguration.current.screenHeightDp),
              modifier =
                  Modifier.padding(
                      vertical = (LocalConfiguration.current.screenHeightDp * 0.015f).dp))
        }
      }
}
