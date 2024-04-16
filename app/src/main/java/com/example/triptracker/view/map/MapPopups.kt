package com.example.triptracker.view.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Pin
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.viewmodel.MapPopupViewModel

/**
 * PathOverlaySheet is a composable function that displays the all of the pins of a path
 *
 * @param itinerary Itinerary of that path
 */
@Composable
fun PathOverlaySheet(itinerary: Itinerary) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .padding(vertical = 10.dp)
              .background(
                  color = md_theme_light_black,
                  shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))) {
        Column(modifier = Modifier.fillMaxWidth().testTag("PathOverlaySheet").padding(25.dp)) {
          Text(text = itinerary.username + "'s Path", color = Color.White)
          Spacer(modifier = Modifier.height(16.dp))

          // This lazy column will display all the pins in the path
          // Each pin will be displayed using the PathItem composable
          LazyColumn {
            items(itinerary.pinnedPlaces) { pin ->
              PathItem(pin)
              Divider(thickness = 1.dp)
            }
          }
        }
      }
}

/**
 * PathItem is a composable function that displays a single pin in the path
 *
 * @param pinnedPlace specific Pin to be displayed
 */
@Composable
fun PathItem(pinnedPlace: Pin) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(
        painter =
            painterResource(
                id = R.drawable.ic_gps_fixed), // Replace with your actual pin icon resource
        contentDescription = "Location pin",
        tint = Color.White)
    Column(modifier = Modifier.weight(1f).testTag("PathItem").padding(start = 16.dp)) {
      Text(text = pinnedPlace.name, color = Color.White)
      // Fetch address
      AddressText(
          mpv = MapPopupViewModel(),
          latitude = pinnedPlace.latitude.toFloat(),
          longitude = pinnedPlace.longitude.toFloat())
    }
    Icon(
        painterResource(id = R.drawable.rightarrow),
        modifier = Modifier.size(16.dp),
        contentDescription = "More info",
        tint = Color.White)
  }
  Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun AddressText(mpv: MapPopupViewModel, latitude: Float, longitude: Float) {
  val address by mpv.address.observeAsState("Loading address...")

  // Trigger the address fetch
  LaunchedEffect(key1 = latitude, key2 = longitude) { mpv.fetchAddressForPin(latitude, longitude) }

  Text(text = address, color = Color.White)
}
