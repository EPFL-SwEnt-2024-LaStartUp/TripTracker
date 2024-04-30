package com.example.triptracker.itinerary

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItineraryListTest {
  private val itineraryList1 = ItineraryList(listOf())
  private val itineraryList2 =
      ItineraryList(
          listOf(
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
                  route = listOf(LatLng(0.0, 0.0), LatLng(0.5, 0.5))),
              Itinerary(
                  id = "2",
                  title = "Trip to the mountains",
                  userMail = "testmail@gmail.com",
                  location = Location(0.2, 0.2, "Mountains"),
                  flameCount = 123,
                  startDateAndTime = "2021-01-01",
                  endDateAndTime = "2021-01-02",
                  pinnedPlaces = listOf(),
                  description = "A trip to the mountains with friends",
                  route = listOf())))

  @Test
  fun getAllItinerariesTest() {
    val itineraryList = itineraryList1.getAllItineraries()
    assert(itineraryList.isEmpty())

    val itineraryList2 = itineraryList2.getAllItineraries()
    assert(itineraryList2.isNotEmpty())
  }

  @Test
  fun setItineraryListTest() {
    val itineraryList = ItineraryList(listOf())
    itineraryList.setItineraryList(
        listOf(
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
                route = listOf())))
    assertEquals(1, itineraryList.size())
    assertEquals("Trip to the beach", itineraryList.getAllItineraries()[0].title)
    assertEquals("testmail@gmail.com", itineraryList.getAllItineraries()[0].userMail)
    assertEquals(234, itineraryList.getAllItineraries()[0].flameCount)
    assertEquals("2021-01-01", itineraryList.getAllItineraries()[0].startDateAndTime)
    assertEquals("2021-01-02", itineraryList.getAllItineraries()[0].endDateAndTime)
    assertEquals("Beach", itineraryList.getAllItineraries()[0].location.name)
    assertEquals(
        "A trip to the beach with friends", itineraryList.getAllItineraries()[0].description)
    assertEquals(0, itineraryList.getAllItineraries()[0].route.size)
    assertEquals(0, itineraryList.getAllItineraries()[0].pinnedPlaces.size)

    itineraryList.setItineraryList(listOf<Itinerary>())
    assertEquals(0, itineraryList.size())
  }

  @Test
  fun addItineraryTest() {
    val itineraryList = ItineraryList(listOf())
    itineraryList.addItinerary(
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
            route = listOf()))
    assertEquals(1, itineraryList.size())
    assertEquals("Trip to the beach", itineraryList.getAllItineraries()[0].title)
    assertEquals("testmail@gmail.com", itineraryList.getAllItineraries()[0].userMail)
    assertEquals(234, itineraryList.getAllItineraries()[0].flameCount)
    assertEquals("2021-01-01", itineraryList.getAllItineraries()[0].startDateAndTime)
    assertEquals("2021-01-02", itineraryList.getAllItineraries()[0].endDateAndTime)
    assertEquals("Beach", itineraryList.getAllItineraries()[0].location.name)
    assertEquals(
        "A trip to the beach with friends", itineraryList.getAllItineraries()[0].description)
    assertEquals(0, itineraryList.getAllItineraries()[0].route.size)
    assertEquals(0, itineraryList.getAllItineraries()[0].pinnedPlaces.size)
  }

  @Test
  fun getFilteredItinerariesByTitleTest() {
    val filteredList2 = itineraryList2.getFilteredItineraries("mountains")
    assertEquals(1, filteredList2.size)
    assertEquals("Trip to the mountains", filteredList2[0].title)
  }

  @Test
  fun getFilteredItinerariesByLocationTest() {
    val filteredList2 =
        itineraryList2.getFilteredItineraries(LatLngBounds(LatLng(-0.1, -0.1), LatLng(0.1, 0.1)), 1)
    assertEquals(1, filteredList2.size)
    assertEquals("Trip to the beach", filteredList2[0].title)
  }

  @Test
  fun getItineraryTest() {
    val itinerary = itineraryList2.getItinerary("1")
    assert(itinerary != null)
    assertEquals("Trip to the beach", itinerary!!.title)
  }

  @Test
  fun getSizeTest() {
    assertEquals(0, itineraryList1.size())
    assertEquals(2, itineraryList2.size())
  }
}
