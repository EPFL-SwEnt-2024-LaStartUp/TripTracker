package com.example.triptracker.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapOverview() {
    val epfl = LatLng(46.5191, 6.5668)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(epfl, 13f)
    }
    GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
        AdvancedMarker(
            state = MarkerState(position = epfl),
            title = "epfl")
    }
}

@Preview
@Composable
fun MapOverviewPreview() {
    MapOverview()
}
