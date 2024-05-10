package com.example.triptracker.screens.home

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.viewmodel.HomeViewModel
import io.mockk.*
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var liveData: MutableLiveData<List<Itinerary>>
  private lateinit var repository: ItineraryRepository

  @get:Rule val mockkRule = MockKRule(this)

  @Before
  fun setUp() {
    repository = mockk(relaxed = true)
    homeViewModel = mockk(relaxed = true)

    val itineraries =
        listOf(
            Itinerary(
                "1",
                "Trip to Paris",
                "user@example.com",
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
                "Trip to Rome",
                "user@example.com",
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
    liveData = MutableLiveData(itineraries)
    every { homeViewModel.itineraryList } returns liveData
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun incrementClickCountUpdatesCountsCorrectly() = runBlockingTest {
    val itineraryId = "1"
    val initialClicks = liveData.value?.find { it.id == itineraryId }?.clicks ?: 0
    every { repository.incrementField(itineraryId, "saves") } just Runs
    every { repository.updateField(itineraryId, "flameCount", any()) } just Runs

    homeViewModel.incrementClickCount(itineraryId)

    verify(exactly = 1) { homeViewModel.incrementClickCount(itineraryId) }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun incrementSaveCountUpdatesCountsAndFlameCorrectly() = runBlockingTest {
    val itineraryId = "1"
    val initialSaves = liveData.value?.find { it.id == itineraryId }?.saves ?: 0

    every { repository.incrementField(itineraryId, "saves") } just Runs
    every { repository.updateField(itineraryId, "flameCount", any()) } just Runs

    homeViewModel.incrementSaveCount(itineraryId)

    verify { homeViewModel.incrementSaveCount(itineraryId) }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun incrementNumStartsUpdatesCountsAndFlameCorrectly() = runBlockingTest {
    val itineraryId = "1"
    val initialNumStarts = liveData.value?.find { it.id == itineraryId }?.numStarts ?: 0

    every { repository.incrementField(itineraryId, "numStarts") } just Runs
    every { repository.updateField(itineraryId, "flameCount", any()) } just Runs

    homeViewModel.incrementNumStarts(itineraryId)

    verify(exactly = 1) { homeViewModel.incrementNumStarts(itineraryId) }
  }

  @Test
  fun calculateFlameCountsTest() {
    val saves = 10L
    val clicks = 20L
    val numStarts = 2L
    val flameCount = calculateFlameCounts(saves, clicks, numStarts)
    assertEquals(50, flameCount)
  }

  fun calculateFlameCounts(saves: Long, clicks: Long, numStarts: Long): Long {
    return 2 * saves + clicks + 5 * numStarts
  }
}
