package com.example.triptracker.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.viewmodel.HomeViewModel
import io.mockk.every
import io.mockk.junit4.MockKRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

  private lateinit var homeViewModel: HomeViewModel

  @get:Rule val mockkRule = MockKRule(this)

  private lateinit var mockObserver: Observer<List<Itinerary>>

  @Before
  fun setUp() {
    homeViewModel = HomeViewModel()
    homeViewModel.itineraryList.observeForever(mockObserver)
  }

  @Test
  fun calculateFlameCountsUpdatesFlameCountsCorrectly() {
    val itineraries =
        listOf(
            Itinerary(
                "1",
                "user@example.com",
                "Trip to Paris",
                Location(0.0, 0.0, ""),
                0,
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
                "user@example.com",
                "Trip to Rome",
                Location(0.0, 0.0, ""),
                0,
                5,
                15,
                1,
                "",
                "",
                listOf(),
                "",
                emptyList()))
    val liveData = MutableLiveData<List<Itinerary>>(itineraries)
    every { homeViewModel.itineraryList } returns liveData

    homeViewModel.calculateFlameCounts()

    assert(homeViewModel.itineraryList.value?.get(0)?.flameCount == 45L) // 2*10 + 20 + 5*2
    assert(homeViewModel.itineraryList.value?.get(1)?.flameCount == 30L) // 2*5 + 15 + 5*1
  }

  @Test
  fun filterByTrendingSortsItinerariesCorrectly() {
    val itineraries =
        listOf(
            Itinerary(
                "1",
                "user@example.com",
                "Trip to Paris",
                Location(0.0, 0.0, ""),
                45,
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
                "user@example.com",
                "Trip to Rome",
                Location(0.0, 0.0, ""),
                30,
                5,
                15,
                1,
                "",
                "",
                listOf(),
                "",
                emptyList()),
            Itinerary(
                "3",
                "user@example.com",
                "Trip to Tokyo",
                Location(0.0, 0.0, ""),
                50,
                5,
                40,
                0,
                "",
                "",
                listOf(),
                "",
                emptyList()))
    val liveData = MutableLiveData<List<Itinerary>>(itineraries)
    every { homeViewModel.itineraryList } returns liveData

    homeViewModel.filterByTrending()

    // Expecting Tokyo, Paris, Rome based on flame count 50, 45, 30
    val sortedItineraries = homeViewModel.itineraryList.value
    assert(sortedItineraries?.get(0)?.title == "Trip to Tokyo")
    assert(sortedItineraries?.get(1)?.title == "Trip to Paris")
    assert(sortedItineraries?.get(2)?.title == "Trip to Rome")
  }
}
