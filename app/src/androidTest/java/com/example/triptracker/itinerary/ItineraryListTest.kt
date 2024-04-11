package com.example.triptracker.itinerary

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.location.Location
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
                  username = "Alice",
                  location = Location(0.0, 0.0, "Beach"),
                  flameCount = 234,
                  startDateAndTime = "2021-01-01",
                  endDateAndTime = "2021-01-02",
                  pinnedPlaces = listOf(),
                  description = "A trip to the beach with friends",
                  route = listOf()),
              Itinerary(
                  id = "2",
                  title = "Trip to the mountains",
                  username = "Bob",
                  location = Location(0.0, 0.0, "Mountains"),
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
  fun getFilteredItinerariesByTitleTest() {
    val filteredList2 = itineraryList2.getFilteredItineraries("mountains")
    assertEquals(1, filteredList2.size)
    assertEquals("Trip to the mountains", filteredList2[0].title)
  }

  //    @Test
  //    fun getFilteredItinerariesByLocationTest() {
  //        // TODO: implement this test
  //    }
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
