package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.location.PopUpStatus
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.navigation.AllowLocationPermission
import com.example.triptracker.navigation.checkForLocationPermission
import com.example.triptracker.navigation.getCurrentLocation
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.home.DisplayItinerary
import com.example.triptracker.view.home.allProfilesFetched
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_light_outline
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.MapViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
/**
 * Composable displaying the map overview with all the paths and markers of trips that are around
 * the user's location. Needs the context of the app in order to ask for location permission and
 * access location.
 *
 * @param mapViewModel: The view model of the map (optional)
 * @param context: The context of the app (needed for location permission and real time location)
 * @param navigation: The app navigation instance
 * @param checkLocationPermission: Boolean whether to check or not the location permission for tests
 *   purposes (optional)
 */
fun MapOverview(
    mapViewModel: MapViewModel = viewModel(),
    context: Context,
    navigation: Navigation,
    checkLocationPermission: Boolean = true, // Default value true, can be overridden during tests
    selectedId: String = "",
    userProfile: MutableUserProfile
) {
  var mapProperties by remember {
    mutableStateOf(
        MapProperties(
            mapType = MapType.NORMAL, isMyLocationEnabled = checkForLocationPermission(context)))
  }

  var uiSettings by remember {
    mutableStateOf(MapUiSettings(myLocationButtonEnabled = checkForLocationPermission(context)))
  }

  var loadMapScreen by remember {
    mutableStateOf(checkLocationPermission && checkForLocationPermission(context))
  }

  // Check if the location permission is granted if not re-ask for it. If the result is still
  // negative then disable the location button and center the view on EPFL
  // else enable the location button and center the view on the user's location and show real time
  // location
  when (loadMapScreen) {
    true -> {
      Scaffold(
          bottomBar = { NavigationBar(navigation) }, modifier = Modifier.testTag("MapOverview")) {
              innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
              Map(
                  mapViewModel,
                  context,
                  mapProperties,
                  uiSettings,
                  selectedId,
                  userProfile,
                  navigation)
            }
          }
    }
    false -> {
      AllowLocationPermission(
          onPermissionGranted = {
            mapProperties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = true)
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
            loadMapScreen = true
          },
          onPermissionDenied = {
            mapProperties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = false)
            uiSettings = MapUiSettings(myLocationButtonEnabled = false)
            loadMapScreen = true
          })
    }
  }
}

/**
 * Composable displaying the map with the user's location and the city name on the top bar.
 *
 * @param mapViewModel: The view model of the map
 * @param context: The context of the app (needed for location permission and real time location)
 * @param mapProperties: The properties of the map (type, location enabled)
 * @param uiSettings: The settings of the map (location button enabled)
 * @param currentSelectedId: The id of the selected path
 */
@Composable
fun Map(
    mapViewModel: MapViewModel,
    context: Context,
    mapProperties: MapProperties,
    uiSettings: MapUiSettings,
    currentSelectedId: String,
    userProfile: MutableUserProfile,
    navigation: Navigation,
    userProfileViewModel: UserProfileViewModel = viewModel()
) {
  // Used to display the gradient with the top bar and the changing city location
  val ui by remember { mutableStateOf(uiSettings) }
  val properties by remember { mutableStateOf(mapProperties) }
  val coroutineScope = rememberCoroutineScope()

  // var mapPopupState by remember { mutableStateOf(mapViewModel.popUpState) }
  val pathList by mapViewModel.pathList.observeAsState()

  val deviceLocation = remember { mutableStateOf(DEFAULT_LOCATION) }

  val cameraPositionState = rememberCameraPositionState {
    doWhenCurrentSelectedIdIsEmpty(currentSelectedId) {
      getCurrentLocation(
          context = context,
          onLocationFetched = {
            deviceLocation.value = it
            position = CameraPosition.fromLatLngZoom(deviceLocation.value, 17f)
          })
    }
  }

  var visibleRegion: VisibleRegion?

  var selectedPolyline by remember { mapViewModel.selectedPolylineState }

  val showCancelDialog = remember { mutableStateOf(false) }

  // When the camera is moving, the city name is updated in the top bar with geo decoding
  LaunchedEffect(cameraPositionState.isMoving) {
    mapViewModel.reverseDecode(
        cameraPositionState.position.target.latitude.toFloat(),
        cameraPositionState.position.target.longitude.toFloat())
    // Get the visible region of the map
    visibleRegion = cameraPositionState.projection?.visibleRegion
    // Get the filtered paths based on the visible region of the map asynchronously
    mapViewModel.getFilteredPaths(visibleRegion?.latLngBounds)
  }

  // Fetch the device location when the composable is launched
  LaunchedEffect(Unit) {
    getCurrentLocation(
        context = context,
        onLocationFetched = {
          doWhenCurrentSelectedIdIsEmpty(currentSelectedId) {
            deviceLocation.value = it
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation.value, 17f)
          }
        })
  }

  // Get the path list from the view model and trigger the launch effect when the path list is
  // updated
  LaunchedEffect(pathList) {
    doWhenCurrentSelectedIdIsNotEmpty(currentSelectedId) {
      // fetch the selected path
      Log.d("MapOverviewPRINT", pathList?.size().toString())
      doWhenPathListIsNotNull(pathList) {
        val selection = mapViewModel.getPathById(pathList!!, currentSelectedId)
        doWhenSelectionIsNotNull(selection) {
          // center the camera on the selected path
          cameraPositionState.position =
              CameraPosition.fromLatLngZoom(
                  LatLng(selection!!.location.latitude, selection.location.longitude), 17f)

          // set the selected polyline
          mapViewModel.selectedPolylineState.value =
              MapViewModel.SelectedPolyline(selection, selection.route[0])
          selectedPolyline = mapViewModel.selectedPolylineState.value

          // display the path popup
          mapViewModel.displayPopUp.value = true
        }
      }
    }
  }

  // Displays the map
  Box(modifier = Modifier.fillMaxSize()) {
    Box(modifier = Modifier.fillMaxSize()) {
      GoogleMap(
          modifier =
              Modifier.matchParentSize()
                  .background(
                      brush =
                          Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black)))
                  .testTag("Map"),
          cameraPositionState = cameraPositionState,
          properties = properties,
          uiSettings = ui,
          onMapClick = {
            doOnMapClick(
                mapViewModel.asStartItinerary.value,
                mapViewModel.displayPicturePopUp.value,
                callbackIfNullStartItinerary = {
                  selectedPolyline = mapViewModel.DUMMY_SELECTED_POLYLINE
                  mapViewModel.displayPopUp.value = false
                  mapViewModel.displayPicturePopUp.value = false
                },
                callbackIfDisplayPicturePopUp = {
                  mapViewModel.displayPicturePopUp.value = false
                  mapViewModel.displayPopUp.value = true
                  mapViewModel.popUpState.value = PopUpStatus.PATH_OVERLAY
                })
          },
          onMapLoaded = {
            // decode the city name and update the top bar
            mapViewModel.reverseDecode(
                cameraPositionState.position.target.latitude.toFloat(),
                cameraPositionState.position.target.longitude.toFloat())
            visibleRegion = cameraPositionState.projection?.visibleRegion
            mapViewModel.getFilteredPaths(visibleRegion?.latLngBounds)
          }) {
            // Display the path of the trips on the map only when they enter the screen
            var itsNotPrivacy = mapViewModel.filteredPathList.value ?: emptyMap()

            if (itsNotPrivacy.isNotEmpty()) {
              itsNotPrivacy =
                  itsNotPrivacy.filter { (k, v) ->
                    val itin = k
                    val ownerProfile = allProfilesFetched.find { it.mail == itin.userMail }
                    if (ownerProfile != null) {
                      ownerProfile.itineraryPrivacy == 0 ||
                          ownerProfile == userProfile.userProfile.value ||
                          (ownerProfile.itineraryPrivacy == 1 &&
                              userProfile.userProfile.value.followers.contains(ownerProfile.mail) &&
                              userProfile.userProfile.value.following.contains(ownerProfile.mail))
                    } else {
                      false
                    }
                  }
            }
            itsNotPrivacy.forEach { (location, latLngList) ->
              // Check if the polyline is selected
              val isSelected = selectedPolyline?.itinerary?.id == location.id
              val width = if (isSelected) 25f else 15f
              // Display the pat polyline
              Polyline(
                  points = latLngList,
                  clickable = true,
                  color = md_theme_orange,
                  width = width,
                  onClick = {
                    onSelectedPolyline(mapViewModel) {
                      selectedPolyline = MapViewModel.SelectedPolyline(location, latLngList[0])
                      mapViewModel.popUpState.value = PopUpStatus.DISPLAY_ITINERARY
                      mapViewModel.displayPopUp.value = true
                    }
                  })

              DisplayPins(
                  isSelected = isSelected,
                  selectedPolyline = selectedPolyline,
              ) { pin ->
                mapViewModel.selectedPin.value = pin

                mapViewModel.displayPicturePopUp.value = true

                mapViewModel.displayPopUp.value = false
              }
            }
          }
    }
    if (showCancelDialog.value) {
      AlertDialog(
          shape = RoundedCornerShape(15.dp),
          modifier = Modifier.align(Alignment.Center),
          onDismissRequest = { showCancelDialog.value = false },
          title = {
            Text(
                text = "Cancel Itinerary",
                fontSize = 20.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground)
          },
          text = {
            Text(
                text = "Are you sure you want to cancel the itinerary?",
                fontSize = 16.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                color = md_theme_light_outline)
          },
          confirmButton = {
            Button(
                modifier = Modifier.testTag("YesCancelItineraryButton"),
                onClick = {
                  mapViewModel.asStartItinerary.value = false
                  mapViewModel.displayPopUp.value = true
                  mapViewModel.displayPicturePopUp.value = false
                  mapViewModel.popUpState.value = PopUpStatus.DISPLAY_ITINERARY
                  showCancelDialog.value = false
                },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = md_theme_orange, contentColor = md_theme_light_onPrimary),
                shape = RoundedCornerShape(35.dp),
            ) {
              Text(
                  text = "Yes",
                  fontSize = 14.sp,
                  fontFamily = Montserrat,
                  fontWeight = FontWeight.SemiBold,
                  color = md_theme_light_onPrimary)
            }
          },
          dismissButton = {
            Button(
                modifier = Modifier.testTag("NoCancelItineraryButton"),
                onClick = { showCancelDialog.value = false },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = md_theme_orange, contentColor = Color.White),
                shape = RoundedCornerShape(35.dp)) {
                  Text(
                      text = "No",
                      fontSize = 14.sp,
                      fontFamily = Montserrat,
                      fontWeight = FontWeight.SemiBold,
                      color = md_theme_light_onPrimary)
                }
          })
    }

    Box(modifier = Modifier.matchParentSize().background(gradient).align(Alignment.TopCenter)) {
      Row(
          modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().padding(10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween) {
            DisplayCancelItineraryButton(mapViewModel, showCancelDialog)

            Text(
                text = mapViewModel.cityNameState.value,
                modifier = Modifier.padding(30.dp),
                fontSize = 24.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = md_theme_light_dark)
            Spacer(modifier = Modifier.width(50.dp))
          }
    }
  }
  Row(
      modifier = Modifier.fillMaxWidth().fillMaxHeight(),
      horizontalArrangement = Arrangement.Start) {
        Box(
            modifier =
                Modifier.padding(horizontal = 35.dp, vertical = 65.dp).align(Alignment.Bottom)) {
              DisplayCenterLocationButtonIfNeeded(
                  ui = ui,
                  properties = properties,
                  mapViewModel = mapViewModel,
                  coroutineScope = coroutineScope,
                  cameraPositionState = cameraPositionState,
                  context = context,
                  deviceLocation = deviceLocation)
            }
      }

  if (mapViewModel.displayPopUp.value && mapViewModel.selectedPolylineState.value != null) {
    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
      // Display the itinerary of the selected polyline
      // (only when the polyline is selected)
      when (mapViewModel.popUpState.value) {
        PopUpStatus.DISPLAY_ITINERARY -> {
          Box(
              modifier =
                  Modifier.fillMaxHeight(0.3f).fillMaxWidth().align(Alignment.BottomCenter)) {
                DisplayItinerary(
                    itinerary = mapViewModel.selectedPolylineState.value!!.itinerary,
                    onClick = { mapViewModel.popUpState.value = PopUpStatus.DISPLAY_PIN },
                    navigation = navigation,
                )
              }
        }
        PopUpStatus.DISPLAY_PIN -> {
          Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
            StartScreen(
                itinerary = mapViewModel.selectedPolylineState.value!!.itinerary,
                userProfileViewModel = UserProfileViewModel(),
                userProfile = userProfile,
                onClick = { mapViewModel.popUpState.value = PopUpStatus.PATH_OVERLAY },
                mapViewModel = mapViewModel)
          }
        }
        PopUpStatus.PATH_OVERLAY -> {
          Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            PathOverlaySheet(
                itinerary = mapViewModel.selectedPolylineState.value!!.itinerary,
                onClick = {
                  mapViewModel.popUpState.value = PopUpStatus.DISPLAY_ITINERARY
                  mapViewModel.displayPopUp.value = false
                  mapViewModel.displayPicturePopUp.value = true
                  mapViewModel.selectedPin.value = it
                })
          }
        }
      }
    }
  }
  Box(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
    AnimatedVisibility(
        visible = mapViewModel.displayPicturePopUp.value,
        enter =
            fadeIn() + expandVertically(expandFrom = Alignment.Bottom, animationSpec = tween(100)),
        exit =
            fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(100)),
        modifier = Modifier.align(Alignment.BottomCenter)) {
          Box(
              modifier =
                  Modifier.fillMaxWidth()
                      .fillMaxHeight(0.4f)
                      .align(Alignment.BottomCenter)
                      .background(
                          color = md_theme_light_black,
                          shape = RoundedCornerShape(topEnd = 35.dp, topStart = 35.dp))) {
                // Display the pictures of the selected pin
                // (only when the pin is selected)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                      Column(
                          modifier =
                              Modifier.fillMaxWidth()
                                  .padding(start = 20.dp, top = 5.dp)
                                  .verticalScroll(rememberScrollState()),
                          verticalArrangement = Arrangement.Center,
                          horizontalAlignment = Alignment.Start) {
                            Text(
                                text = mapViewModel.selectedPin.value?.name ?: "",
                                fontSize = 20.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                color = md_theme_light_onPrimary)
                            Text(
                                text = mapViewModel.selectedPin.value?.description ?: "",
                                fontSize = 12.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Normal,
                                color = md_theme_light_outline)
                          }
                    }

                val selectedPin = mapViewModel.selectedPin.value
                val scrollState = rememberScrollState()

                Row(
                    modifier =
                        Modifier.fillMaxSize()
                            .horizontalScroll(scrollState)
                            .align(Alignment.BottomStart)
                            .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom) {
                      DisplayPinImages(selectedPin)
                    }
              }
        }
  }
}

private fun onSelectedPolyline(mapViewModel: MapViewModel, callback: () -> Unit) {
  if (!mapViewModel.asStartItinerary.value) {
    callback()
  }
}

@Composable
private fun DisplayPins(
    isSelected: Boolean,
    selectedPolyline: MapViewModel.SelectedPolyline?,
    callback: (pin: Pin) -> Unit
) {
  // Display the start marker of the polyline and a thicker path when selected
  if (isSelected && selectedPolyline!!.itinerary.route.isNotEmpty()) {
    val startMarkerState = rememberMarkerState(position = selectedPolyline.itinerary.route[0])
    MarkerComposable(state = startMarkerState) {
      Icon(
          imageVector = Icons.Outlined.ArrowDownward,
          contentDescription = "Start Location",
          tint = md_theme_light_black)
    }

    selectedPolyline.itinerary.pinnedPlaces.forEach { pin ->
      val markerState = rememberMarkerState(position = LatLng(pin.latitude, pin.longitude))
      MarkerComposable(
          state = markerState,
          onClick = {
            // Display the pin information
            callback(pin)
            true
          }) {
            Icon(
                imageVector = Icons.Outlined.PinDrop,
                contentDescription = "Add Picture",
                tint = md_theme_light_black)
          }
    }
  }
}

@Composable
fun DisplayCancelItineraryButton(
    mapViewModel: MapViewModel,
    showCancelDialog: MutableState<Boolean>
) {
  Log.d("TEST", showCancelDialog.value.toString())
  Log.d("TEST", mapViewModel.asStartItinerary.value.toString())
  if (mapViewModel.asStartItinerary.value) {
    IconButton(
        onClick = { showCancelDialog.value = true },
        modifier = Modifier.testTag("CancelItineraryButton").size(50.dp)) {
          Icon(
              imageVector = Icons.Outlined.Close,
              contentDescription = "Cancel Itinerary",
              tint = md_theme_light_dark)
        }
  } else {
    Spacer(modifier = Modifier.width(50.dp))
  }
}

/**
 * Composable that decides whether to display the center location button on the map or not.
 *
 * @param ui: The UI settings of the map
 * @param properties: The properties of the map
 * @param mapViewModel: The view model of the map
 * @param coroutineScope: The coroutine scope
 * @param cameraPositionState: The state of the camera position
 * @param context: The context of the app
 * @param deviceLocation: The location of the device
 */
@Composable
fun DisplayCenterLocationButtonIfNeeded(
    ui: MapUiSettings,
    properties: MapProperties,
    mapViewModel: MapViewModel,
    coroutineScope: CoroutineScope,
    cameraPositionState: CameraPositionState,
    context: Context,
    deviceLocation: MutableState<LatLng>
) {
  if (ui.myLocationButtonEnabled &&
      properties.isMyLocationEnabled &&
      !mapViewModel.displayPopUp.value &&
      !mapViewModel.displayPicturePopUp.value) {
    DisplayCenterLocationButton(
        coroutineScope = coroutineScope,
        deviceLocation = deviceLocation.value,
        cameraPositionState = cameraPositionState) {
          getCurrentLocation(
              context = context,
              onLocationFetched = {
                deviceLocation.value = it
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(deviceLocation.value, 17f)
              })
        }
  }
}

/**
 * Composable that handles the display of the images of a pin
 *
 * @param selectedPin: The selected pin
 */
@Composable
fun DisplayPinImages(selectedPin: Pin?) {
  if (selectedPin?.imageUrl?.isEmpty() == true) {
    Log.e("MapOverview", "No images available")
    Text(
        text = "No images available",
        fontSize = 20.sp,
        fontFamily = Montserrat,
        fontWeight = FontWeight.Light,
        color = md_theme_grey)
  } else {
    selectedPin?.imageUrl?.forEach { url ->
      AsyncImage(
          modifier =
              Modifier.clip(shape = RoundedCornerShape(20.dp))
                  .height(200.dp)
                  .padding(horizontal = 10.dp),
          model = url,
          contentDescription = "Image",
      )
    }
  }
}

/**
 * Helper functions for complexity that checks if values are empty or null and accordingly call for
 * callback.
 */
fun doWhenCurrentSelectedIdIsEmpty(currentSelectedId: String, callback: () -> Unit) {
  if (currentSelectedId == "") {
    callback()
  }
}

fun doWhenCurrentSelectedIdIsNotEmpty(currentSelectedId: String, callback: () -> Unit) {
  if (currentSelectedId != "") {
    callback()
  }
}

fun doWhenPathListIsNotNull(pathList: ItineraryList?, callback: () -> Unit) {
  if (pathList != null) {
    callback()
  }
}

fun doWhenSelectionIsNotNull(selection: Itinerary?, callback: () -> Unit) {
  if (selection != null) {
    callback()
  }
}

fun doOnMapClick(
    startItinerary: Boolean,
    displayPicturePopUp: Boolean,
    callbackIfNullStartItinerary: () -> Unit,
    callbackIfDisplayPicturePopUp: () -> Unit
) {
  if (!startItinerary) {
    callbackIfNullStartItinerary()
  } else {
    if (displayPicturePopUp) {
      callbackIfDisplayPicturePopUp()
    }
  }
}
