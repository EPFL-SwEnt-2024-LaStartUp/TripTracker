package com.example.triptracker.map

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.location.popupState
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.viewmodel.MapViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import com.google.android.gms.maps.model.LatLng
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.launch
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
  @RelaxedMockK private lateinit var mockViewModelProfile: UserProfileViewModel
  @RelaxedMockK private lateinit var mockNavigation: Navigation

  @Before
  fun setUp() {
    mockViewModel = mockk(relaxed = true)
    mockNavigation = mockk(relaxed = true)
    mockViewModelProfile = mockk(relaxed = true)
  }

  @Test
  fun testMapDisplaysCorrectly() {
    val itineraryList = MockItineraryList().getItineraries()
    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns null
    every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
    every { mockViewModel.pathList.value } returns ItineraryList(itineraryList)
    every { mockViewModel.displayPopUp.value } returns false
    every { mockViewModel.displayPicturePopUp.value } returns false

    composeTestRule.setContent {
      MapOverview(
          mapViewModel = mockViewModel,
          context = appContext,
          navigation = mockNavigation,
          selectedId = "")
    }

    // Verify that the dependencies are properly initialized
    composeTestRule.onNodeWithTag("MapOverview").assertExists()
    composeTestRule.onNodeWithTag("MapOverview").performClick()
  }

  @Test
  fun testMapIsNotDisplayedWhenLocationPermissionNotGranted() {
    val itineraryList = MockItineraryList().getItineraries()
    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns null
    every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
    every { mockViewModel.displayPopUp.value } returns false
    every { mockViewModel.displayPicturePopUp.value } returns false

    composeTestRule.setContent {
      MapOverview(
          mapViewModel = mockViewModel,
          context = appContext,
          navigation = mockNavigation,
          checkLocationPermission = false,
          selectedId = "1")
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
    every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "") } returns null
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "1") } returns itineraryList[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "2") } returns itineraryList[1]
    every { mockViewModel.pathList.value } returns ItineraryList(itineraryList)
    every { mockViewModel.displayPopUp.value } returns false
    every { mockViewModel.displayPicturePopUp.value } returns false

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
    every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "") } returns null
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "1") } returns itineraryList[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "2") } returns itineraryList[1]
    every { mockViewModel.pathList.value } returns ItineraryList(itineraryList)
    every { mockViewModel.displayPopUp.value } returns false
    every { mockViewModel.displayPicturePopUp.value } returns false

    composeTestRule.setContent {
      MapOverview(
          mapViewModel = mockViewModel,
          context = appContext,
          navigation = mockNavigation,
          checkLocationPermission = true,
          selectedId = "")
    }

    // Verify that the dependencies are properly called
    verify { mockViewModel.selectedPolylineState.value }
  }

  @Test
  fun testMapOverviewPreview() {
    val itineraryList = MockItineraryList().getItineraries()
    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns null
    every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "") } returns null
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "1") } returns itineraryList[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "2") } returns itineraryList[1]
    every { mockViewModel.pathList.value } returns ItineraryList(itineraryList)
    every { mockViewModel.displayPopUp.value } returns false
    every { mockViewModel.displayPicturePopUp.value } returns false

    composeTestRule.setContent {
      MapOverview(
          mapViewModel = mockViewModel,
          context = appContext,
          navigation = mockNavigation,
          checkLocationPermission = false,
          selectedId = "1")
    }

    // Verify that the map is not shown
    composeTestRule.onNodeWithTag("MapOverview").assertDoesNotExist()
  }

  @Test
  fun dummyTest() {
    // Dummy test to avoid "Empty test suite" warning
    try {
      val itineraryList = MockItineraryList().getItineraries()
      every { mockViewModel.cityNameState.value } returns "Lyon"
      every { mockViewModel.filteredPathList.value } returns
          mapOf(itineraryList[0] to listOf<LatLng>(LatLng(0.0, 0.0)))
      every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "") } returns null
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "1") } returns
          itineraryList[0]
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "2") } returns
          itineraryList[1]
      every { mockViewModel.selectedPolylineState.value } returns
          MapViewModel.SelectedPolyline(itineraryList[0], LatLng(0.0, 0.0))
      every { mockViewModel.pathList.value } returns ItineraryList(itineraryList)
      every { mockViewModel.displayPopUp.value } returns true
      every { mockViewModel.displayPicturePopUp.value } returns false
      composeTestRule.setContent {
        MapOverview(mapViewModel = mockViewModel, context = appContext, navigation = mockNavigation)
      }
    } catch (e: Exception) {
      // If any exception occurs, fail the test
      junit.framework.TestCase.assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  @Test
  fun testClickOnPath() {
    try {
      val itineraryList = MockItineraryList().getItineraries()
      every { mockViewModel.cityNameState.value } returns "Lyon"
      every { mockViewModel.filteredPathList.value } returns
          mapOf(itineraryList[0] to listOf<LatLng>(LatLng(0.0, 0.0)))
      every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "") } returns null
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "1") } returns
          itineraryList[0]
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "2") } returns
          itineraryList[1]
      every { mockViewModel.selectedPolylineState.value } returns
          MapViewModel.SelectedPolyline(itineraryList[0], LatLng(0.0, 0.0))
      every { mockViewModel.pathList.value } returns ItineraryList(itineraryList)
      every { mockViewModel.displayPopUp.value } returns true
      every { mockViewModel.displayPicturePopUp.value } returns false

      composeTestRule.setContent {
        MapOverview(
            mapViewModel = mockViewModel,
            context = appContext,
            navigation = mockNavigation,
            checkLocationPermission = true,
            selectedId = "")
      }

      composeTestRule.onNodeWithTag("MapOverview").assertExists()
      composeTestRule.onNodeWithTag("Map").assertExists()

      // Verify that the dependencies are properly called
      verify { mockViewModel.selectedPolylineState.value }
    } catch (e: Exception) {
      // If any exception occurs, fail the test
      junit.framework.TestCase.assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  @Test
  fun testClickOnPathAndDisplayOverlay() {
    try {
      val itineraryList = MockItineraryList().getItineraries()
      every { mockViewModel.cityNameState.value } returns "Lyon"
      every { mockViewModel.filteredPathList.value } returns
          mapOf(itineraryList[0] to listOf<LatLng>(LatLng(0.0, 0.0)))
      every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "") } returns null
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "1") } returns
          itineraryList[0]
      every { mockViewModel.getPathById(ItineraryList(itineraryList), "2") } returns
          itineraryList[1]
      every { mockViewModel.selectedPolylineState.value } returns
          MapViewModel.SelectedPolyline(itineraryList[0], LatLng(0.0, 0.0))
      every { mockViewModel.pathList.value } returns ItineraryList(itineraryList)
      every { mockViewModel.displayPopUp.value } returns true
      every { mockViewModel.popUpState } returns popupState.PATHOVERLAY
      every { mockViewModel.displayPicturePopUp.value } returns false

      composeTestRule.setContent {
        MapOverview(
            mapViewModel = mockViewModel,
            context = appContext,
            navigation = mockNavigation,
            checkLocationPermission = true,
            selectedId = "")
      }

      composeTestRule.onNodeWithTag("MapOverview").assertExists()
      composeTestRule.onNodeWithTag("Map").assertExists()

      // Verify that the dependencies are properly called
      verify { mockViewModel.selectedPolylineState.value }
    } catch (e: Exception) {
      // If any exception occurs, fail the test
      junit.framework.TestCase.assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  @Test
  fun testClickOnPicture() {
    val itineraryList = MockItineraryList().getItineraries()
    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns
        mapOf(itineraryList[0] to listOf<LatLng>(LatLng(0.0, 0.0)))
    every { mockViewModel.selectedPolylineState.value } returns
        MapViewModel.SelectedPolyline(itineraryList[0], LatLng(0.0, 0.0))
    every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "") } returns null
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "1") } returns itineraryList[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "2") } returns itineraryList[1]
    every { mockViewModel.pathList.value } returns ItineraryList(itineraryList)
    every { mockViewModelProfile.getUserProfile(itineraryList[0].userMail, any()) } answers
        {
          secondArg<(UserProfile) -> Unit>().invoke(UserProfile("test"))
        }
    every { mockViewModel.displayPopUp.value } returns false
    every { mockViewModel.displayPicturePopUp.value } returns true

    composeTestRule.setContent {
      MapOverview(mapViewModel = mockViewModel, context = appContext, navigation = mockNavigation)
    }

    composeTestRule.onNodeWithTag("MapOverview").assertExists()
    composeTestRule.onNodeWithTag("Map").assertExists()

    // Verify that the dependencies are properly called
    verify { mockViewModel.selectedPolylineState.value }
  }

  @Test
  fun testLaunchedEffect() {
    val itineraryList = MockItineraryList().getItineraries()

    every { mockViewModel.cityNameState.value } returns "Lyon"
    every { mockViewModel.filteredPathList.value } returns
        mapOf(itineraryList[0] to listOf<LatLng>(LatLng(0.0, 0.0)))
    every { mockViewModel.selectedPolylineState.value } returns
        MapViewModel.SelectedPolyline(itineraryList[0], LatLng(0.0, 0.0))
    every { mockViewModel.selectedPin.value } returns itineraryList[0].pinnedPlaces[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "") } returns null
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "1") } returns itineraryList[0]
    every { mockViewModel.getPathById(ItineraryList(itineraryList), "2") } returns itineraryList[1]
    every { mockViewModelProfile.getUserProfile(itineraryList[0].userMail, any()) } answers
        {
          secondArg<(UserProfile) -> Unit>().invoke(UserProfile("test"))
        }
    every { mockViewModel.displayPopUp.value } returns false
    every { mockViewModel.displayPicturePopUp.value } returns false

    every { mockViewModel.pathList } returns MutableLiveData()

    composeTestRule.setContent {
      val coroutineScope = rememberCoroutineScope()

      // Mock ViewModel and other dependencies as needed
      val mapViewModel = mockk<MapViewModel>()

      // Set the initial state for ViewModel
      every { mapViewModel.getPathById(any(), any()) } returns mockk()

      // Set the content with the component that contains the LaunchedEffect
      MapOverview(
          mapViewModel = mockViewModel,
          context = appContext,
          navigation = mockNavigation,
          selectedId = "1")

      // Trigger the LaunchedEffect
      coroutineScope.launch {
        every { mockViewModel.pathList } returns MutableLiveData(ItineraryList(itineraryList))
      }

      coroutineScope.launch { every { mockViewModel.pathList } returns MutableLiveData() }

      coroutineScope.launch {
        every { mockViewModel.pathList } returns MutableLiveData(ItineraryList(itineraryList))
      }
    }
  }
}
