package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.R
import com.example.triptracker.navigation.AllowLocationPermission
import com.example.triptracker.navigation.checkForLocationPermission
import com.example.triptracker.navigation.getCurrentLocation
import com.example.triptracker.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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
) {

  // The device location is set to EPFL by default
  var deviceLocation = LatLng(46.519962, 6.633597)
  var mapProperties =
      MapProperties(
          mapType = MapType.SATELLITE, isMyLocationEnabled = checkForLocationPermission(context))
  var uiSettings = MapUiSettings(myLocationButtonEnabled = checkForLocationPermission(context))

  getCurrentLocation(context = context, onLocationFetched = { deviceLocation = it })

  // Check if the location permission is granted if not re-ask for it. If the result is still
  // negative then disable the location button and center the view on EPFL
  // else enable the location button and center the view on the user's location and show real time
  // location
  when (checkForLocationPermission(context = context)) {
    true -> {
      Map(mapViewModel, context, deviceLocation, mapProperties, uiSettings)
    }
    false -> {
      AllowLocationPermission(
          onPermissionGranted = {
            mapProperties = MapProperties(mapType = MapType.SATELLITE, isMyLocationEnabled = true)
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
          },
          onPermissionDenied = {
            mapProperties = MapProperties(mapType = MapType.SATELLITE, isMyLocationEnabled = false)
            uiSettings = MapUiSettings(myLocationButtonEnabled = false)
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
    uiSettings: MapUiSettings
) {
  // Used to display the gradient with the top bar and the changing city location
  val ui by remember { mutableStateOf(uiSettings) }
  val properties by remember { mutableStateOf(mapProperties) }
  var deviceLocation by remember { mutableStateOf(startLocation) }

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(deviceLocation, 16f)
  }

  // When the camera is moving, the city name is updated in the top bar with geo decoding
  LaunchedEffect(cameraPositionState.isMoving) {
    Log.d("CAMERA_STATE", "Camera is moving")
    mapViewModel.reverseDecode(
        cameraPositionState.position.target.latitude.toFloat(),
        cameraPositionState.position.target.longitude.toFloat())
    Log.d(
        "LAT_LON",
        "${cameraPositionState.position.target.latitude} and ${cameraPositionState.position.target.longitude}")
  }

  // Fetch the device location when the composable is launched
  LaunchedEffect(Unit) {
    getCurrentLocation(
        context = context,
        onLocationFetched = {
          deviceLocation = it
          cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation, 16f)
        })
  }

  val gradient =
      Brush.verticalGradient(
          colorStops =
              arrayOf(0.0f to Color.White, 0.1f to Color.White, 0.15f to Color.Transparent))

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
        mapViewModel.itineraryList.value?.forEach { (location, latLngList) ->
          Polyline(points = latLngList, color = redFox, width = 15f)
        }
      }
    }
    Box(modifier = Modifier.matchParentSize().background(gradient).align(Alignment.TopCenter)) {
      Text(
          text = mapViewModel.cityNameState.value,
          modifier = Modifier.padding(30.dp).align(Alignment.TopCenter),
          fontSize = 24.sp,
          fontFamily = FontFamily.SansSerif,
          color = Color.Black)

      IconButton(
          onClick = {
            getCurrentLocation(
                context = context,
                onLocationFetched = {
                  deviceLocation = it
                  cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation, 16f)
                })
          }) {
            Icon(
                modifier = Modifier.padding(40.dp),
                painter = painterResource(id = R.drawable.ic_gps_fixed),
                contentDescription = null)
          }
    }
  }
}

@Preview
@Composable
// Start this screen to only see the overview of the map
fun MapOverviewPreview() {
  val context = LocalContext.current
  MapOverview(
      MapViewModel(),
      context,
  )
}
