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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val gradient =
    Brush.verticalGradient(
        colorStops = arrayOf(0.0f to Color.White, 0.1f to Color.White, 0.3f to Color.Transparent))

val DEFAULT_LOCATION = LatLng(46.518831258, 6.559331096)

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
              .background(transparentGray, shape = RoundedCornerShape(16.dp))
              .padding(10.dp)
              .clickable {
                coroutineScope.launch {
                  cameraPositionState.animate(
                      CameraUpdateFactory.newCameraPosition(
                          CameraPosition.fromLatLngZoom(deviceLocation, 17f)))
                }
              })
}
