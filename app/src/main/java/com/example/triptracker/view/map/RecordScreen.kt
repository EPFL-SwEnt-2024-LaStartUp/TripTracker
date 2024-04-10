package com.example.triptracker.view.map

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.example.triptracker.navigation.AllowLocationPermission
import com.example.triptracker.navigation.checkForLocationPermission
import com.example.triptracker.navigation.getCurrentLocation
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Define the delay for the location update
const val DELAY = 5000L

/**
 * Composable function for displaying the RecordScreen.
 *
 * @param context The application context.
 * @param viewModel The RecordViewModel instance.
 */
@Composable
fun RecordScreen(
    context: Context,
    navigation: Navigation,
    viewModel: RecordViewModel = RecordViewModel()
) {
  // default device location is EPFL
  var deviceLocation = DEFAULT_LOCATION

  // Default map properties and UI settings
  var mapProperties =
      MapProperties(
          mapType = MapType.NORMAL, isMyLocationEnabled = checkForLocationPermission(context))
  var uiSettings = MapUiSettings(myLocationButtonEnabled = checkForLocationPermission(context))

  // Get current device location
  getCurrentLocation(context = context, onLocationFetched = { deviceLocation = it })

  // Check for location permission
  when (checkForLocationPermission(context = context)) {
    true -> {
      Scaffold(
          bottomBar = { NavigationBar(navigation) }, modifier = Modifier.testTag("RecordScreen")) {
              innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
              Map(context, viewModel, deviceLocation, mapProperties, uiSettings)
            }
          }
    }
    false -> {
      Scaffold(
          bottomBar = { NavigationBar(navigation) }, modifier = Modifier.testTag("RecordScreen")) {
              innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
              AllowLocationPermission(
                  onPermissionGranted = {
                    mapProperties =
                        MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true)
                    uiSettings = MapUiSettings(myLocationButtonEnabled = true)
                  },
                  onPermissionDenied = {
                    mapProperties =
                        MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = false)
                    uiSettings = MapUiSettings(myLocationButtonEnabled = false)
                  })
            }
          }
    }
  }
}
/**
 * Composable function for displaying the map.
 *
 * @param context The application context.
 * @param viewModel The RecordViewModel instance.
 * @param startLocation The starting location.
 * @param mapProperties: The properties of the map (type, location enabled)
 * @param uiSettings: The settings of the map (location button enabled)
 */
@Composable
fun Map(
    context: Context,
    viewModel: RecordViewModel,
    startLocation: LatLng,
    mapProperties: MapProperties,
    uiSettings: MapUiSettings
) {
  // Mutable state for the device location and the local LatLng list
  var deviceLocation by remember { mutableStateOf(startLocation) }

  // Have a local list of LatLng points to display the route is the only way to display the route
  val localLatLngList = remember { mutableStateListOf<LatLng>() }
  val ui by remember { mutableStateOf(uiSettings) }
  val properties by remember { mutableStateOf(mapProperties) }

  // Coroutine scope for the animations
  val coroutineScope = rememberCoroutineScope()

  // Firebase Repository
  val itineraryRepository = ItineraryRepository()

  // Remember camera position state
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(deviceLocation, 17f)
  }

  // Animate camera to device location
  LaunchedEffect(key1 = deviceLocation) {
    coroutineScope.launch {
      cameraPositionState.animate(
          CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(deviceLocation, 17f)))
    }
  }

  // Update device location
  LaunchedEffect(Unit) {
    while (true) {
      getCurrentLocation(context = context, onLocationFetched = { deviceLocation = it })
      delay(DELAY)
    }
  }

  // Add current location to list every "DELAY" seconds if recording
  LaunchedEffect(key1 = viewModel.isRecording()) {
    while (viewModel.isRecording()) {
      if (!viewModel.isPaused.value) {
        viewModel.addLatLng(deviceLocation)
        if (viewModel.latLongList.isNotEmpty()) {
          localLatLngList.add(viewModel.latLongList.last())
        }
      }
      delay(DELAY)
    }
  }

  // Display the map
  Box {
    Box(modifier = Modifier.fillMaxSize()) {
      GoogleMap(
          modifier =
              Modifier.matchParentSize()
                  .background(
                      brush =
                          Brush.verticalGradient(
                              colors = listOf(Color.Transparent, md_theme_light_dark))),
          cameraPositionState = cameraPositionState,
          properties = properties,
          uiSettings = ui,
      ) {
        Polyline(
            points = localLatLngList.toList(),
            color = md_theme_orange,
            width = 15f,
        )
      }
    }

    // Display gradient for top bar
    Box(modifier = Modifier.matchParentSize().background(gradient).align(Alignment.TopCenter)) {
      Text(
          text = "Record",
          modifier = Modifier.padding(30.dp).align(Alignment.TopCenter),
          fontSize = 24.sp,
          fontFamily = Montserrat,
          fontWeight = FontWeight.SemiBold,
          color = md_theme_light_dark)
    }

    // Display start window
    StartWindow(viewModel = viewModel)

    // Button to center on device location
    Row(
        modifier = Modifier.align(Alignment.BottomCenter),
        horizontalArrangement = Arrangement.Center) {
          if (ui.myLocationButtonEnabled && properties.isMyLocationEnabled) {
            Box(modifier = Modifier.padding(horizontal = 0.dp, vertical = 60.dp)) {
              DisplayCenterLocationButton(
                  coroutineScope = coroutineScope,
                  deviceLocation = deviceLocation,
                  cameraPositionState = cameraPositionState)
            }
          }

          // Button to start/stop recording
          FilledTonalButton(
              onClick = {
                if (viewModel.isRecording()) {
                  viewModel.stopRecording()

                  // Add itinerary to database
                  val id = itineraryRepository.getUID()
                  val title = "TEST" // TODO : get title from user but not implemented yet
                  val username = "lomimi" // TODO : get username from user but not implemented yet
                  val location = Location(
                      deviceLocation.latitude,
                      deviceLocation.longitude,
                      "Device Location")
                    // TODO : get location from user but not implemented yet (default device location)
                  val flameCount = 0L
                  val startDate = viewModel.startDate.value
                  val endDate = viewModel.endDate.value
                  val pinList = emptyList<Pin>() // TODO : get pin list from user but not implemented yet
                  val description = "description" // TODO : get description from user but not implemented yet


                  val itinerary = Itinerary(
                      id,
                      title,
                      username,
                      location,
                      flameCount,
                      startDate,
                      endDate,
                      pinList,
                      description,
                      viewModel.latLongList.toList())

                  itineraryRepository.addNewItinerary(itinerary)

                  viewModel.resetRecording()
                  localLatLngList.clear()
                } else {
                  viewModel.startRecording()
                }
              },
              modifier = Modifier.padding(50.dp).fillMaxWidth(0.6f).fillMaxHeight(0.1f),
              colors =
                  ButtonDefaults.filledTonalButtonColors(
                      containerColor = md_theme_orange, contentColor = Color.White),
          ) {
            Text(
                text = if (viewModel.isRecording()) "Stop" else "Start",
                fontSize = 24.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Color.White)
          }
          Spacer(modifier = Modifier.width(50.dp))
        }
  }
}

/**
 * Composable function to display the start window.
 *
 * @param viewModel The RecordViewModel instance.
 */
@Composable
fun StartWindow(viewModel: RecordViewModel) {
  val timer = remember { mutableLongStateOf(0L) }

  // Update timer if recording
  if (viewModel.isRecording()) {
    LaunchedEffect(key1 = viewModel.isRecording()) {
      while (viewModel.isRecording()) {
        timer.longValue = viewModel.getElapsedTime()
        delay(1000)
      }
    }
  }

  // Display recording window if user is recording
  Column(
      modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(top = 20.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.SpaceBetween) {
        // animate the visibility of the recording window
        AnimatedVisibility(
            visible = viewModel.isRecording(),
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)) {
              // Display the timer
              Box(modifier = Modifier.fillMaxHeight(0.35f).fillMaxWidth()) {
                Box(
                    modifier =
                        Modifier.fillMaxWidth(0.9f)
                            .fillMaxHeight(0.5f)
                            .background(md_theme_light_dark, shape = RoundedCornerShape(35.dp))
                            .align(Alignment.Center)) {
                      Row(
                          modifier = Modifier.fillMaxSize(),
                          horizontalArrangement = Arrangement.SpaceEvenly) {
                            Text(
                                text = "TIME",
                                modifier = Modifier.align(Alignment.CenterVertically),
                                fontSize = 22.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White)
                            Text(
                                text = displayTime(timer.longValue),
                                modifier = Modifier.align(Alignment.CenterVertically),
                                fontSize = 22.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White)
                          }
                    }
              }
            }
        // animate the visibility of the pause/resume button
        AnimatedVisibility(
            visible = viewModel.isRecording(),
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)) {
              // Display the pause/resume button and add spot button
              Box(modifier = Modifier.fillMaxHeight(0.5f).fillMaxWidth()) {
                Box(
                    modifier =
                        Modifier.fillMaxWidth(0.9f)
                            .fillMaxHeight(0.45f)
                            .background(md_theme_light_dark, shape = RoundedCornerShape(35.dp))
                            .align(Alignment.Center)) {
                      Row(
                          modifier =
                              Modifier.fillMaxSize()
                                  .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                          horizontalArrangement = Arrangement.SpaceBetween) {
                            FilledTonalButton(
                                onClick = {
                                  if (viewModel.isPaused.value) {
                                    viewModel.resumeRecording()
                                  } else {
                                    viewModel.pauseRecording()
                                  }
                                },
                                modifier =
                                    Modifier.align(Alignment.CenterVertically).fillMaxHeight(0.6f),
                                colors =
                                    ButtonDefaults.filledTonalButtonColors(
                                        containerColor = Color.White,
                                        contentColor = md_theme_light_dark),
                            ) {
                              Text(
                                  text = if (viewModel.isPaused.value) "Resume" else "Pause",
                                  fontSize = 14.sp,
                                  fontFamily = Montserrat,
                                  fontWeight = FontWeight.SemiBold,
                                  color = md_theme_light_dark)
                            }
                            Row(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                horizontalArrangement = Arrangement.SpaceEvenly) {
                                  Text(
                                      text = "Add spot",
                                      modifier = Modifier.align(Alignment.CenterVertically),
                                      fontSize = 14.sp,
                                      fontFamily = Montserrat,
                                      fontWeight = FontWeight.SemiBold,
                                      color = md_theme_grey)
                                  Spacer(modifier = Modifier.width(20.dp))
                                  IconButton(
                                      onClick = { /*TODO*/},
                                      modifier =
                                          Modifier.align(Alignment.CenterVertically)
                                              .size(50.dp)
                                              .background(Color.White, shape = CircleShape),
                                  ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "Add spot",
                                        tint = md_theme_light_dark,
                                        modifier = Modifier.size(30.dp))
                                  }
                                }
                          }
                    }
              }
            }
      }
}

/**
 * Function to display the time in a readable format.
 *
 * @param time The time in milliseconds.
 * @return A formatted time string.
 */
fun displayTime(time: Long): String {
  val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
  val minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60
  val hours = TimeUnit.MILLISECONDS.toHours(time)
  if(seconds < 0 || minutes < 0 || hours < 0) {
    Log.e("RecordScreen", "Negative time")
    return "Error"
  }
  return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/** Function to preview the RecordScreen */
@Preview
@Composable
fun RecordScreenPreview() {
  val context = LocalContext.current
  val navController = rememberNavController()
  val navigation = remember(navController) { Navigation(navController) }
  RecordScreen(context, navigation)
}
