package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
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
import coil.compose.AsyncImage
import com.example.triptracker.model.location.popupState
import com.example.triptracker.navigation.AllowLocationPermission
import com.example.triptracker.navigation.checkForLocationPermission
import com.example.triptracker.navigation.getCurrentLocation
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.home.DisplayItinerary
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

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
    mapViewModel: MapViewModel = MapViewModel(),
    context: Context,
    navigation: Navigation,
    checkLocationPermission: Boolean = true, // Default value true, can be overridden during tests
    startLocation: LatLng = DEFAULT_LOCATION
) {
  // The device location is set to EPFL by default
  var deviceLocation = startLocation
  var mapProperties by remember {
    mutableStateOf(
        MapProperties(
            mapType = MapType.NORMAL, isMyLocationEnabled = checkForLocationPermission(context)))
  }

  var uiSettings by remember {
    mutableStateOf(MapUiSettings(myLocationButtonEnabled = checkForLocationPermission(context)))
  }

  getCurrentLocation(
      context = context,
      onLocationFetched = {
        if (startLocation == DEFAULT_LOCATION) {
          deviceLocation = it
        }
      })

  var loadMapScreen by remember {
    mutableStateOf(if (checkLocationPermission) checkForLocationPermission(context) else false)
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
              Map(mapViewModel, context, deviceLocation, mapProperties, uiSettings, navigation)
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
 * @param startLocation: The starting location of the map
 * @param mapProperties: The properties of the map (type, location enabled)
 * @param uiSettings: The settings of the map (location button enabled)
 */
@Composable
fun Map(
    mapViewModel: MapViewModel,
    context: Context,
    startLocation: LatLng,
    mapProperties: MapProperties,
    uiSettings: MapUiSettings,
    navigation: Navigation
) {
  // Used to display the gradient with the top bar and the changing city location
  val ui by remember { mutableStateOf(uiSettings) }
  val properties by remember { mutableStateOf(mapProperties) }
  var deviceLocation by remember { mutableStateOf(startLocation) }
  val coroutineScope = rememberCoroutineScope()

  var displayPopUp by remember { mutableStateOf(false) }
  var displayPicturesPopUp by remember { mutableStateOf(false) }
  var mapPopupState by remember { mutableStateOf(popupState.DISPLAYITINERARY) }

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(deviceLocation, 17f)
  }
  var visibleRegion: VisibleRegion?

  var displayPopUp by remember { mutableStateOf(mapViewModel.displayPopUp.value) }
  var displayPicturesPopUp by remember { mutableStateOf(mapViewModel.displayPicturesPopUp.value) }

  // When the camera is moving, the city name is updated in the top bar with geo decoding
  LaunchedEffect(cameraPositionState.isMoving) {
    mapViewModel.reverseDecode(
        cameraPositionState.position.target.latitude.toFloat(),
        cameraPositionState.position.target.longitude.toFloat())
    // Get the visible region of the map
    visibleRegion = cameraPositionState.projection?.visibleRegion
    // Get the filtered paths based on the visible region of the map asynchronously
    mapViewModel.getFilteredPaths(visibleRegion?.latLngBounds)
    //      Log.d("MAP_VISIBLE_REGION", visibleRegion?.latLngBounds.toString())
    //      Log.d("MAP_FILTERED_PATHS",  mapViewModel.filteredPathList.value.toString())
  }

  // Fetch the device location when the composable is launched
  LaunchedEffect(Unit) {
    getCurrentLocation(
        context = context,
        onLocationFetched = {
          if (startLocation == DEFAULT_LOCATION) {
            deviceLocation = it
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation, 17f)
          }
        })
  }

  // Displays the map
  Box() {
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
          onMapClick = { it ->
            displayPopUp = false
            mapPopupState = popupState.DISPLAYITINERARY
            displayPicturesPopUp = false
            mapViewModel.selectedPolylineState.value = null
          },
      ) {
        // Display the path of the trips on the map only when they enter the screen
        mapViewModel.filteredPathList.value?.forEach { (location, latLngList) ->
          // Check if the polyline is selected
          val isSelected by
              rememberUpdatedState(
                  newValue = mapViewModel.selectedPolylineState.value?.itinerary?.id == location.id)
          // Display the pat polyline
          Polyline(
              points = latLngList,
              clickable = true,
              color = md_theme_orange,
              width = if (isSelected) 25f else 15f,
              onClick = {
                mapViewModel.selectedPolylineState.value =
                    MapViewModel.SelectedPolyline(location, latLngList[0])
                mapPopupState = popupState.DISPLAYITINERARY
                displayPopUp = true
              })

          // Display the start marker of the polyline and a thicker path when selected
          if (isSelected) {
            val startMarkerState =
                rememberMarkerState(
                    position = mapViewModel.selectedPolylineState.value!!.startLocation)
            MarkerComposable(state = startMarkerState) {
              Icon(
                  imageVector = Icons.Outlined.Accessibility,
                  contentDescription = "Start Location",
                  tint = md_theme_orange)
            }
            mapViewModel.selectedPolylineState.value?.itinerary?.pinnedPlaces?.forEach { pin ->
              val markerState = rememberMarkerState(position = LatLng(pin.latitude, pin.longitude))
              MarkerComposable(
                  state = markerState,
                  onClick = { marker ->
                    // Display the pin information
                    mapViewModel.selectedPin.value = pin
                    if (mapPopupState == popupState.PATHOVERLAY) {
                      displayPicturesPopUp = true
                    }
                    displayPopUp = false

                    false
                  }) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Add Picture",
                        tint = md_theme_orange)
                  }
            }
          }
        }
      }
    }

    Box(modifier = Modifier.matchParentSize().background(gradient).align(Alignment.TopCenter)) {
      Text(
          text = mapViewModel.cityNameState.value,
          modifier = Modifier.padding(30.dp).align(Alignment.TopCenter),
          fontSize = 24.sp,
          fontFamily = Montserrat,
          fontWeight = FontWeight.SemiBold,
          color = md_theme_light_dark)
    }
    Row(
        modifier = Modifier.align(Alignment.BottomStart),
        horizontalArrangement = Arrangement.Start) {
          Box(modifier = Modifier.padding(horizontal = 35.dp, vertical = 65.dp)) {
            if (ui.myLocationButtonEnabled &&
                properties.isMyLocationEnabled &&
                !displayPopUp &&
                !displayPicturesPopUp) {
              DisplayCenterLocationButton(
                  coroutineScope = coroutineScope,
                  deviceLocation = deviceLocation,
                  cameraPositionState = cameraPositionState) {
                    getCurrentLocation(
                        context = context,
                        onLocationFetched = {
                          deviceLocation = it
                          cameraPositionState.position =
                              CameraPosition.fromLatLngZoom(deviceLocation, 17f)
                        })
                  }
            }
            if (displayPopUp) {
              Box(modifier = Modifier.fillMaxHeight(0.3f)) {
                if (mapViewModel.selectedPolylineState.value != null) {
                  // Display the itinerary of the selected polyline
                  // (only when the polyline is selected)
                  when (mapPopupState) {
                    popupState.DISPLAYITINERARY -> {
                      DisplayItinerary(
                          itinerary = mapViewModel.selectedPolylineState.value!!.itinerary,
                          navigation = navigation,
                          onClick = { mapPopupState = popupState.PATHOVERLAY })
                    }
                    popupState.DISPLAYPIN -> {
                      // called detailed view Theo
                    }
                    popupState.PATHOVERLAY -> {
                      PathOverlaySheet(
                          itinerary = mapViewModel.selectedPolylineState.value!!.itinerary)
                    }
                  }
                }
              }
            }

            if (displayPicturesPopUp) {
              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .padding(15.dp)
                          .height(300.dp)
                          .background(
                              color = md_theme_light_black, shape = RoundedCornerShape(35.dp))) {
                    // Display the pictures of the selected pin
                    // (only when the pin is selected)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {
                          Column(
                              modifier = Modifier.fillMaxWidth(),
                              verticalArrangement = Arrangement.Center,
                              horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = mapViewModel.selectedPin.value?.name ?: "",
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    fontSize = 15.sp,
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight.SemiBold,
                                    color = md_theme_orange)
                                Text(
                                    text = mapViewModel.selectedPin.value?.description ?: "",
                                    fontSize = 12.sp,
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight.SemiBold,
                                    color = md_theme_light_onPrimary)
                              }
                        }

                    val selectedPin = mapViewModel.selectedPin.value
                    val scrollState = rememberScrollState()

                    Row(
                        modifier =
                            Modifier.fillMaxSize()
                                .horizontalScroll(scrollState)
                                .padding(vertical = 20.dp, horizontal = 20.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom) {
                          selectedPin?.image_url?.forEach { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = "Image",
                                modifier = Modifier.height(200.dp).padding(horizontal = 2.dp))
                          }
                        }
                  }
            }
          }
        }
  }
}
/*
@Composable
fun ManagePopups(itinerary: Itinerary, navigation: Navigation, mapPopupState: popupState) {
  when (mapPopupState) {
    popupState.DISPLAYITINERARY -> {
      DisplayItinerary(itinerary = itinerary, navigation = navigation, onClick = { mapPopupState = popupState.PATHOVERLAY})
    }
    popupState.DISPLAYPIN -> {
      // called detailed view Theo
    }
    popupState.PATHOVERLAY -> {
      PathOverlaySheet(itinerary = itinerary)
    }
    popupState.NONE -> {
      // do nothing
    }
  }
}
*/
@Preview
@Composable
// Start this screen to only see the overview of the map
fun MapOverviewPreview() {
  val context = LocalContext.current
  val navController = rememberNavController()
  val navigation = remember(navController) { Navigation(navController) }
  MapOverview(MapViewModel(), context, navigation)
}
