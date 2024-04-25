package com.example.triptracker.view.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.popupState
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.MapViewModel

/**
 * Displays an itinerary in the list of itineraries
 *
 * @param itinerary: Itinerary object to display
 * @param navigation: Navigation object to use for navigation
 * @param pinNamesMap: Map of itinerary ID to list of pin names
 */
@Composable
fun DisplayItinerary(
    itinerary: Itinerary,
    navigation: Navigation,
    // onClick: () -> Unit, TODO : Uncomment this line when needed
    mapViewModel: MapViewModel,
) {
  // Number of additional itineraries not displayed
  val pinListString = fetchPinNames(itinerary)
  // The height of the box that contains the itinerary, fixed
  val boxHeight = 200.dp
  // The padding around the box
  val paddingAround = 15.dp
  // The size of the user's avatar/profile picture
  val avatarSize = 20.dp
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .padding(paddingAround)
              .height(boxHeight)
              .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))
              .clickable { // When you click on an itinerary, it should bring you to the map
                // overview with the selected itinerary highlighted and the first pinned places

                if (navigation.getCurrentDestination() ==
                    TopLevelDestination(Route.MAPS, Icons.Outlined.Place, "Maps")) {
                  mapViewModel.mapPopupState.value = popupState.PATHOVERLAY
                } else {
                  navigation.navigateTo(navigation.getTopLevelDestinations()[1])
                  mapViewModel.displayPopUp.value = true
                }
                // TODO : when changing Top Level Destination, the navbar should be updated to
                // highlight the correct tab.
                // TODO : Would call DisplayItineraryInMap or sth similar later on
                // onClick()
              }
              .testTag("Itinerary")) {
        Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {
          Row(modifier = Modifier.fillMaxWidth()) {
            // change the image to the user's profile picture
            AsyncImage(
                model =
                    "https://blueprintdigital.com/wp-content/uploads/2014/11/stock-photo-happy-man-giving-okay-sign-portrait-on-white-background-141327337.jpg",
                contentDescription = "User Avatar",
                modifier =
                    Modifier.size(avatarSize)
                        .clip(CircleShape)
                        .testTag("ProfilePic")
                        .clickable { /* TODO bring user to profile page */})
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = itinerary.username,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = md_theme_grey,
                modifier = Modifier.testTag("Username"))
            Spacer(modifier = Modifier.width(120.dp))
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Star",
                Modifier.size(20.dp))
          }
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
  return if (pinList.size <= 3) {
    pinList.joinToString(", ")
  } else {
    val displayedPins = pinList.take(3).joinToString(", ")
    val remainingCount = pinList.size - 3
    "$displayedPins, and $remainingCount more"
  }
}
