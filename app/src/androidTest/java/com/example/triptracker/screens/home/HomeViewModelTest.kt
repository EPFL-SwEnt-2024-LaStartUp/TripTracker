package com.example.triptracker.screens.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.viewmodel.HomeViewModel
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

  private lateinit var homeViewModel: HomeViewModel

  @get:Rule val mockkRule = MockKRule(this)

  @Before
  fun setUp() {
    val itineraries =
        listOf(
            Itinerary(
                "1",
                "Trip to Paris",
                "user@example.com",
                Location(0.0, 0.0, ""),
                50, // set this to 0
                10,
                20,
                2,
                "",
                "",
                listOf(),
                "",
                emptyList()),
            Itinerary(
                "2",
                "Trip to Rome",
                "user@example.com",
                Location(0.0, 0.0, ""),
                30, // set this to 0
                5,
                15,
                1,
                "",
                "",
                listOf(),
                "",
                emptyList()))
    val liveData = MutableLiveData<List<Itinerary>>(itineraries)
    homeViewModel = mockk(relaxed = true)
    every { homeViewModel.itineraryList } returns liveData
  }

  @Test
  fun calculateFlameCountsUpdatesFlameCountsCorrectly() {

    homeViewModel.calculateFlameCounts()

    Log.d("HomeViewModelTest", "flame: ${homeViewModel.itineraryList.value?.get(0)?.flameCount}")

    assert(homeViewModel.itineraryList.value?.get(0)?.flameCount == 50L) // 2*10 + 20 + 5*2
    assert(homeViewModel.itineraryList.value?.get(1)?.flameCount == 30L) // 2*5 + 15 + 5*1
  }

  @Test
  fun filterByTrendingSortsItinerariesCorrectly() {
    val itineraries2 =
        listOf(
            Itinerary(
                "1",
                "Trip to Paris",
                "user@example.com",
                Location(0.0, 0.0, ""),
                70, // 70
                5, // 5
                40, // 40
                4, // 4
                "",
                "",
                listOf(),
                "",
                emptyList()),
            Itinerary(
                "2",
                "Trip to Tokyo",
                "user@example.com",
                Location(0.0, 0.0, ""),
                50,
                10,
                20,
                2,
                "",
                "",
                listOf(),
                "",
                emptyList()),
            Itinerary(
                "3",
                "Trip to Rome",
                "user@example.com",
                Location(0.0, 0.0, ""),
                30,
                5,
                15,
                1,
                "",
                "",
                listOf(),
                "",
                emptyList()))
    val liveData = MutableLiveData<List<Itinerary>>(itineraries2)
    every { homeViewModel.itineraryList } returns liveData

    homeViewModel.filterByTrending()

    // Assertion: Check the result after the function call
    val sortedItineraries = liveData.value
    println("sorted: ${sortedItineraries?.map { it.title }}")
    assert(sortedItineraries?.get(0)?.title == "Trip to Paris")
    assert(sortedItineraries?.get(1)?.title == "Trip to Tokyo")
    assert(sortedItineraries?.get(2)?.title == "Trip to Rome")
  }

  fun calculateFlameCounts(liveData: MutableLiveData<List<Itinerary>>) {
    liveData.value =
        liveData.value?.map { itinerary ->
          itinerary.copy(
              flameCount = 2 * itinerary.saves + itinerary.clicks + 5 * itinerary.numStarts)
        }
  }

  /**
   * Filter the itinerary list by trending itineraries based on the flame count The trending
   * itineraries are sorted in descending order of flame count
   */
  fun filterByTrending(liveData: MutableLiveData<List<Itinerary>>) {
    liveData.value = liveData.value?.sortedByDescending { itinerary -> itinerary.flameCount }
  }
}
