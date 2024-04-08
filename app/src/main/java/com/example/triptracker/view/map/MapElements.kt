package com.example.triptracker.view.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.triptracker.view.theme.md_theme_grey
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Gradient for the top bar of the map screen. This is common for the MapOverview and RecordScreen
val gradient =
    Brush.verticalGradient(
        colorStops = arrayOf(0.0f to Color.White, 0.1f to Color.White, 0.3f to Color.Transparent))

// Default location for the map when starting the app and when the user location is not available
// This is the location of the EPFL campus
val DEFAULT_LOCATION = LatLng(46.518831258, 6.559331096)

/**
 * Display a button to center the map on the device location
 *
 * @param coroutineScope the coroutine scope to launch the animation
 * @param deviceLocation the location of the device
 * @param cameraPositionState the state of the camera position
 */
@Composable
fun DisplayCenterLocationButton(
    coroutineScope: CoroutineScope,
    deviceLocation: LatLng,
    cameraPositionState: CameraPositionState
) {

  Icon(
      imageVector = Icons.Outlined.LocationOn,
      contentDescription = "Center on device location",
      tint = Color.White,
      modifier =
          Modifier.width(50.dp)
              .height(50.dp)
              .background(md_theme_grey, shape = RoundedCornerShape(16.dp))
              .padding(10.dp)
              .clickable {
                coroutineScope.launch {
                  cameraPositionState.animate(
                      CameraUpdateFactory.newCameraPosition(
                          CameraPosition.fromLatLngZoom(deviceLocation, 17f)))
                }
              })
}
