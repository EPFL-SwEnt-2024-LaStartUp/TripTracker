package com.example.triptracker.view

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.triptracker.navigation.checkForLocationPermission
import com.example.triptracker.navigation.getCurrentLocation
import com.example.triptracker.view.map.MapOverview
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType

@Composable
fun PathMap(context: Context) {

  var showMap by remember { mutableStateOf(false) }
  var deviceLocation by remember { mutableStateOf(LatLng(46.519962, 6.633597)) }

  getCurrentLocation(
      context = context,
      onLocationFetched = {
        deviceLocation = it
        showMap = true
      })

  when (showMap) {
    true -> {
      MapOverview(
          context = context,
          startLocation = deviceLocation,
          showCity = true,
          mapProperties =
              MapProperties(
                  mapType = MapType.SATELLITE,
                  isMyLocationEnabled = checkForLocationPermission(context)))
    }
    false -> {
      //            LaunchPermissionRequest(context = context)
      //            if(checkForLocationPermission(context)) {
      //                showMap = true
      //            }
      //            showMap = true
    }
  }
}

@Preview
@Composable
fun PathMapPreview() {
  val context = LocalContext.current
  PathMap(context = context)
}
