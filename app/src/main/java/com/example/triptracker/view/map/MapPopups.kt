package com.example.triptracker.view.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_light_onSurface
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.MapPopupViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * PathOverlaySheet is a composable function that displays the all of the pins of a path
 *
 * @param itinerary Itinerary of that path
 */
@Composable
fun PathOverlaySheet(
    itinerary: Itinerary,
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
    onClick: (Pin) -> Unit
) {
  var readyToDisplay by remember { mutableStateOf(false) }
  var profile by remember { mutableStateOf(UserProfile("")) }

  userProfileViewModel.getUserProfile(itinerary.userMail) { itin ->
    if (itin != null) {
      profile = itin
      readyToDisplay = true
    }
  }

  when (readyToDisplay) {
    false -> {
      Log.d("UserProfile", "User profile is null")
    }
    else -> {
      Box(
          modifier =
              Modifier.fillMaxWidth()
                  .fillMaxHeight()
                  .background(
                      color = md_theme_light_onSurface,
                      shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))) {
            Column(modifier = Modifier.fillMaxWidth().testTag("PathOverlaySheet").padding(25.dp)) {
              Text(
                  text = profile.username + "'s Path",
                  color = md_theme_light_onPrimary,
                  modifier = Modifier.padding(end = 10.dp))
              Spacer(modifier = Modifier.height(16.dp))

              // This lazy column will display all the pins in the path
              // Each pin will be displayed using the PathItem composable
              LazyColumn {
                items(itinerary.pinnedPlaces) { pin ->
                  PathItem(pin, onClick)
                  Divider(thickness = 1.dp, color = md_theme_light_onSurface)
                }
              }
            }
          }
    }
  }
}

/**
 * PathItem is a composable function that displays a single pin in the path
 *
 * @param pinnedPlace specific Pin to be displayed
 */
@Composable
fun PathItem(pinnedPlace: Pin, onClick: (Pin) -> Unit) {
  Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.clickable { onClick(pinnedPlace) }.testTag("PathItem")) {
        Icon(
            painter =
                painterResource(
                    id = R.drawable.ic_gps_fixed), // Replace with your actual pin icon resource
            contentDescription = "Location pin",
            tint = Color.White)
        Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
          Text(text = pinnedPlace.name, color = Color.White)
          // Fetch address
          AddressText(
              mpv = MapPopupViewModel(),
              latitude = pinnedPlace.latitude.toFloat(),
              longitude = pinnedPlace.longitude.toFloat())
        }
        Icon(
            painterResource(id = R.drawable.rightarrow),
            modifier = Modifier.size(16.dp),
            contentDescription = "More info",
            tint = Color.White)
      }
  Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun AddressText(mpv: MapPopupViewModel, latitude: Float, longitude: Float) {
  val address by mpv.address.observeAsState("Loading address...")

  // Trigger the address fetch
  LaunchedEffect(key1 = latitude, key2 = longitude) { mpv.fetchAddressForPin(latitude, longitude) }

  Text(text = address, color = Color.White, modifier = Modifier.testTag("AddressText"))
}

@Composable
fun StartScreen(itinerary: Itinerary, uservm: UserProfileViewModel, onClick: () -> Unit) {
  val configuration = LocalConfiguration.current
  val screenWidth = configuration.screenWidthDp.dp
  val screenHeight = configuration.screenHeightDp.dp

  // The size of the user's avatar/profile picture
  val avatarSize = 35.dp
  var userofpost by remember { mutableStateOf(UserProfile("")) }

  uservm.getUserProfile(itinerary.userMail) { itin ->
    if (itin != null) {
      userofpost = itin
    }
  }

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .fillMaxHeight()
              .background(
                  color = md_theme_light_onSurface,
                  shape =
                      RoundedCornerShape(
                          topStart = 35.dp,
                          topEnd = 35.dp,
                          bottomStart = 35.dp,
                          bottomEnd = 35.dp))) {
        Column(modifier = Modifier.fillMaxWidth().padding(25.dp).padding(top = 30.dp)) {
          Row(modifier = Modifier.fillMaxWidth()) {
            // change the image to the user's profile picture
            AsyncImage(
                model = userofpost.profileImageUrl,
                contentDescription = "User Avatar",
                modifier =
                    Modifier.size(avatarSize)
                        .clip(CircleShape)
                        .sizeIn(maxWidth = 20.dp)
                        .testTag("ProfilePic")
                        .clickable { /* TODO bring user to profile page */})

            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = userofpost.name,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                //                wrapContentHeight(align = Alignment.CenterVertically),
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = md_theme_grey,
                modifier =
                    Modifier.testTag("Username")
                        .wrapContentHeight(align = Alignment.CenterVertically))
            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Star",
                Modifier.size(40.dp))
          }
          Spacer(modifier = Modifier.height(20.dp))
          Text(
              text = itinerary.title,
              fontFamily = FontFamily(Font(R.font.montserrat_regular)),
              fontWeight = FontWeight.Bold,
              fontSize = 30.sp,
              color = md_theme_light_onPrimary,
              modifier = Modifier.testTag("Title"))
          Text(
              text = "${itinerary.flameCount}🔥",
              color = md_theme_orange, // This is the orange color
              fontFamily = FontFamily(Font(R.font.montserrat_regular)),
              fontSize = 20.sp,
              modifier =
                  Modifier.padding(
                      bottom = 20.dp,
                      top = 10.dp,
                  ))
          LazyColumn(
              modifier =
                  Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 40.dp)
                      .size(screenWidth, screenHeight * 0.08f)) {
                items(itinerary.pinnedPlaces) { pin ->
                  Text(text = "• ${pin.name}", color = md_theme_grey, fontSize = 20.sp)
                  Spacer(modifier = Modifier.height(3.dp))
                }
              }

          LazyRow {
            items(itinerary.pinnedPlaces) { pin ->
              for (image in pin.image_url) {
                AsyncImage(
                    model = image,
                    contentDescription = pin.description,
                    modifier =
                        Modifier.clip(RoundedCornerShape(corner = CornerSize(14.dp)))
                            .size(screenWidth * 0.9f, screenHeight * 0.3f))

                Spacer(modifier = Modifier.width(20.dp))
              }
            }
          }
          Button(
              onClick = { onClick() },
              modifier =
                  Modifier.padding(bottom = 30.dp, top = 10.dp)
                      .align(Alignment.CenterHorizontally)
                      .height(56.dp) // Set a specific height for the button to make it larger
                      .fillMaxWidth(fraction = 0.5f), // Make the button fill 90% of the width
              shape = RoundedCornerShape(35.dp),
              colors =
                  ButtonDefaults.buttonColors(
                      backgroundColor = Color(0xFFF06F24),
                  ) // Rounded corners with a radius of 12.dp
              ) {
                Text(
                    "Start",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold)
              }
        }
      }
}
