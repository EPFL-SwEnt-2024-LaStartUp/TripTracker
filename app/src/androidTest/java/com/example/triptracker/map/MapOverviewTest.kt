package com.example.triptracker.map

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.view.map.MapOverviewPreview
import com.example.triptracker.viewmodel.MapViewModel
import com.google.android.gms.maps.model.LatLng
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Tests for MapOverview. */
@RunWith(AndroidJUnit4::class)
class MapOverviewTest : TestCase() {
  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule
  val permissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  @RelaxedMockK private lateinit var mockViewModel: MapViewModel
  @RelaxedMockK private lateinit var mockNavigation: Navigation
  @RelaxedMockK private lateinit var filteredPathList: MutableLiveData<Map<Itinerary, List<LatLng>>>

  @Before
  fun setUp() {
    mockViewModel = mockk(relaxed = true)
    mockNavigation = mockk(relaxed = true)
    filteredPathList = mockk(relaxed = true)
  }

  @Test
  fun testMapDisplaysCorrectly() {

    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns null

    composeTestRule.setContent {
      MapOverview(mapViewModel = mockViewModel, context = appContext, navigation = mockNavigation)
    }

    // Verify that the dependencies are properly initialized
    composeTestRule.onNodeWithTag("MapOverview").assertExists()
    composeTestRule.onNodeWithTag("MapOverview").performClick()
  }

  @Test
  fun testMapIsNotDisplayedWhenLocationPermissionNotGranted() {

    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns null

    composeTestRule.setContent {
      MapOverview(
          mapViewModel = mockViewModel,
          context = appContext,
          navigation = mockNavigation,
          checkLocationPermission = false)
    }

    // Verify that the map is not shown
    composeTestRule.onNodeWithTag("MapOverview").assertDoesNotExist()
  }

  @Test
  fun testMapOverviewRetrievesPathList() {
    val itineraryList = MockItineraryList().getItineraries()
    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns
        mapOf(itineraryList[0] to listOf<LatLng>(LatLng(0.0, 0.0)))
    every { mockViewModel.selectedPolylineState.value } returns
        MapViewModel.SelectedPolyline(itineraryList[0], LatLng(0.0, 0.0))

    composeTestRule.setContent {
      MapOverview(mapViewModel = mockViewModel, context = appContext, navigation = mockNavigation)
    }

    // Verify that the dependencies are properly called
    verify { mockViewModel.filteredPathList.value }
  }

  @Test
  fun testMapOverviewDisplayedASelectedPath() {
    val itineraryList = MockItineraryList().getItineraries()
    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns
        mapOf(itineraryList[0] to listOf<LatLng>(LatLng(0.0, 0.0)))
    every { mockViewModel.selectedPolylineState.value } returns
        MapViewModel.SelectedPolyline(itineraryList[0], LatLng(0.0, 0.0))

    composeTestRule.setContent {
      MapOverview(mapViewModel = mockViewModel, context = appContext, navigation = mockNavigation)
    }

    // Verify that the dependencies are properly called
    verify { mockViewModel.selectedPolylineState.value }
  }

  @Test
  fun testMapOverviewPreview() {
    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns null

    composeTestRule.setContent { MapOverviewPreview(mapViewModel = mockViewModel) }

    // Verify that the dependencies are properly initialized
    composeTestRule.onNodeWithTag("MapOverview").assertExists()
  }
}
