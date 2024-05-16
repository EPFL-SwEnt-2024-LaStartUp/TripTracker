package com.example.triptracker.view.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.profile.AmbientUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel

// set up a dummy profile for testing
val dummyProfile = UserProfile("test@gmail.com", "Test User", "test", "test bio")

/**
 * Displays an itinerary in the list of itineraries
 *
 * @param itinerary: Itinerary object to display
 * @param navigation: Navigation object to use for navigation
 * @param boxHeight: Height of the box that contains the itinerary
 * @param userProfileViewModel: UserProfileViewModel object to use for fetching user profiles
 * @param onClick: Function to call when the itinerary is clicked
 * @param test : Boolean to test the function
 */
@Composable
fun DisplayItinerary(
    itinerary: Itinerary,
    boxHeight: Dp = 200.dp,
    userProfileViewModel: UserProfileViewModel = viewModel(),
    onClick: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(),
    test: Boolean = false
) {
  // Number of additional itineraries not displayed
  val pinListString = fetchPinNames(itinerary)
  // The height of the box that contains the itinerary, fixed
  // The padding around the box
  val paddingAround = 15.dp
  // The size of the user's avatar/profile picture
  val avatarSize = 25.dp
  // Boolean to check if the user profile is ready to display
  var readyToDisplay by remember { mutableStateOf(false) }
  // The user profile fetched from the database for each path
  var dbProfile by remember { mutableStateOf(UserProfile("")) }
  // The current user profile of the user using the app
  val ambientProfile = AmbientUserProfile.current

  userProfileViewModel.getUserProfile(itinerary.userMail) {
    if (it != null) {
      dbProfile = it
      readyToDisplay = true
    }
  }

  when (true) {
    false -> {
      Log.d("UserProfile", "User profile is null")
    }
    else -> {
      Box(
          modifier =
              Modifier.fillMaxWidth()
                  .padding(paddingAround)
                  .height(boxHeight)
                  .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))
                  .clickable { // When you click on an itinerary, it should bring you to the map
                    // overview with the selected itinerary highlighted and the first pinned places
                    onClick()
                  }
                  .testTag("Itinerary")) {
            Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween) {
                    // change the image to the user's profile picture
                    Row() {
                      AsyncImage(
                          model = dbProfile.profileImageUrl,
                          contentDescription = "User Avatar",
                          modifier =
                              Modifier.size(avatarSize)
                                  .clip(CircleShape)
                                  .testTag("ProfilePic")
                                  .clickable { /* TODO bring user to profile page */})

                      Spacer(modifier = Modifier.width(15.dp))
                      Text(
                          text = dbProfile.username,
                          fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                          fontWeight = FontWeight.Normal,
                          fontSize = 16.sp,
                          color = md_theme_grey,
                          modifier = Modifier.testTag("Username"))
                    }

                    Spacer(modifier = Modifier.width(120.dp))
                    if (ambientProfile.userProfile.value.favoritesPaths.contains(itinerary.id)) {
                      // If the user has favorited this itinerary, display a star orange
                      Icon(
                          imageVector = Icons.Outlined.Star,
                          contentDescription = "Star",
                          tint = md_theme_orange,
                          modifier =
                              Modifier.size(20.dp).clickable {
                                userProfileViewModel.removeFavorite(ambientProfile, itinerary.id)
                              })
                    } else {
                      // If the user has not favorited this itinerary, display a star grey
                      Icon(
                          imageVector = Icons.Outlined.StarBorder,
                          contentDescription = "Star",
                          tint = md_theme_grey,
                          modifier =
                              Modifier.size(20.dp).clickable {
                                userProfileViewModel.addFavorite(ambientProfile, itinerary.id)
                                homeViewModel.incrementSaveCount(
                                    itinerary.id) // when click on grey star, increment save count
                              })
                    }
                  }
              Spacer(modifier = Modifier.width(120.dp))
              Spacer(modifier = Modifier.height(5.dp))
              Log.d("ItineraryRoute", itinerary.route.toString())
              Text(
                  text = itinerary.title,
                  fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                  fontWeight = FontWeight.Bold,
                  fontSize = 24.sp,
                  color = md_theme_light_onPrimary,
                  modifier = Modifier.testTag("Title"))
              Text(
                  text = "${itinerary.flameCount}🔥",
                  color = md_theme_orange, // This is the orange color
                  fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                  fontSize = 14.sp)
              Spacer(modifier = Modifier.height(30.dp).weight(1f))
              Text(
                  text = pinListString,
                  fontSize = 14.sp,
                  modifier = Modifier.fillMaxWidth().testTag("PinList"),
                  maxLines = 2,
                  overflow = "and more".let { TextOverflow.Ellipsis },
                  fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                  color = md_theme_grey)
            }
          }
    }
  }
}

private fun fetchPinNames(itinerary: Itinerary): String {
  val pinNames = mutableListOf<String>()
  for (pin in itinerary.pinnedPlaces) {
    pinNames.add(pin.name)
  }
  Log.d("PinNames", pinNames.toString())
  return convertPinListToString(pinNames)
}

/**
 * Helper function to convert a list of pin names to a string
 *
 * @param pinList: List of pin names
 * @return String representation of the list of pin names
 */
private fun convertPinListToString(pinList: List<String>): String {
  val numOfPinsToDisplay = 2
  return if (pinList.size <= numOfPinsToDisplay) {
    pinList.joinToString(", ")
  } else {
    val displayedPins = pinList.take(numOfPinsToDisplay).joinToString(", ")
    val remainingCount = pinList.size - numOfPinsToDisplay
    "$displayedPins, and $remainingCount more"
  }
}
