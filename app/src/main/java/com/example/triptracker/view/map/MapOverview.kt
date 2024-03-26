package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.navigation.checkForLocationPermission
import com.example.triptracker.navigation.getCurrentLocation
import com.example.triptracker.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
/**
 * Composable displaying the map overview with all the paths and markers of trips that are around
 * the user's location.
 */
fun MapOverview(
    mapViewModel: MapViewModel = MapViewModel(),
    context: Context,
    startLocation: LatLng,
    showCity: Boolean,
    mapProperties: MapProperties
) {

  // Used to display the gradient with the top bar and the changing city location
  var uiSettings by remember { mutableStateOf(MapUiSettings()) }
  val properties by remember { mutableStateOf(mapProperties) }
  var deviceLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

  /// TODO change location to the users one with permissions

  val screenPosition = startLocation
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(screenPosition, 13f)
  }

  // When the camera is moving, the city name is updated in the top bar with geo decoding
  LaunchedEffect(cameraPositionState.isMoving) {
    if (showCity) {
      Log.d("CAMERA_STATE", "Camera is moving")
      mapViewModel.reverseDecode(
          cameraPositionState.position.target.latitude.toFloat(),
          cameraPositionState.position.target.longitude.toFloat())
      Log.d(
          "LAT_LON",
          "${cameraPositionState.position.target.latitude} and ${cameraPositionState.position.target.longitude}")
    }
  }

  LaunchedEffect(Unit) {
    getCurrentLocation(context = context, onLocationFetched = { deviceLocation = it })
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
          uiSettings = uiSettings,
      ) {
        AdvancedMarker(state = MarkerState(position = screenPosition), title = "EPFL")
      }
    }
    Box(modifier = Modifier.matchParentSize().background(gradient).align(Alignment.TopCenter)) {
      Text(
          text =
              if (showCity) {
                mapViewModel.cityNameState.value
              } else {
                "Record"
              },
          modifier = Modifier.padding(30.dp).align(Alignment.TopCenter),
          fontSize = 24.sp,
          fontFamily = FontFamily.SansSerif,
          color = Color.Black)
      Switch(
          checked = uiSettings.zoomControlsEnabled,
          onCheckedChange = { uiSettings = uiSettings.copy(zoomControlsEnabled = it) },
          modifier = Modifier.padding(15.dp))
    }
  }
}

@Preview
@Composable
fun MapOverviewPreview() {
  val context = LocalContext.current
  MapOverview(
      MapViewModel(),
      context,
      LatLng(46.519962, 6.633597),
      false,
      MapProperties(
          mapType = MapType.SATELLITE, isMyLocationEnabled = checkForLocationPermission(context)))
}
