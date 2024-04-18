package com.example.triptracker.screens.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.viewmodel.HomeViewModel
import com.google.android.gms.maps.model.LatLng
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeTest {
  @get:Rule val composeTestRule = createComposeRule()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule val mockkRule = MockKRule(this)

  // Relaxed mocks methods have a default implementation returning values

  @RelaxedMockK lateinit var mockNav: Navigation
  @RelaxedMockK private lateinit var mockViewModel: HomeViewModel
  @RelaxedMockK private lateinit var mockItineraryRepository: ItineraryRepository

  @Before
  fun setUp() {
    // Mocking necessary components
    mockNav = mockk(relaxed = true)
    mockItineraryRepository = mockk(relaxed = true)
    mockViewModel = mockk(relaxed = true)

    val mockItineraries =
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
                listOf(Pin(0.2, 0.1, "Statue of Liberty", "yes", "test.com")),
                "veryyy cool",
                listOf(LatLng(0.1, 0.4))))

    // Ensure the LiveData is prepared before use
    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    Log.d("ItineraryList", mockViewModel.itineraryList.value.toString())
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(listOf(mockItineraries[0]))
    every { mockNav.getTopLevelDestinations()[0] } returns
        TopLevelDestination(Route.HOME, Icons.Outlined.Home, "Home")

    // Setting up the test composition
    composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel = mockViewModel) }
  }

  @Test
  fun homeScreenComponentsAreDisplayed() {
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      // Test the UI elements
      composeTestRule
          .onNodeWithText("Search for an itinerary", useUnmergedTree = true)
          .assertIsDisplayed()
          .assertTextEquals("Search for an itinerary")

      itinerary {
        assertIsDisplayed()
        assertHasClickAction()
        Log.d("Itinerary", "Itinerary is displayed and clickable")
      }
    }
  }

  @Test
  fun clickingOnItineraryOpensItineraryPreview() {
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      itinerary {
        assertIsDisplayed()
        performClick()
      }
      // TODO check if Pol's itinerary preview is displayed
    }
  }

  @Test
  fun testSearchBarDisplaysComponentsCorrectly() {
    // Mock dependencies
    // Check all components are displayed correctly
    composeTestRule.onNodeWithTag("searchBar", useUnmergedTree = true).assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("Menu", useUnmergedTree = true).assertIsDisplayed()
    composeTestRule.onNodeWithTag("searchBarText", useUnmergedTree = true).assertIsDisplayed()
  }

  @Test
  fun searchFiltersItinerariesCorrectly() {
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      searchBar {
        assertIsDisplayed()
        assertTextEquals("Search for an itinerary")

        performClick()

        performTextInput("NYC")
      }
    }
  }

  @Test
  fun navigationToDetailScreenOnItineraryClick() {
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) { itinerary { performClick() } }
  }

  @Test
  fun profileIconClickOpensProfileScreen() {
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) { profilePic { performClick() } }
  }

  @Test
  fun searchFilterTest() {
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      val noMatchQuery = "This does not match any title"

      searchBar {
        assertIsDisplayed()
        composeTestRule.onNodeWithTag("searchBarText", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("searchBarText", useUnmergedTree = true)
            .assertTextEquals("Search for an itinerary")
        performClick()
        // Check that "No results found" is displayed press a 10 times
        pressKey(84) // letter t
        //composeTestRule.onNodeWithTag("ItineraryItem", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("BackButton", useUnmergedTree = true).performClick()
        performClick()
        composeTestRule.onNodeWithTag("ClearButton", useUnmergedTree = true).performClick()
      }
    }
  }
}
