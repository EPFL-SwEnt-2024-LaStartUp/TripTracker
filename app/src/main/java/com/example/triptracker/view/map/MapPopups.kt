package com.example.triptracker.view.map

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryDownload
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.location.popupState
import com.example.triptracker.model.profile.AmbientUserProfile
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.home.flowerStringBasedOnCount
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_light_onSurface
import com.example.triptracker.view.theme.md_theme_light_outlineVariant
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.MapPopupViewModel
import com.example.triptracker.viewmodel.MapViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import kotlinx.coroutines.delay

val DELAY_ADDRESS = 1000L
val numberOfCharVisible = 20

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
              mapPopupViewModel = MapPopupViewModel(),
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

@Composable
fun AddressText(mapPopupViewModel: MapPopupViewModel, latitude: Float, longitude: Float) {
  val address = remember { mutableStateOf("Loading address...") }

  // Launch a coroutine to fetch the address
  LaunchedEffect(Unit) {
    while (address.value == "Loading address...") {
      mapPopupViewModel.fetchAddressForPin(latitude, longitude)
      // Simulate a delay for fetching the address
      delay(DELAY_ADDRESS)
      address.value = mapPopupViewModel.address.value ?: "Loading address..."
    }
  }

  Text(
      text = getDisplayAddress(address.value),
      modifier = Modifier.testTag("AddressText"),
      color = md_theme_grey,
      fontSize = 13.sp,
      fontFamily = FontFamily(Font(R.font.montserrat_light)),
      fontWeight = FontWeight.Light)
}

/**
 * getDisplayAddress is a helper function that formats the address to display
 *
 * @param address String of the address to format
 * @return String of the formatted address
 */
fun getDisplayAddress(address: String): String {
  val addressParts = address.split(",")
  return if (addressParts.isNotEmpty()) {
    val firstPart = addressParts[0].trim()
    if (firstPart.toIntOrNull() != null && addressParts.size > 1) {
      // The first part is a number, take the string between the first and second comma
      addressParts[0].trim() + ", " + addressParts[1].trim()
    } else {
      // The first part is not a number, take only the first part
      firstPart
    }
  } else {
    address
  }
}

/**
 * Get the height of the image based on whether the image is empty or not
 *
 * @param imageIsEmpty MutableState of a boolean that keeps track of whether there are images to
 *   display or not
 *     @param screenHeight Dp of the screen height
 */
private fun getHeight(imageIsEmpty: MutableState<Boolean>, screenHeight: Dp): Dp {
  return if (imageIsEmpty.value) 0.dp else screenHeight * 0.2f
}

/**
 * ShowPictures is a composable function that displays the images of the pins
 *
 * @param pin Pin to display the images of
 * @param imageIsEmpty MutableState of a boolean that keeps track of whether there are images to
 */
@Composable
private fun ShowPictures(pin: Pin, imageIsEmpty: MutableState<Boolean>) {
  for (image in pin.image_url) {
    imageIsEmpty.value = false
    AsyncImage(
        model = image,
        contentDescription = pin.description,
        modifier = Modifier.clip(RoundedCornerShape(corner = CornerSize(15.dp))))

    Spacer(modifier = Modifier.width(15.dp))
  }
}

@Composable
fun StartScreen(
    itinerary: Itinerary,
    userProfileViewModel: UserProfileViewModel = viewModel(),
    onClick: () -> Unit,
    userProfile: MutableUserProfile,
    homeViewModel: HomeViewModel = viewModel(),
    mapViewModel: MapViewModel = viewModel(),
    offline: Boolean = false
) {
  val configuration = LocalConfiguration.current
  val screenWidth = configuration.screenWidthDp.dp
  val screenHeight = configuration.screenHeightDp.dp

  /* Mutable state variable that holds the loading state of the screen */
  var isLoading by remember { mutableStateOf(false) }

  /** Alpha value for the screen depending on loading state */
  val alpha = if (!isLoading) 1f else 0.99f

  val ambientProfile = AmbientUserProfile.current
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
  val onClickGoBack =
      if (offline) {
        onClick
      } else {
        {
          // When you click on the back button, it should bring you back to the map
          mapViewModel.popUpState.value = popupState.DISPLAYITINERARY
        }
      }
  Box(
      modifier = Modifier.fillMaxWidth().fillMaxHeight().alpha(alpha),
      contentAlignment = Alignment.Center) {
        Column(
            modifier =
                Modifier.fillMaxWidth(0.9f)
                    .fillMaxHeight(0.95f)
                    .background(
                        color = md_theme_light_dark,
                        shape =
                            RoundedCornerShape(
                                topStart = 35.dp,
                                topEnd = 35.dp,
                                bottomStart = 35.dp,
                                bottomEnd = 35.dp))) {
              IconButton(
                  onClick = onClickGoBack,
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
              Column(
                  modifier =
                      Modifier.fillMaxWidth()
                          .fillMaxHeight()
                          .verticalScroll(rememberScrollState())
                          .padding(top = 15.dp, start = 25.dp, end = 25.dp, bottom = 0.dp),
                  verticalArrangement = Arrangement.SpaceBetween) {
                    Column() {
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
                                //                wrapContentHeight(align =
                                // Alignment.CenterVertically),
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = md_theme_light_outlineVariant,
                                modifier =
                                    Modifier.testTag("Username")
                                        .wrapContentHeight(align = Alignment.CenterVertically))
                            Spacer(Modifier.weight(1f))

                            DisplayStar(
                                userProfileViewModel,
                                userProfile,
                                itinerary,
                                homeViewModel,
                                offline) {
                                  isLoading = !isLoading
                                }
                          }

                      // Spacer(modifier = Modifier.height(20.dp))
                      Column(
                          modifier = Modifier.fillMaxWidth().padding(top = screenHeight * 0.015f),
                      ) {
                        Text(
                            text = itinerary.title,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 26.sp,
                            color = md_theme_light_onPrimary,
                            modifier = Modifier.testTag("Title"))
                        Text(
                            text =
                                if (ambientProfile.userProfile.value.flowerMode == 1)
                                    "${itinerary.flameCount} ${flowerStringBasedOnCount(itinerary.flameCount)}"
                                else "${itinerary.flameCount} 🔥",
                            color = md_theme_orange, // This is the orange color
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            modifier =
                                Modifier.padding(
                                        bottom = 10.dp,
                                        top = 10.dp,
                                    )
                                    .testTag("FlameCount"))
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxHeight()) {
                              LazyColumn(
                                  modifier =
                                      Modifier.padding(
                                              top = 10.dp,
                                              start = 10.dp,
                                              end = 10.dp,
                                              bottom = 0.dp)
                                          .size(screenWidth, screenHeight * 0.3f)) {
                                    items(itinerary.pinnedPlaces) { pin ->
                                      val index = itinerary.pinnedPlaces.indexOf(pin)
                                      PinDescription(
                                          descriptionsOpen = descriptionsOpen,
                                          pin = pin,
                                          index = index)
                                      Spacer(modifier = Modifier.height(5.dp))
                                      OnDescriptionOpen(
                                          descriptionsOpen = descriptionsOpen,
                                          pin = pin,
                                          index = index)
                                      Spacer(modifier = Modifier.height(5.dp))
                                    }
                                  }
                            }
                        // check if it is possible to display the images
                        val height = getHeight(imageIsEmpty, screenHeight)
                        LazyRow(
                            modifier = Modifier.height(height),
                            verticalAlignment = Alignment.CenterVertically) {
                              items(itinerary.pinnedPlaces) { pin ->
                                ShowPictures(pin = pin, imageIsEmpty = imageIsEmpty)
                              }
                            }
                        OnImageIsEmpty(imageIsEmpty = imageIsEmpty, screenHeight = screenHeight)
                        // add spacer proportional to the screen height
                        Spacer(modifier = Modifier.height(screenHeight * 0.025f))
                      }
                    }
                    if (!offline) {
                      Button(
                          onClick = {
                            onClick()
                            mapViewModel.asStartItinerary.value = true
                          },
                          modifier =
                              Modifier.padding(bottom = screenHeight * 0.05f)
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
                                  containerColor = Color(0xFFF06F24),
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
        if (isLoading) {
          CircularProgressIndicator(
              modifier = Modifier.size(64.dp).align(Alignment.Center),
              color = md_theme_orange,
              trackColor = md_theme_grey,
          )
        }
      }
}

/** Helper composable functions to lower complexity */

/**
 * DisplayStar is a composable function that displays a star icon that changes color based on
 * whether the user has favorited the itinerary or not
 *
 * @param userProfileViewModel ViewModel for the user's profile
 * @param userProfile MutableUserProfile of the user
 * @param itinerary Itinerary to be favorited
 * @param homeViewModel ViewModel for the home screen
 * @param offline Boolean flag for offline mode
 * @param itineraryDownload Object that contains the download function for the itinerary
 * @param onLoadingChange Function to change the loading state
 */
@Composable
fun DisplayStar(
    userProfileViewModel: UserProfileViewModel,
    userProfile: MutableUserProfile,
    itinerary: Itinerary,
    homeViewModel: HomeViewModel,
    offline: Boolean,
    itineraryDownload: ItineraryDownload = ItineraryDownload(context = LocalContext.current),
    onLoadingChange: () -> Unit
) {

  val actionOnStarFull: () -> Unit = {
    userProfileViewModel.removeFavorite(userProfile, itinerary.id)
    // delete the itinerary from internal storage
    itineraryDownload.deleteItinerary(itinerary.id)
  }

  val actionOnStarEmpty: () -> Unit =
      if (offline) {
        {}
      } else {
        {
          onLoadingChange()
          // save the itinerary to internal storage
          itineraryDownload.saveItineraryToInternalStorage(itinerary) {
            userProfileViewModel.addFavorite(userProfile, itinerary.id)
            // when click on grey star, increment save count
            homeViewModel.incrementSaveCount(itinerary.id)
            onLoadingChange()
          }
        }
      }

  if (userProfile.userProfile.value.favoritesPaths.contains(itinerary.id)) {
    // If the user has favorited this itinerary, display a star orange
    Icon(
        imageVector = Icons.Outlined.Star,
        contentDescription = "Star",
        tint = md_theme_orange,
        modifier = Modifier.size(30.dp).testTag("Star").clickable { actionOnStarFull() })
  } else {
    // If the user has not favorited this itinerary, display a star grey
    Icon(
        imageVector = Icons.Outlined.StarBorder,
        contentDescription = "EmptyStar",
        tint = md_theme_grey,
        modifier = Modifier.size(30.dp).testTag("EmptyStar").clickable { actionOnStarEmpty() })
  }
}

/**
 * PinDescription is a composable function that displays the description of a pin
 *
 * @param descriptionsOpen MutableState of a list of booleans that keeps track of whether the
 *   description of a pin is open or not
 * @param pin Pin to display the description of
 * @param index Index of the pin in the list of pins
 */
@Composable
fun PinDescription(descriptionsOpen: MutableState<List<Boolean>>, pin: Pin, index: Int) {
  Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier =
          Modifier.clickable {
                val oldValue = descriptionsOpen.value[index]
                descriptionsOpen.value =
                    descriptionsOpen.value.toMutableList().also { it[index] = !oldValue }
              }
              .fillMaxWidth()) {
        val displayText =
            if (!descriptionsOpen.value[index]) {
              pin.name.take(numberOfCharVisible) +
                  if (pin.name.length > numberOfCharVisible) "..." else ""
            } else {
              pin.name
            }
        val imageVector =
            if (descriptionsOpen.value[index]) Icons.Outlined.ExpandLess
            else Icons.Outlined.ExpandMore

        Text(
            text = "•  " + displayText.split(",")[0],
            color = md_theme_light_outlineVariant,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
        Icon(
            imageVector = imageVector,
            contentDescription = "Arrow",
            tint = md_theme_grey,
            modifier = Modifier.size(20.dp))
      }
}

/**
 * OnDescriptionOpen is a composable function that displays the description of a pin if it is open
 *
 * @param descriptionsOpen MutableState of a list of booleans that keeps track of whether the
 *   description of a pin is open or not
 * @param pin Pin to display the description of
 * @param index Index of the pin in the list of pins
 */
@Composable
fun OnDescriptionOpen(descriptionsOpen: MutableState<List<Boolean>>, pin: Pin, index: Int) {
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
}

/**
 * OnImageIsEmpty is a composable function that displays a message if there are no images to display
 *
 * @param imageIsEmpty MutableState of a boolean that keeps track of whether there are images to
 *   display or not
 * @param screenHeight Dp of the screen height
 */
@Composable
fun OnImageIsEmpty(imageIsEmpty: MutableState<Boolean>, screenHeight: Dp) {
  if (imageIsEmpty.value) {
    Text(
        text = "No pictures to display",
        color = md_theme_light_outlineVariant,
        fontSize = 16.sp,
        modifier = Modifier.padding(start = 20.dp).height(screenHeight * 0.15f))
  }
}
