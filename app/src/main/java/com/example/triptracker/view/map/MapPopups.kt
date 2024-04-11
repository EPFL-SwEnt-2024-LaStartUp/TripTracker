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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.viewmodel.MapViewModel

@Preview
@Composable
fun TestPathOverlaySheet() {
  // test a path overlay sheet
  val itinerary =
      Itinerary(
          "1",
          "Jack's Path",
          "Jack",
          Location(34.5, 34.5, "jo"),
          0,
          "start",
          "end",
          listOf(
              Pin(78.3, 78.3, "Picadilly Circus", "hi", "https://www.google.com"),
              Pin(78.3, 78.3, "Buckingham Palace", "hi", "https://www.google.com"),
              Pin(78.3, 78.3, "Abbey Road", "hi", "https://www.google.com")),
          "description",
          listOf())
  PathOverlaySheet(itinerary, MapViewModel())
}

/**
 * PathOverlaySheet is a composable function that displays the all of the pins of a path
 *
 * @param itinerary Itinerary of that path
 */
@Composable
fun PathOverlaySheet(itinerary: Itinerary, mv: MapViewModel) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .padding(vertical = 10.dp)
              .background(
                  color = md_theme_light_black,
                  shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))) {
        Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {
          Text(text = "Jack's Path", color = Color.White)
          Spacer(modifier = Modifier.height(16.dp))

          LazyColumn {
            items(itinerary.pinnedPlaces) { pin ->
              PathItem(pin, MapViewModel())
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
 * @param mv MapViewModel, used to find addresss of the pin
 */
@Composable
fun PathItem(pinnedPlace: Pin, mv: MapViewModel) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(
        painter =
            painterResource(
                id = R.drawable.ic_gps_fixed), // Replace with your actual pin icon resource
        contentDescription = "Location pin",
        tint = Color.White)
    Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
      Text(text = pinnedPlace.name, color = Color.White)
      // Fetch address
      Text(
          text = // mv.reverseDecode(pinnedPlace.latitude.toFloat(),
              // pinnedPlace.longitude.toFloat()
              "address",
          color = Color.White)
    }
    Icon(
        painterResource(id = R.drawable.rightarrow),
        modifier = Modifier.size(16.dp),
        contentDescription = "More info",
        tint = Color.White)
  }
  Spacer(modifier = Modifier.height(16.dp))
}
