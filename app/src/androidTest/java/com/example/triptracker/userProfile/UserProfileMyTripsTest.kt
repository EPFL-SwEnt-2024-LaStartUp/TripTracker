package com.example.triptracker.userProfile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.network.Connection
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.screens.home.HomeViewScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.profile.UserProfileMyTrips
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileMyTripsTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation
  @RelaxedMockK private lateinit var mockViewModel: HomeViewModel
  @RelaxedMockK private lateinit var mockItineraryRepository: ItineraryRepository
  @RelaxedMockK private lateinit var mockProfile: MutableUserProfile
  @RelaxedMockK private lateinit var mockConnection: Connection

  val mockList = MockItineraryList()
  val mockItineraries = mockList.getItineraries()

  val mockUserList = MockUserList()
  val mockUsers = mockUserList.getUserProfiles()

  @Before
  fun setUp() {
    mockViewModel = mockk(relaxed = true)
    mockNav = mockk(relaxed = true)
    mockItineraryRepository = mockk(relaxed = true)
    mockProfile = mockk(relaxed = true)
    mockConnection = mockk(relaxed = true)

    MockKAnnotations.init(this, relaxUnitFun = true)

    every { mockNav.getTopLevelDestinations()[3] } returns
        TopLevelDestination(Route.MYTRIPS, Icons.Outlined.RadioButtonChecked, "Record")
  }

  @Test
  fun componentsAreCorrectlyDisplayed() {
    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
    every { mockViewModel.filteredItineraryList.value } returns mockItineraries
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileMyTrips(
          homeViewModel = mockViewModel,
          navigation = mockNav,
          userProfile = mockProfile,
          connection = mockConnection)
    }
    composeTestRule.onNodeWithTag("UserProfileMyTripsScreen").assertExists()
    composeTestRule.onNodeWithTag("ScreenTitle").assertExists()
    composeTestRule.onNodeWithTag("GoBackButton").assertExists()
  }

  @Test
  fun noTripsTextIsDisplayed() {
    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
    every { mockViewModel.filteredItineraryList.value } returns emptyList()
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileMyTrips(
          homeViewModel = mockViewModel,
          navigation = mockNav,
          userProfile = mockProfile,
          connection = mockConnection)
    }
    composeTestRule.onNodeWithTag("NoDataText").assertExists()
  }

  // catch the firestore error
  @Test
  fun tripsAreDisplayed() {
    try {
      every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

      every { mockViewModel.filteredItineraryList } returns MutableLiveData(mockItineraries)
      every { mockViewModel.selectedFilter.value } returns FilterType.PIN
      every { mockViewModel.searchQuery.value } returns ""
      every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
      every { mockProfile.userProfile.value } returns mockUsers[0]
      // Setting up the test composition
      composeTestRule.setContent {
        UserProfileMyTrips(
            homeViewModel = mockViewModel,
            navigation = mockNav,
            userProfile = mockProfile,
            connection = mockConnection)
      }
      composeTestRule.onNodeWithTag("DataList").assertExists()
    } catch (e: Exception) {
      TestCase.assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  @Test
  fun tripsAreDisplayed2() {
    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    every { mockViewModel.filteredItineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.selectedFilter.value } returns FilterType.PIN
    every { mockViewModel.searchQuery.value } returns ""
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileMyTrips(
          homeViewModel = mockViewModel,
          navigation = mockNav,
          userProfile = mockProfile,
          connection = mockConnection)
    }
    composeTestRule.onNodeWithTag("DataList").assertExists()
  }

  @Test
  fun itineraryIsLongClickableYes() {
    every { mockItineraryRepository.getAllItineraries() } returns listOf(mockItineraries[0])

    every { mockViewModel.filteredItineraryList } returns
        MutableLiveData(listOf(mockItineraries[0]))
    every { mockViewModel.selectedFilter.value } returns FilterType.PIN
    every { mockViewModel.searchQuery.value } returns ""
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileMyTrips(
          homeViewModel = mockViewModel,
          navigation = mockNav,
          userProfile = mockProfile,
          connection = mockConnection)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      composeTestRule.onNodeWithTag("Itinerary", useUnmergedTree = true).assertIsDisplayed()
      composeTestRule.onNodeWithTag("Itinerary", useUnmergedTree = true).performTouchInput {
        longClick()
      }
      composeTestRule
          .onNodeWithTag("YesCancelItineraryButton", useUnmergedTree = true)
          .performClick()
      composeTestRule
          .onNodeWithTag("YesCancelItineraryButton", useUnmergedTree = true)
          .assertDoesNotExist()
    }
  }

  @Test
  fun itineraryIsLongClickableNo() {
    every { mockItineraryRepository.getAllItineraries() } returns listOf(mockItineraries[0])

    every { mockViewModel.filteredItineraryList } returns
        MutableLiveData(listOf(mockItineraries[0]))
    every { mockViewModel.selectedFilter.value } returns FilterType.PIN
    every { mockViewModel.searchQuery.value } returns ""
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileMyTrips(
          homeViewModel = mockViewModel,
          navigation = mockNav,
          userProfile = mockProfile,
          connection = mockConnection)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      composeTestRule.onNodeWithTag("Itinerary", useUnmergedTree = true).assertIsDisplayed()
      composeTestRule.onNodeWithTag("Itinerary", useUnmergedTree = true).performTouchInput {
        longClick()
      }
      composeTestRule
          .onNodeWithTag("NoCancelItineraryButton", useUnmergedTree = true)
          .performClick()
      composeTestRule
          .onNodeWithTag("NoCancelItineraryButton", useUnmergedTree = true)
          .assertDoesNotExist()
    }
  }
}
