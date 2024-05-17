package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
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
import com.example.triptracker.model.location.popupState
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_light_onSurface
import com.example.triptracker.view.theme.md_theme_light_outlineVariant
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.MapPopupViewModel
import com.example.triptracker.viewmodel.MapViewModel
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
  var expanded by remember { mutableStateOf(false) }
  userProfileViewModel.getUserProfile(itinerary.userMail) { itin ->
    if (itin != null) {
      profile = itin
      readyToDisplay = true
    }
  }

  AnimatedVisibility(
      visible = readyToDisplay,
      enter =
          fadeIn() + expandVertically(expandFrom = Alignment.Bottom, animationSpec = tween(100)),
      exit =
          fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(100))) {
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .fillMaxHeight(if (expanded) 0.9f else 0.3f)
                    .animateContentSize()
                    .background(
                        color = md_theme_light_onSurface,
                        shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))) {
              Column(
                  modifier =
                      Modifier.fillMaxWidth()
                          .testTag("PathOverlaySheet")
                          .padding(vertical = 10.dp, horizontal = 25.dp)) {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier =
                            Modifier.size(30.dp)
                                .align(Alignment.CenterHorizontally)
                                .testTag("BackButton")) {
                          Icon(
                              imageVector =
                                  if (expanded) Icons.Outlined.ExpandMore
                                  else Icons.Outlined.ExpandLess,
                              contentDescription = "Back",
                              tint = md_theme_light_outlineVariant,
                              modifier = Modifier.size(30.dp))
                        }
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = profile.username + "'s Path",
                        color = md_theme_light_onPrimary,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        fontWeight = FontWeight.SemiBold,
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
            imageVector = Icons.Outlined.RadioButtonUnchecked,
            contentDescription = "Location pin",
            tint = md_theme_grey)
        Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
          Text(
              text = pinnedPlace.name,
              color = Color.White,
              fontSize = 16.sp,
              fontFamily = FontFamily(Font(R.font.montserrat_regular)),
              fontWeight = FontWeight.Normal,
              modifier = Modifier.padding(end = 5.dp).testTag("PathItemTitle"))

          // Fetch address
          AddressText(
              mpv = MapPopupViewModel(),
              latitude = pinnedPlace.latitude.toFloat(),
              longitude = pinnedPlace.longitude.toFloat())
        }
        Icon(
            painterResource(id = R.drawable.rightarrow),
            modifier = Modifier.size(12.dp),
            contentDescription = "More info",
            tint = md_theme_grey)
      }
  Spacer(modifier = Modifier.height(16.dp))
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun AddressText(mpv: MapPopupViewModel, latitude: Float, longitude: Float) {
  val address by mpv.address.observeAsState("Loading address...")
  Log.d("AddressText", "Address: $address")

  // take only the first part of the address (the street)
  val addressParts = address.split(",")
  val street = addressParts[0]

  // Trigger the address fetch
  LaunchedEffect(key1 = latitude, key2 = longitude) { mpv.fetchAddressForPin(latitude, longitude) }

  Text(
      text = street,
      modifier = Modifier.testTag("AddressText"),
      color = md_theme_grey,
      fontSize = 13.sp,
      fontFamily = FontFamily(Font(R.font.montserrat_light)),
      fontWeight = FontWeight.Light)
}

@Composable
fun StartScreen(
    itinerary: Itinerary,
    userProfileViewModel: UserProfileViewModel,
    onClick: () -> Unit,
    userProfile: MutableUserProfile,
    homeViewModel: HomeViewModel = HomeViewModel(),
    mapViewModel: MapViewModel
) {
  val configuration = LocalConfiguration.current
  val screenWidth = configuration.screenWidthDp.dp
  val screenHeight = configuration.screenHeightDp.dp

  // The size of the user's avatar/profile picture
  val avatarSize = 35.dp
  var userOfPost by remember { mutableStateOf(UserProfile("")) }

  userProfileViewModel.getUserProfile(itinerary.userMail) { itin ->
    if (itin != null) {
      userOfPost = itin
    }
  }
  val imageIsEmpty = remember { mutableStateOf(true) }
  val descriptionsOpen = remember { mutableStateOf(List(itinerary.pinnedPlaces.size) { false }) }
  Log.d("descriptionsOpen", descriptionsOpen.value.toString())
  Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(), contentAlignment = Alignment.Center) {
    Box(
        modifier =
            Modifier.fillMaxWidth(0.9f)
                .fillMaxHeight(0.95f)
                .background(
                    color = md_theme_light_onSurface,
                    shape =
                        RoundedCornerShape(
                            topStart = 35.dp,
                            topEnd = 35.dp,
                            bottomStart = 35.dp,
                            bottomEnd = 35.dp))) {
          Column(
              modifier =
                  Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                      .padding(top = 15.dp, start = 25.dp, end = 25.dp, bottom = 10.dp)) {
                IconButton(
                    onClick = {
                      // When you click on the back button, it should bring you back to the map
                      mapViewModel.popUpState.value = popupState.DISPLAYITINERARY
                    },
                    modifier =
                        Modifier.size(40.dp)
                            .align(Alignment.CenterHorizontally)
                            .testTag("BackButton")) {
                      Icon(
                          imageVector = Icons.Outlined.ExpandLess,
                          contentDescription = "Back",
                          tint = md_theme_light_outlineVariant,
                          modifier = Modifier.size(40.dp))
                    }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                      // change the image to the user's profile picture
                      AsyncImage(
                          model = userOfPost.profileImageUrl,
                          contentDescription = "User Avatar",
                          modifier =
                              Modifier.size(avatarSize)
                                  .clip(CircleShape)
                                  .sizeIn(maxWidth = 20.dp)
                                  .testTag("ProfilePic")
                                  .clickable { /* TODO bring user to profile page */})

                      Spacer(modifier = Modifier.width(15.dp))
                      Text(
                          text = userOfPost.username,
                          fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                          //                wrapContentHeight(align = Alignment.CenterVertically),
                          fontWeight = FontWeight.Normal,
                          fontSize = 16.sp,
                          color = md_theme_light_outlineVariant,
                          modifier =
                              Modifier.testTag("Username")
                                  .wrapContentHeight(align = Alignment.CenterVertically))
                      Spacer(Modifier.weight(1f))

                      if (userProfile.userProfile.value.favoritesPaths.contains(itinerary.id)) {
                        // If the user has favorited this itinerary, display a star orange
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Star",
                            tint = md_theme_orange,
                            modifier =
                                Modifier.size(30.dp).clickable {
                                  userProfileViewModel.removeFavorite(userProfile, itinerary.id)
                                })
                      } else {
                        // If the user has not favorited this itinerary, display a star grey
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = "Star",
                            tint = md_theme_grey,
                            modifier =
                                Modifier.size(30.dp).clickable {
                                  userProfileViewModel.addFavorite(userProfile, itinerary.id)
                                  homeViewModel.incrementSaveCount(
                                      itinerary.id) // when click on grey star, increment save count
                                })
                      }
                    }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = itinerary.title,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = md_theme_light_onPrimary,
                    modifier = Modifier.testTag("Title"))
                Text(
                    text = "${itinerary.flameCount} 🔥",
                    color = md_theme_orange, // This is the orange color
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp,
                    modifier =
                        Modifier.padding(
                                bottom = 20.dp,
                                top = 10.dp,
                            )
                            .testTag("FlameCount"))
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()) {
                      LazyColumn(
                          modifier =
                              Modifier.padding(
                                      top = 10.dp, start = 10.dp, end = 10.dp, bottom = 40.dp)
                                  .size(screenWidth, screenHeight * 0.2f)) {
                            items(itinerary.pinnedPlaces) { pin ->
                              val index = itinerary.pinnedPlaces.indexOf(pin)
                              Row(
                                  verticalAlignment = Alignment.CenterVertically,
                                  modifier =
                                      Modifier.clickable {
                                            val oldValue = descriptionsOpen.value[index]
                                            descriptionsOpen.value =
                                                descriptionsOpen.value.toMutableList().also {
                                                  it[index] = !oldValue
                                                }
                                          }
                                          .fillMaxWidth()) {
                                    Text(
                                        text =
                                            "•   ${if(!descriptionsOpen.value[index]) pin.name.take(30) else pin.name}" +
                                                if (pin.name.length > 30 &&
                                                    !descriptionsOpen.value[index])
                                                    "..."
                                                else "",
                                        color = md_theme_light_outlineVariant,
                                        fontFamily = Montserrat,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                    )
                                    Icon(
                                        imageVector =
                                            if (descriptionsOpen.value[index])
                                                Icons.Outlined.ExpandLess
                                            else Icons.Outlined.ExpandMore,
                                        contentDescription = "Arrow",
                                        tint = md_theme_grey,
                                        modifier = Modifier.size(20.dp))
                                  }
                              Spacer(modifier = Modifier.height(5.dp))
                              if (descriptionsOpen.value[index]) {
                                Text(
                                    text = pin.description,
                                    color = md_theme_light_outlineVariant,
                                    fontSize = 14.sp,
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.padding(start = 30.dp))
                                Spacer(modifier = Modifier.height(5.dp))
                              }
                              Spacer(modifier = Modifier.height(5.dp))
                            }
                          }
                      // check if it is possible to display the images
                      LazyRow(
                          modifier =
                              Modifier.height(
                                  if (imageIsEmpty.value) 0.dp else screenHeight * 0.25f)
                                  .verticalScroll(rememberScrollState()),
                          verticalAlignment = Alignment.CenterVertically) {
                            items(itinerary.pinnedPlaces) { pin ->
                              for (image in pin.image_url) {
                                imageIsEmpty.value = false
                                AsyncImage(
                                    model = image,
                                    contentDescription = pin.description,
                                    modifier =
                                        Modifier.clip(
                                                RoundedCornerShape(corner = CornerSize(15.dp)))
                                            .background(Color.Red))

                                Spacer(modifier = Modifier.width(15.dp))
                              }
                            }
                          }
                      if (imageIsEmpty.value) {
                        Text(
                            text = "No images to display",
                            color = md_theme_light_outlineVariant,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 20.dp).height(screenHeight * 0.25f))
                      }
                      // add spacer proportional to the screen height
                      Button(
                          onClick = {
                            onClick()
                            mapViewModel.asStartItinerary.value = true
                          },
                          modifier =
                              Modifier.padding(bottom = 20.dp, top = 10.dp)
                                  .align(Alignment.CenterHorizontally)
                                  .height(
                                      screenHeight *
                                          0.07f) // Set a specific height for the button to make it
                                  // larger
                                  .fillMaxWidth(
                                      fraction = 0.5f), // Make the button fill 90% of the width
                          shape = RoundedCornerShape(50.dp),
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
  }
}
