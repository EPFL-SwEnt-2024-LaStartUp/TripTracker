package com.example.triptracker.view.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import com.example.triptracker.viewmodel.loggedUser

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
    userProfileViewModel: UserProfileViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    navigation: Navigation
) {
  val userMail: String = loggedUser.email ?: ""
  var readyToDisplay by remember { mutableStateOf(false) }
  var profile by remember { mutableStateOf(UserProfile("")) }
  val myTripsList = homeViewModel.filteredItineraryList
  var myTripsCount = 0
  myTripsList.observeForever(Observer { list -> myTripsCount = list.size })

  userProfileViewModel.getUserProfile(userMail) { fetch ->
    if (fetch != null) {
      profile = fetch
      readyToDisplay = true
    }
  }

  when (readyToDisplay) {
    false -> {
      Log.d("UserProfile", "User profile is null")
    }
    true -> {
      homeViewModel.setSearchFilter(FilterType.USERNAME)
      homeViewModel.setSearchQuery(
          profile.username) // Filters the list of trips on user that created it
      var sizeUsername = 24.sp
      if (profile.username.length > 15) {
        sizeUsername = 18.sp
      }

      Scaffold(
          topBar = {},
          bottomBar = { NavigationBar(navigation) },
          modifier = Modifier.fillMaxSize().testTag("ProfileOverview")) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
              Row(modifier = Modifier.height(75.dp).fillMaxSize()) {}

              // Profile picture and name (later maybe more informations depending of data classes
              // updates
              Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                // Profile picture
                Column() {
                  AsyncImage(
                      model = profile.profileImageUrl,
                      contentDescription = "Profile picture",
                      placeholder = painterResource(id = R.drawable.blankprofile),
                      modifier =
                          Modifier.shadow(
                                  elevation = 15.dp,
                                  shape = CircleShape,
                                  ambientColor = md_theme_light_dark,
                                  spotColor = md_theme_light_dark)
                              .padding(start = 15.dp)
                              .size(110.dp)
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
                        text = profile.username, // I think we only show the pseudo here and keep
                        // birthdate
                        // name and surname private.
                        style =
                            TextStyle(
                                fontSize = sizeUsername,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat)),
                                fontWeight = FontWeight(700),
                                color = md_theme_light_dark,
                                textAlign = TextAlign.Right,
                                letterSpacing = 0.5.sp,
                            ),
                        modifier =
                            Modifier.width(250.dp).height(37.dp).padding(top = 12.dp, end = 15.dp))
                  }
                  Text(
                      text = "Interests",
                      style = AppTypography.secondaryTitleStyle,
                      modifier = Modifier.align(Alignment.End).padding(end = 15.dp))
                  Text(
                      text = "Hiking, Photography", // profile.interestsList
                      style = AppTypography.secondaryContentStyle,
                      modifier = Modifier.align(Alignment.End).padding(end = 15.dp))

                  /*add more informations later if UserProfile is udpated*/
                }
              }
              // Number of trips, followers and following when implemented in the data classes
              Row(modifier = Modifier.height(225.dp).align(Alignment.CenterHorizontally)) {
                Column(
                    modifier =
                        Modifier.align(Alignment.CenterVertically).padding(horizontal = 40.dp)) {
                      Text(
                          text = "${myTripsCount}", // Call to the filtered Itinerarylist
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = AppTypography.bigNumberStyle)
                      Text(
                          text = "Trips",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = AppTypography.categoryTextStyle)
                    }
                Column(
                    modifier =
                        Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                      Text(
                          text = "${profile.followers.size}",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = AppTypography.bigNumberStyle)
                      Text(
                          text = "Followers",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = AppTypography.categoryTextStyle)
                    }
                Column(
                    modifier =
                        Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                      Text(
                          text = "${profile.following.size}",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = AppTypography.bigNumberStyle)
                      Text(
                          text = "Following",
                          modifier = Modifier.align(Alignment.CenterHorizontally),
                          style = AppTypography.categoryTextStyle)
                    }
              }
              // Favourites, Friends, Settings and MyTrips tiles
              Box(
                  modifier =
                      Modifier.height(300.dp).width(350.dp).align(Alignment.CenterHorizontally)) {
                    ProfileButton(
                        label = "Favorites",
                        icon = Icons.Outlined.FavoriteBorder,
                        onClick = { navigation.navController.navigate(Route.FAVORITES) },
                        modifier = Modifier.align(Alignment.TopStart).testTag("FavoritesButton"))
                    ProfileButton(
                        label = "Friends",
                        icon = Icons.Outlined.People,
                        onClick = { navigation.navController.navigate(Route.FAVORITES) },
                        modifier = Modifier.align(Alignment.TopEnd).testTag("FriendsButton"))
                    ProfileButton(
                        label = "MyTrips",
                        icon = Icons.Outlined.BookmarkBorder,
                        onClick = { navigation.navController.navigate(Route.MYTRIPS) },
                        modifier = Modifier.align(Alignment.BottomStart).testTag("MyTripsButton"))
                    ProfileButton(
                        label = "Settings",
                        icon = Icons.Outlined.Settings,
                        onClick = { navigation.navController.navigate(Route.FAVORITES) },
                        modifier = Modifier.align(Alignment.BottomEnd).testTag("SettingsButton"))
                  }
            }
          }
    }
  }
}

object AppTypography {

  val bigNumberStyle =
      TextStyle(
          fontSize = 24.sp,
          lineHeight = 16.sp,
          fontFamily = FontFamily(Font(R.font.montserrat)),
          fontWeight = FontWeight.Bold,
          color = md_theme_light_dark,
          textAlign = TextAlign.Center,
          letterSpacing = 0.5.sp)

  val categoryTextStyle =
      TextStyle(
          fontSize = 12.sp,
          lineHeight = 16.sp,
          fontFamily = FontFamily(Font(R.font.montserrat)),
          fontWeight = FontWeight.Light,
          color = md_theme_dark_gray,
          textAlign = TextAlign.Center,
          letterSpacing = 0.5.sp)

  val buttonTextStyle =
      TextStyle(
          fontSize = 20.sp,
          lineHeight = 16.sp,
          fontFamily = FontFamily(Font(R.font.montserrat)),
          fontWeight = FontWeight.SemiBold,
          color = md_theme_dark_gray,
          letterSpacing = 0.5.sp)

  val secondaryTitleStyle =
      TextStyle(
          fontSize = 14.sp,
          lineHeight = 16.sp,
          fontFamily = FontFamily(Font(R.font.montserrat)),
          fontWeight = FontWeight(400),
          color = md_theme_light_dark,
          textAlign = TextAlign.Right,
          letterSpacing = 0.5.sp,
      )

  val secondaryContentStyle =
      TextStyle(
          fontSize = 12.sp,
          lineHeight = 16.sp,
          fontFamily = FontFamily(Font(R.font.montserrat)),
          fontWeight = FontWeight(400),
          color = md_theme_dark_gray,
          textAlign = TextAlign.Right,
          letterSpacing = 0.5.sp,
      )
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
      colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
      modifier =
          modifier
              .height(130.dp)
              .width(160.dp)
              .testTag("ProfileButton")
              .background(color = md_theme_light_dark, shape = RoundedCornerShape(16.dp))) {
        Column(modifier = Modifier.width(150.dp)) {
          Icon(
              icon,
              contentDescription = "$label icon",
              tint = md_theme_orange,
              modifier = Modifier.size(40.dp))

          Text(
              text = label,
              style = AppTypography.buttonTextStyle,
              modifier = Modifier.padding(vertical = 15.dp))
        }
      }
}
