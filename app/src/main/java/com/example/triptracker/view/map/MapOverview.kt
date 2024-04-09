package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.triptracker.navigation.AllowLocationPermission
import com.example.triptracker.navigation.checkForLocationPermission
import com.example.triptracker.navigation.getCurrentLocation
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
/**
 * Composable displaying the map overview with all the paths and markers of trips that are around
 * the user's location. Needs the context of the app in order to ask for location permission and
 * access location.
 *
 * @param mapViewModel: The view model of the map
 * @param context: The context of the app (needed for location permission and real time location)
 */
fun MapOverview(
    mapViewModel: MapViewModel = MapViewModel(),
    context: Context,
    navigation: Navigation
) {
  // The device location is set to EPFL by default
  var deviceLocation = DEFAULT_LOCATION
  var mapProperties =
      MapProperties(
          mapType = MapType.NORMAL, isMyLocationEnabled = checkForLocationPermission(context))
  var uiSettings = MapUiSettings(myLocationButtonEnabled = checkForLocationPermission(context))

  getCurrentLocation(context = context, onLocationFetched = { deviceLocation = it })

  // Check if the location permission is granted if not re-ask for it. If the result is still
  // negative then disable the location button and center the view on EPFL
  // else enable the location button and center the view on the user's location and show real time
  // location
  when (checkForLocationPermission(context = context)) {
    true -> {
      Scaffold(
          bottomBar = { NavigationBar(navigation) }, modifier = Modifier.testTag("MapOverview")) {
              innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
              Map(mapViewModel, context, deviceLocation, mapProperties, uiSettings)
            }
          }
    }
    false -> {
      Scaffold(
          bottomBar = { NavigationBar(navigation) }, modifier = Modifier.testTag("MapOverview")) {
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
    uiSettings: MapUiSettings
) {
  // Used to display the gradient with the top bar and the changing city location
  val ui by remember { mutableStateOf(uiSettings) }
  val properties by remember { mutableStateOf(mapProperties) }
  var deviceLocation by remember { mutableStateOf(startLocation) }
  val coroutineScope = rememberCoroutineScope()
  var visibleRegion: VisibleRegion?

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(deviceLocation, 17f)
  }

  // When the camera is moving, the city name is updated in the top bar with geo decoding
  LaunchedEffect(cameraPositionState.isMoving) {
    mapViewModel.reverseDecode(
        cameraPositionState.position.target.latitude.toFloat(),
        cameraPositionState.position.target.longitude.toFloat())
    // fetch the new paths from the DB
    visibleRegion = cameraPositionState.projection?.visibleRegion
    Log.d("Map", "Visible region: $visibleRegion")
  }

  // Fetch the device location when the composable is launched
  LaunchedEffect(Unit) {
    getCurrentLocation(
        context = context,
        onLocationFetched = {
          deviceLocation = it
          cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation, 17f)
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
                          Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black))),
          cameraPositionState = cameraPositionState,
          properties = properties,
          uiSettings = ui,
      ) {
        mapViewModel.getAllPaths().forEach { (location, latLngList) ->
          Polyline(
              points = latLngList,
              clickable = true,
              color = md_theme_orange,
              width = 15f,
              onClick = { mapViewModel.cityNameState.value = location })
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
          if (ui.myLocationButtonEnabled && properties.isMyLocationEnabled) {
            Box(modifier = Modifier.padding(horizontal = 35.dp, vertical = 65.dp)) {
              DisplayCenterLocationButton(
                  coroutineScope = coroutineScope,
                  deviceLocation = deviceLocation,
                  cameraPositionState = cameraPositionState)
            }
          }
        }
  }
}

@Preview
@Composable
// Start this screen to only see the overview of the map
fun MapOverviewPreview() {
  val context = LocalContext.current
  val navController = rememberNavController()
  val navigation = remember(navController) { Navigation(navController) }
  MapOverview(MapViewModel(), context, navigation)
}
