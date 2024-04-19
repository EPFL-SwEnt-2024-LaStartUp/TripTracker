package com.example.triptracker.itinerary

import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.google.android.gms.maps.model.LatLng

class MockItineraryList {
  // feel free to add more itineraries in this list
  private val mockItineraries =
      listOf(
          Itinerary(
              "1",
              "Trip to Paris",
              "User1",
              Location(0.0, 0.0, "Paris"),
              200,
              "10-03-2024",
              "20-03-2024",
              listOf(Pin(0.2, 0.1, "Eiffel Tower", "yes", "test.com")),
              "super cool",
              listOf(LatLng(0.1, 0.4))), // Fill in your mock data
          Itinerary(
              "2",
              "NYC was fun",
              "User2",
              Location(0.0, 0.0, "New York"),
              200,
              "10-03-2024",
              "20-03-2024",
              listOf(
                  Pin(0.2, 0.1, "Statue of Liberty", "yes", "test.com"),
                  Pin(0.2, 0.1, "EmpState", "yes", "test.com"),
                  Pin(0.2, 0.1, "Cool", "yes", "test.com"),
                  Pin(0.2, 0.1, "NYC", "yes", "test.com")),
              "veryyy cool",
              listOf(LatLng(0.1, 0.4))))

  fun getItineraries(): List<Itinerary> {
    return mockItineraries
  }
}
