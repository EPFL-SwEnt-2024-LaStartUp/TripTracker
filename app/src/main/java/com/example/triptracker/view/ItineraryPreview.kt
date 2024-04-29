package com.example.triptracker.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.view.home.DisplayItinerary

val Itinerary =
    Itinerary(
        "1",
        "Trip to the beach",
        "user1",
        Location(0.0, 0.0, "perth"),
        100,
        "2022-12-12 12:00",
        "2022-12-12 14:00",
        listOf(),
        "description",
        listOf())

@Composable
fun ItineraryPreview(itinerary: Itinerary, navigation: Navigation, onClick: () -> Unit) {
  val boxHeight = 600.dp
  val paddingAround = 15.dp

  DisplayItinerary(itinerary, navigation, boxHeight, onClick = onClick)
}

@Preview
@Composable
fun ItineraryPreviewPreview() {
  ItineraryPreview(Itinerary, Navigation(navController = rememberNavController()), {})
}
