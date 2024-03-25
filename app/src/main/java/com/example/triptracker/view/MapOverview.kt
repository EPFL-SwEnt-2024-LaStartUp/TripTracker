package com.example.triptracker.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
/**
 * Composable displaying the map overview with all the paths and markers of trips that are around
 * the user's location.
 */
fun MapOverview(mapViewModel: MapViewModel = MapViewModel()) {

  val gradient =
      Brush.verticalGradient(
          colorStops =
              arrayOf(0.0f to Color.White, 0.1f to Color.White, 0.15f to Color.Transparent))

  // Used to display the gradient with the top bar and the changing city location
  Box {
    Map(mapViewModel)
    Box(modifier = Modifier.matchParentSize().background(gradient).align(Alignment.TopCenter)) {
      Text(
          text = mapViewModel.cityNameState.value,
          modifier = Modifier.padding(30.dp).align(Alignment.TopCenter),
          fontSize = 24.sp,
          fontFamily = FontFamily.SansSerif,
          color = Color.Black)
    }
  }
}

@Composable
fun Map(
    mapViewModel: MapViewModel,
) {

  // TODO change location to the users one with permissions
  val epfl = LatLng(46.520862035795545, 6.629670672118664)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(epfl, 13f)
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

  // Displays the map
  GoogleMap(
      modifier =
          Modifier.fillMaxSize()
              .background(
                  brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black))),
      cameraPositionState = cameraPositionState) {
        AdvancedMarker(state = MarkerState(position = epfl), title = "EPFL")
      }
}

@Preview
@Composable
fun MapOverviewPreview() {
  MapOverview(MapViewModel())
}
