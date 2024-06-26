package com.example.triptracker.view.home

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
import com.example.triptracker.model.itinerary.ItineraryDownload
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.network.Connection
import com.example.triptracker.model.profile.AmbientUserProfile
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_background
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_error
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_light_outline
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel

// set up a dummy profile for testing
val dummyProfile = UserProfile("test@gmail.com", "Test User", "test", "test bio")

/**
 * Displays an itinerary in the list of itineraries
 *
 * @param itinerary: Itinerary object to display
 * @param boxHeight: Height of the box that contains the itinerary
 * @param userProfileViewModel: UserProfileViewModel object to use for fetching user profiles
 * @param onClick: Function to call when the itinerary is clicked
 * @param homeViewModel: HomeViewModel object to use for deleting the itinerary
 * @param displayImage: Boolean to display the image of the pinned places
 * @param test : Boolean to test the function
 * @param canBeDeleted: Boolean to check if the itinerary can be deleted
 * @param connection: Connection object to check the device's internet connection
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayItinerary(
    itinerary: Itinerary,
    boxHeight: Dp = 220.dp,
    userProfileViewModel: UserProfileViewModel = viewModel(),
    onClick: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(),
    displayImage: Boolean = false,
    canBeDeleted: Boolean = false,
    connection: Connection = Connection(),
    navigation: Navigation,
    onDelete: () -> Unit = {}
) {
  val configuration = LocalConfiguration.current
  val screenWidth = configuration.screenWidthDp.dp
  val screenHeight = configuration.screenHeightDp.dp

  // The object that contains the download function for the itinerary
  val itineraryDownload = ItineraryDownload(context = LocalContext.current)

  // Number of additional itineraries not displayed
  val pinListString = fetchPinNames(itinerary)
  Log.d("PinListString", pinListString)
  // The height of the box that contains the itinerary, fixed
  // The padding around the box
  val paddingAround = 10.dp
  // The size of the user's avatar/profile picture
  val avatarSize = 25.dp
  // The user profile fetched from the database for each path
  var dbProfile by remember { mutableStateOf(UserProfile("")) }
  // The current user profile of the user using the app
  val ambientProfile = AmbientUserProfile.current
  // Boolean to check if the profile picture should be displayed
  var showProfilePicture by remember { mutableStateOf(false) }

  // Boolean to check if the image is empty
  val imageIsEmpty = remember { mutableStateOf(true) }

  /* Mutable state variable that holds the loading state of the screen */
  var isLoading by remember { mutableStateOf(false) }

  // Mutable state variable that holds the flower mode state of the screen
  val (isFlowerMode, setIsFlowerMode) = remember { mutableStateOf(false) }

  /** Alpha value for the screen depending on loading state */
  val alpha = if (!isLoading) 1f else 0.7f

  var showAlert by remember { mutableStateOf(false) }

  when (showAlert) {
    true -> {
      ShowAlert(
          onDismiss = { showAlert = false },
          onConfirm = {
            onDelete()
            showAlert = false
          })
    }
    false -> {
      /* Do nothing */
    }
  }

  var boxHeightToDisplay = 0.dp
  if (checkIfImage(itinerary) && displayImage) {
    boxHeightToDisplay = 480.dp
  } else {
    boxHeightToDisplay = boxHeight
  }

  userProfileViewModel.getUserProfile(itinerary.userMail) {
    if (it != null) {
      dbProfile = it
    }
  }

  when (showProfilePicture) {
    true -> {
      Box(
          modifier =
              Modifier.fillMaxWidth()
                  .padding(paddingAround)
                  .height(boxHeightToDisplay)
                  .background(
                      color = MaterialTheme.colorScheme.onSurface,
                      shape = RoundedCornerShape(35.dp))) {

            // Close button
            IconButton(
                modifier = Modifier.padding(10.dp).testTag("CloseButton"),
                onClick = { showProfilePicture = false }) {
                  Icon(
                      imageVector = Icons.Outlined.ArrowBackIosNew,
                      contentDescription = "back",
                      tint = md_theme_light_onPrimary)
                }

            Box(
                modifier =
                    Modifier.padding(15.dp)
                        .size(200.dp) // Adjust the size as needed
                        .clip(CircleShape)
                        .background(Color.Gray) // Optional: background color to see the boundaries
                        .align(Alignment.Center)
                        .testTag("ProfilePicBig")) {
                  AsyncImage(
                      model = dbProfile.profileImageUrl,
                      contentDescription = "User Avatar",
                      contentScale = ContentScale.Crop,
                      modifier = Modifier.fillMaxSize())
                }
          }
    }
    false -> {
      val haptics = LocalHapticFeedback.current
      Box(
          modifier =
              Modifier.fillMaxWidth()
                  .padding(paddingAround)
                  .height(boxHeightToDisplay)
                  .shadow(
                      color = md_theme_light_dark.copy(alpha = 0.4f),
                      borderRadius = 35.dp,
                      blurRadius = 25.dp,
                      offsetY = 20.dp,
                      offsetX = 0.dp,
                      spread = 9.dp)
                  .background(color = md_theme_light_dark, shape = RoundedCornerShape(35.dp))
                  .combinedClickable(
                      onClick = {
                        onClick()
                      }, // When you click on an itinerary, it should bring you to the map
                      // overview with the selected itinerary highlighted and the first pinned
                      // places
                      onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (canBeDeleted) {
                          showAlert = true
                        }
                      })
                  .testTag("Itinerary")) {
            Column(modifier = Modifier.alpha(alpha).fillMaxWidth().padding(25.dp)) {
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween) {
                    // change the image to the user's profile picture
                    Row(
                        modifier =
                            Modifier.testTag("UsernameBox").clickable {
                              navigation.navController.navigate(
                                  Route.USER + "/${itinerary.userMail}")
                            },
                    ) {
                      AsyncImage(
                          model = dbProfile.profileImageUrl,
                          contentDescription = "User Avatar",
                          contentScale = ContentScale.Crop,
                          modifier =
                              Modifier.size(avatarSize)
                                  .clip(CircleShape)
                                  .testTag("ProfilePic")
                                  .clickable { showProfilePicture = true })

                      Spacer(modifier = Modifier.width(15.dp))
                      Text(
                          text = dbProfile.username,
                          fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                          fontWeight = FontWeight.Normal,
                          fontSize = 16.sp,
                          color = md_theme_grey,
                          modifier = Modifier.testTag("Username"))
                    }

                    Spacer(modifier = Modifier.width(1.dp))
                    val actionOnStarFull: () -> Unit = {
                      userProfileViewModel.removeFavorite(ambientProfile, itinerary.id)
                      // delete the itinerary from internal storage
                      itineraryDownload.deleteItinerary(itinerary.id)
                    }

                    val actionOnStarEmpty: () -> Unit =
                        if (!connection.isDeviceConnectedToInternet()) {
                          {}
                        } else {
                          {
                            isLoading = true
                            // save the itinerary to internal storage
                            itineraryDownload.saveItineraryToInternalStorage(itinerary) {
                              userProfileViewModel.addFavorite(ambientProfile, itinerary.id)
                              // when click on grey star, increment save count
                              homeViewModel.incrementSaveCount(itinerary.id)
                              isLoading = false
                            }
                          }
                        }
                    Spacer(modifier = Modifier.width(1.dp))
                    if (ambientProfile.userProfile.value.favoritesPaths.contains(itinerary.id)) {
                      // If the user has favorited this itinerary, display a star orange
                      Icon(
                          imageVector = Icons.Outlined.Star,
                          contentDescription = "Star",
                          tint = md_theme_orange,
                          modifier = Modifier.size(28.dp).clickable { actionOnStarFull() })
                    } else {
                      // If the user has not favorited this itinerary, display a star grey
                      Icon(
                          imageVector = Icons.Outlined.StarBorder,
                          contentDescription = "Star",
                          tint = md_theme_grey,
                          modifier = Modifier.size(28.dp).clickable { actionOnStarEmpty() })
                    }
                  }
              Box(
                  modifier =
                      Modifier.combinedClickable(
                          onClick = {
                            onClick()
                          }, // When you click on an itinerary, it should bring you to the map
                          // overview with the selected itinerary highlighted and the first
                          // pinned
                          // places
                          onLongClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            updateDeletedStatus(canBeDeleted) { showAlert = true }
                          })) {
                    Column(
                        modifier =
                            Modifier.combinedClickable(
                                onClick = {
                                  onClick()
                                }, // When you click on an itinerary, it should bring you to the map
                                // overview with the selected itinerary highlighted and the first
                                // pinned
                                // places
                                onLongClick = {
                                  haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                  updateDeletedStatus(canBeDeleted) { showAlert = true }
                                })) {
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
                              text = getTrendingCount(ambientProfile, itinerary),
                              color = md_theme_orange, // This is the orange color
                              fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                              fontSize = 14.sp)
                          Spacer(modifier = Modifier.height(screenHeight * 0.02f))
                          Text(
                              text = pinListString,
                              fontSize = 14.sp,
                              modifier = Modifier.fillMaxWidth().testTag("PinList"),
                              maxLines = 2,
                              overflow = "and more".let { TextOverflow.Ellipsis },
                              fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                              color = md_theme_grey)

                          DisplayListOfPictures(
                              displayImage = displayImage,
                              screenHeight = screenHeight,
                              itinerary = itinerary,
                              imageIsEmpty = imageIsEmpty)
                        }
                  }
            }
            DisplayLoadingScreen(isLoading = isLoading)
          }
    }
  }
}

@Composable
private fun ShowAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {
  AlertDialog(
      shape = RoundedCornerShape(15.dp),
      onDismissRequest = { onDismiss() },
      title = {
        Text(
            text = "Delete Itinerary",
            fontSize = 20.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            color = md_theme_light_onPrimary)
      },
      text = {
        Text(
            text = "Are you sure you want to delete the itinerary?",
            fontSize = 16.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            color = md_theme_light_outline)
      },
      confirmButton = {
        Button(
            modifier = Modifier.testTag("YesCancelItineraryButton"),
            onClick = { onConfirm() },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = md_theme_light_error, contentColor = Color.White),
            shape = RoundedCornerShape(35.dp),
        ) {
          Text(
              text = "Yes, delete",
              fontSize = 14.sp,
              fontFamily = Montserrat,
              fontWeight = FontWeight.SemiBold,
              color = md_theme_light_background)
        }
      },
      dismissButton = {
        Button(
            modifier = Modifier.testTag("NoCancelItineraryButton"),
            onClick = { onDismiss() },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = md_theme_light_black, contentColor = Color.White),
            shape = RoundedCornerShape(35.dp)) {
              Text(
                  text = "No",
                  fontSize = 14.sp,
                  fontFamily = Montserrat,
                  fontWeight = FontWeight.SemiBold,
                  color = md_theme_light_background)
            }
      })
}

private fun updateDeletedStatus(canBeDeleted: Boolean, callback: () -> Unit) {
  if (canBeDeleted) {
    callback()
  }
}

/**
 * Displays the loading screen when the itinerary is downloading
 *
 * @param isLoading: Boolean to check if the itinerary is downloading
 */
@Composable
fun DisplayLoadingScreen(isLoading: Boolean) {
  // Loading bar for when the itinerary is downloading
  if (isLoading) {
    Box(modifier = Modifier.fillMaxSize()) {
      CircularProgressIndicator(
          modifier = Modifier.size(64.dp).align(Alignment.Center),
          color = md_theme_orange,
          trackColor = md_theme_grey,
      )
    }
  }
}

/**
 * Displays the list of pictures of the pinned places in the itinerary
 *
 * @param displayImage: Boolean to display the image of the pinned places
 * @param screenHeight: Dp value of the screen height
 * @param itinerary: Itinerary object to get the pinned places
 * @param imageIsEmpty: MutableState to check if the image is empty
 */
@Composable
private fun DisplayListOfPictures(
    displayImage: Boolean,
    screenHeight: Dp,
    itinerary: Itinerary,
    imageIsEmpty: MutableState<Boolean>
) {
  if (displayImage) {
    Spacer(modifier = Modifier.height(screenHeight * 0.02f))
    LazyRow(
        modifier = Modifier.getHeight(imageIsEmpty, screenHeight),
        verticalAlignment = Alignment.CenterVertically) {
          items(itinerary.pinnedPlaces) { pin -> DisplayPictures(pin, imageIsEmpty) }
        }
  }
}

/**
 * Displays the images of the pinned places in the itinerary
 *
 * @param pin: Pin object to display
 * @param imageIsEmpty: MutableState to check if the image is empty
 */
@Composable
private fun DisplayPictures(pin: Pin, imageIsEmpty: MutableState<Boolean>) {
  for (image in pin.imageUrl) {
    imageIsEmpty.value = false
    AsyncImage(
        model = image,
        contentDescription = pin.description,
        modifier =
            Modifier.clip(RoundedCornerShape(corner = CornerSize(15.dp))).background(Color.Red))
    Spacer(modifier = Modifier.width(15.dp))
  }
}

/**
 * Modifier function to set the height of the box that contains the itinerary
 *
 * @param imageIsEmpty: MutableState to check if the image is empty
 * @param screenHeight: Dp value of the screen height
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.getHeight(imageIsEmpty: MutableState<Boolean>, screenHeight: Dp): Modifier {
  return Modifier.height(if (imageIsEmpty.value) 0.dp else screenHeight * 0.25f)
}

/**
 * Function to get the text to display based on the flame count
 *
 * @param ambientProfile: MutableUserProfile object to get the user's profile
 * @param itinerary: Itinerary object to get the flame count
 */
fun getTrendingCount(ambientProfile: MutableUserProfile, itinerary: Itinerary): String {
  return if (ambientProfile.userProfile.value.flowerMode == 1)
      "${itinerary.flameCount} ${flowerStringBasedOnCount(itinerary.flameCount)}"
  else "${itinerary.flameCount} 🔥"
}

/**
 * Function to fetch the names of the pinned places in the itinerary
 *
 * @param itinerary: Itinerary object to get the pinned places
 */
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

private fun checkIfImage(itinerary: Itinerary): Boolean {
  itinerary.pinnedPlaces.forEach { pin ->
    pin.imageUrl.forEach { imageUrl -> if (imageUrl.isNotEmpty()) return true }
  }
  return false // Return false if no valid URLs are found after checking all
}

/**
 * Function to return a string of flowers based on the flame count
 *
 * @param flameCount: The flame count of the itinerary
 * @return String of flowers based on the flame count
 */
fun flowerStringBasedOnCount(flameCount: Long): String {
  return when {
    flameCount > 700 -> "🌸🌷🌹🌺🌻🌼\uD83D\uDC90"
    flameCount in 601..700 -> "🌸🌷🌹🌺🌻🌼"
    flameCount in 501..600 -> "🌸🌷🌹🌺🌻"
    flameCount in 401..500 -> "🌸🌷🌹\uD83D\uDC90"
    flameCount in 301..400 -> "🌸🌷🌹"
    flameCount in 201..300 -> "🌸🌷"
    flameCount in 0..200 -> "🌸"
    else -> "🌸" // This handles negative values, though it's unlikely in your context
  }
}

/**
 * Modifier function to add a shadow to a composable element
 *
 * @param color: Color of the shadow
 * @param borderRadius: Radius of the shadow
 * @param blurRadius: Blur radius of the shadow
 * @param offsetY: Offset of the shadow in the y direction
 * @param offsetX: Offset of the shadow in the x direction
 * @param spread: Spread of the shadow
 * @param modifier: Modifier to apply the shadow to
 */
fun Modifier.shadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) =
    this.then(
        modifier.drawBehind {
          this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
              frameworkPaint.maskFilter =
                  (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint)
          }
        })
