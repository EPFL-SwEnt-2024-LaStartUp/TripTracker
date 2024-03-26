package com.example.triptracker.view

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType


@Composable
fun RecordScreen(context: Context) {    //there will be the viewmodel

    var deviceLocation = LatLng(46.519962, 6.633597)
    var mapProperties =
        MapProperties(
            mapType = MapType.NORMAL, isMyLocationEnabled = checkForLocationPermission(context))

}
@Composable
fun Map(){
}


@Preview
@Composable
fun RecordScreenPreview() {
    val context = LocalContext.current
    RecordScreen(
        context,
    )
}