package com.example.triptracker.itinerary

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItineraryTest {
  private val itinerary1 =
      Itinerary(
          id = "1",
          title = "Trip to the beach",
          userMail = "testmail@gmail.com",
          location = Location(0.0, 0.0, "Beach"),
          flameCount = 234,
          startDateAndTime = "2021-01-01",
          endDateAndTime = "2021-01-02",
          pinnedPlaces = listOf(),
          description = "A trip to the beach with friends",
          route = listOf())

  private val itinerary2 =
      Itinerary(
          id = "2",
          title = "",
          userMail = "testmail@gmail.com",
          location = Location(0.0, 0.0, "Mountains"),
          flameCount = 0,
          startDateAndTime = "2021-01-01",
          endDateAndTime = "2021-01-02",
          pinnedPlaces = listOf(),
          description = "A trip to the mountains with friends",
          route = listOf())

  @Test
  fun testItinerary1() {
    assertEquals("1", itinerary1.id)
    assertEquals("Trip to the beach", itinerary1.title)
    assertEquals("testmail@gmail.com", itinerary1.userMail)
    assertEquals(Location(0.0, 0.0, "Beach"), itinerary1.location)
    assertEquals(234, itinerary1.flameCount)
    assertEquals("2021-01-01", itinerary1.startDateAndTime)
    assertEquals("2021-01-02", itinerary1.endDateAndTime)
    assertEquals(listOf<Pin>(), itinerary1.pinnedPlaces)
    assertEquals("A trip to the beach with friends", itinerary1.description)
    assertEquals(listOf<LatLng>(), itinerary1.route)
  }

  @Test
  fun testItinerary2() {
    assertEquals("2", itinerary2.id)
    assertEquals("", itinerary2.title)
    assertEquals("testmail@gmail.com", itinerary2.userMail)
    assertEquals(Location(0.0, 0.0, "Mountains"), itinerary2.location)
    assertEquals(0, itinerary2.flameCount)
    assertEquals("2021-01-01", itinerary2.startDateAndTime)
    assertEquals("2021-01-02", itinerary2.endDateAndTime)
    assertEquals(listOf<Pin>(), itinerary2.pinnedPlaces)
    assertEquals("A trip to the mountains with friends", itinerary2.description)
    assertEquals(listOf<LatLng>(), itinerary2.route)
  }
}
