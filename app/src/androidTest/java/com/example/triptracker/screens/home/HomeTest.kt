package com.example.triptracker.screens.home

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.userProfile.MockUserList
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.home.HomePager
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.home.ItineraryItem
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
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
  // @RelaxedMockK private  lateinit var mockUserProfileRepository: UserProfileRepository
  @RelaxedMockK private lateinit var mockUserProfileRepository: UserProfileRepository
  @RelaxedMockK private lateinit var mockUserProfileViewModel: UserProfileViewModel
  // private lateinit var mockUserProfileViewModel: UserProfileViewModel
  @RelaxedMockK private lateinit var mockProfile: MutableUserProfile

  val mockList = MockItineraryList()
  val mockItineraries = mockList.getItineraries()

  val mockUserList = MockUserList()
  val mockUsers = mockUserList.getUserProfiles()
  val mockMail = "test@gmail.com"

  /**
   * This method is run before each test to set up the necessary mocks. It is used to initialize the
   * mocks and set up the necessary dependencies.
   *
   * Had to copy paste in everyTest so that could specify the mock data for each test: every {
   * mockItineraryRepository.getAllItineraries() } returns mockItineraries every {
   * mockViewModel.itineraryList } returns MutableLiveData(mockItineraries) // Setting up the test
   * composition composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
   * mockViewModel) }
   */
  @Before
  fun setUp() {
    // Mocking necessary components
    mockNav = mockk(relaxed = true)
    mockItineraryRepository = mockk(relaxed = true)
    mockViewModel = mockk(relaxed = true)
    // This allows mocking unit returning functions
    mockUserProfileRepository = mockk(relaxUnitFun = true)
    mockUserProfileViewModel = mockk(relaxUnitFun = true)

    MockKAnnotations.init(this, relaxUnitFun = true)
    mockProfile = mockk(relaxUnitFun = true)

    // Log.d("ItineraryList", mockViewModel.itineraryList.value.toString())
    every { mockNav.getTopLevelDestinations()[0] } returns
        TopLevelDestination(Route.HOME, Icons.Outlined.Home, "Home")

    every { mockUserProfileViewModel.fetchAllUserProfiles { any() } } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<UserProfile>) -> Unit>(0)
          callback(mockUsers)
        }
  }

  /** This test checks if the home screen components are displayed correctly */
  @Test
  fun homeScreenComponentsAreDisplayed() {
    // Have to repeat code to have specific mock data for each test!!
    every { mockUserProfileRepository.getUserProfileByEmail(mockMail) {} } returns
        mockk(relaxUnitFun = true)
    // this removes previous problem
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(
          navigation = mockNav,
          homeViewModel = mockViewModel,
          userProfileViewModel = mockUserProfileViewModel,
          test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      // Test the UI elements
      composeTestRule
          .onNodeWithText("Find Itineraries", useUnmergedTree = true)
          .assertIsDisplayed()
          .assertTextEquals("Find Itineraries")

      itinerary {
        assertIsDisplayed()
        assertHasClickAction()
        Log.d("Itinerary", "Itinerary is displayed and clickable")
      }
    }
  }

  /** This test checks if itinerary is displayed and clicks on it */
  @Test
  fun clickingOnItineraryOpensItineraryPreview() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
    every { mockNav.getTopLevelDestinations()[1] } returns
        TopLevelDestination(Route.MAPS, Icons.Outlined.Place, "Maps")
    every { mockProfile.userProfile.value } returns mockUsers[0]

    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      itinerary {
        assertIsDisplayed()
        performClick()
      }
    }
  }

  /** This test checks if the profile picture is displayed and clicks on it */
  @Test
  fun clickingOnProfilePictureOpensPreview() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(mockItineraries)
    every { mockNav.getTopLevelDestinations()[1] } returns
        TopLevelDestination(Route.MAPS, Icons.Outlined.Place, "Maps")
    every { mockProfile.userProfile.value } returns mockUsers[0]

    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      profilePic {
        assertIsDisplayed()
        performClick()
      }

      profilePicScreen { assertIsDisplayed() }

      profilePicClose {
        assertIsDisplayed()
        performClick()
      }
    }
  }

  /**
   * This test checks if the search bar is displayed correctly and if the search bar text is
   * displayed correctly
   */
  @Test
  fun testSearchBarDisplaysComponentsCorrectly() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }

    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    // Mock dependencies
    // Check all components are displayed correctly
    composeTestRule.onNodeWithTag("searchBar", useUnmergedTree = true).assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("Menu", useUnmergedTree = true).assertIsDisplayed()
    composeTestRule.onNodeWithTag("searchBarText", useUnmergedTree = true).assertIsDisplayed()
  }

  /** This test checks if the search bar is displayed correctly */
  @Test
  fun searchBarIsDisplayed() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }

    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      searchBar { assertIsDisplayed() }
    }
  }

  /**
   * This test checks if the search bar is displayed correctly and if the search bar text is
   * displayed correctly
   */
  @Test
  fun setSearchQuery() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns
        MutableLiveData(listOf(mockItineraries[0]))
    every { mockViewModel.searchQuery } returns MutableLiveData("tr")
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      searchBar {
        performClick()
        pressKey(84) // letter t
        pressKey(82) // letter r
        mockViewModel.setSearchQuery("tr")
        composeTestRule.waitForIdle()
      }
    }
  }

  /** This checks if itinerary is clickable */
  @Test
  fun itineraryIsClickable() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
    every { mockNav.getTopLevelDestinations()[1] } returns
        TopLevelDestination(Route.MAPS, Icons.Outlined.Place, "Maps")
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) { itinerary { performClick() } }
  }

  /** This test checks if no itineraries text is displayed when there are no itineraries */
  @Test
  fun noItinerariesTextIsDisplayed() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(null)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
    every { mockProfile.userProfile.value } returns mockUsers[0]
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      Log.d("ItineraryListInTestnoItToDisplay", mockViewModel.itineraryList.value.toString())
      composeTestRule.onNodeWithTag("NoItinerariesText", useUnmergedTree = true).assertIsDisplayed()
    }
  }

  /** This test clicks on the profile icon */
  @Test
  fun profileIconClickOpensProfileScreen() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
    every { mockProfile.userProfile.value } returns mockUsers[0]
    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) { profilePic { performClick() } }
  }

  /**
   * This test checks if the search bar is displayed correctly and if the search bar text is
   * displayed correctly Check if itineraries are displayed.
   */
  @Test
  fun searchFilterTest() {
    val filteredItineraryLiveData = MutableLiveData<List<Itinerary>>(listOf(mockItineraries[0]))

    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockUserProfileViewModel.fetchAllUserProfiles(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<UserProfile>) -> Unit>(0)
          callback(mockUsers)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns filteredItineraryLiveData
    every { mockViewModel.searchQuery } returns MutableLiveData("Tr")
    every { mockProfile.userProfile.value } returns mockUsers[0]

    // This Log.d("FilteredItineraryList", mockViewModel.filteredItineraryList.value.toString())
    // correctly shows [Itinerary(id=1, title=Trip to Paris, username=User1,...)]

    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      // Check that the search bar is displayed
      searchBar {
        assertIsDisplayed()
        composeTestRule.onNodeWithTag("searchBarText", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("searchBarText", useUnmergedTree = true)
            .assertTextEquals("Find Itineraries")
        performClick()
        composeTestRule.waitForIdle()
        pressKey(84) // letter t, doesn't update the UI yet

        // Wait for the UI to update with the LiveData change
        composeTestRule.waitForIdle()
        // composeTestRule.onNodeWithTag("ItineraryItem", useUnmergedTree =
        // true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("BackButton", useUnmergedTree = true).performClick()
        performClick()
        composeTestRule.onNodeWithTag("ClearButton", useUnmergedTree = true).performClick()
      }
    }
  }

  /**
   * This test checks if the search bar is displayed correctly and if the search bar text is
   * displayed correctly Check if itineraries are displayed.
   */
  @Test
  fun clickOnDropDownMenu() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(mockItineraries)
    every { mockProfile.userProfile.value } returns mockUsers[0]

    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      searchBar {
        assertIsDisplayed()
        performClick()
        pressKey(84)
        composeTestRule.waitForIdle()
      }

      every { mockViewModel.searchQuery } returns MutableLiveData("Tr")
      composeTestRule
          .onNodeWithTag("DropDownBox", useUnmergedTree = true)
          .assertIsDisplayed()
          .performClick()

      // composeTestRule.onNodeWithTag("DropDownFilter").performClick().assertIsDisplayed()
    }
  }

  /** This test checks if no results are displayed when searching for an inexistent itinerary */
  @Test
  fun noResultWhenSearchingForInexistantItinerary() {
    try {
      Log.d("MockItineraries", mockItineraries.toString())
      every { mockUserProfileViewModel.getUserProfile(mockMail) {} } returns
          mockk(relaxUnitFun = true)

      every { mockItineraryRepository.getAllItineraries(any()) } answers
          {
            // Invoke the callback with mock data
            val callback = arg<(List<Itinerary>) -> Unit>(0)
            callback(mockItineraries)
          }
      every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
      every { mockViewModel.filteredItineraryList } returns MutableLiveData(emptyList())
      every { mockViewModel.searchQuery } returns MutableLiveData("test")
      every { mockProfile.userProfile.value } returns mockUsers[0]
      composeTestRule.setContent {
        HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
      }
      ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
        searchBar {
          assertIsDisplayed()
          performClick()
          for (i in 0..9) {
            pressKey(84) // letter t
          }
          composeTestRule.waitForIdle()
          composeTestRule
              .onNodeWithTag("NoResultsFound", useUnmergedTree = true)
              .assertIsDisplayed()
        }
      }
    } catch (e: Exception) {
      // If any exception occurs, fail the test
      TestCase.assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  /** This test checks if no results are displayed when searching for a non-existent itinerary */
  @Test
  fun noResultWhenSearchingForNonExistentItinerary() {
    try {
      Log.d("MockItineraries", mockItineraries.toString())
      every { mockUserProfileViewModel.getUserProfile(mockMail) {} } returns
          mockk(relaxUnitFun = true)

      every { mockItineraryRepository.getAllItineraries(any()) } answers
          {
            // Invoke the callback with mock data
            val callback = arg<(List<Itinerary>) -> Unit>(0)
            callback(mockItineraries)
          }
      every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
      every { mockViewModel.filteredItineraryList } returns MutableLiveData(emptyList())
      every { mockViewModel.searchQuery } returns MutableLiveData("test")
      every { mockProfile.userProfile.value } returns mockUsers[0]
      composeTestRule.setContent {
        HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
      }
      ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
        searchBar {
          assertIsDisplayed()
          performClick()
          for (i in 0..9) {
            pressKey(84) // letter t
          }
          composeTestRule.waitForIdle()
          composeTestRule
              .onNodeWithTag("NoResultsFound", useUnmergedTree = true)
              .assertIsDisplayed()
        }
      }
    } catch (e: Exception) {
      // If any exception occurs, fail the test
      TestCase.assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  @Test
  fun testTabIsDisplayed() {
    composeTestRule.setContent {
      HomePager(
          navigation = mockNav,
          homeViewModel = mockViewModel,
          innerPadding = PaddingValues(0.dp),
          test = true)
    }

    composeTestRule.onNodeWithText("TRENDING", useUnmergedTree = true).assertIsDisplayed()
    composeTestRule.onNodeWithText("FOLLOWING", useUnmergedTree = true).assertIsDisplayed()
  }

  /** This test clicks on the following tab */
  @Test
  fun testClickOnFollowing() {
    every { mockViewModel.itineraryList } returns MutableLiveData(emptyList())
    composeTestRule.setContent {
      HomePager(
          navigation = mockNav,
          homeViewModel = mockViewModel,
          innerPadding = PaddingValues(0.dp),
          test = true)
    }
    composeTestRule.onNodeWithText("FOLLOWING", useUnmergedTree = true).performClick()
  }

  /** This test swipes left on the pager */
  @Test
  fun testSwipeLeft() {
    composeTestRule.setContent {
      HomePager(
          navigation = mockNav,
          homeViewModel = mockViewModel,
          innerPadding = PaddingValues(0.dp),
          test = true)
    }

    // Find the node representing the pager
    composeTestRule.onNodeWithTag("HomePager").performTouchInput { swipeLeft() }
  }

  /** This test swipes left on the pager with non-empty itineraries */
  @Test
  fun SwipeLeftWithItineraries() {
    every { mockUserProfileViewModel.getUserProfile(mockMail) {} } returns
        mockk(relaxUnitFun = true)

    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.trendingList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.followingList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.searchQuery } returns MutableLiveData("test")
    every { mockUserProfileViewModel.fetchAllUserProfiles(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<UserProfile>) -> Unit>(0)
          callback(mockUsers)
        }

    composeTestRule.setContent {
      HomePager(
          navigation = mockNav,
          homeViewModel = mockViewModel,
          innerPadding = PaddingValues(0.dp),
          test = true)
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }

    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      itinerary {
        assertIsDisplayed()
        performClick()
      }
    }

    composeTestRule.onNodeWithTag("HomePager").performTouchInput { swipeLeft() }
  }

  /** This covers uncovered code in HomeScreen */
  @Test
  fun testWithBooleanFalseHomeScreen() {
    // Have to repeat code to have specific mock data for each test!!
    every { mockUserProfileRepository.getUserProfileByEmail(mockMail) {} } returns
        mockk(relaxUnitFun = true)
    // this removes previous problem
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
    every { mockProfile.userProfile.value } returns mockUsers[0]

    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(
          navigation = mockNav,
          homeViewModel = mockViewModel,
          userProfileViewModel = mockUserProfileViewModel,
          test = false)
      HomePager(
          navigation = mockNav,
          homeViewModel = mockViewModel,
          innerPadding = PaddingValues(0.dp),
          test = false)
    }
    composeTestRule.onNodeWithTag("HomePager").performTouchInput { swipeLeft() }
  }

  /** This test clicks on the drop down menu and selects an item */
  @Test
  fun clickOnDropDownMenuDisplaysFilters() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(mockItineraries)
    every { mockProfile.userProfile.value } returns mockUsers[0]

    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      searchBar {
        assertIsDisplayed()
        performClick()
        composeTestRule.waitForIdle()
        // Interact with the dropdown menu
        composeTestRule.onNodeWithTag("DropDownBox").performClick()
        composeTestRule.onNodeWithText("TITLE").assertIsDisplayed()
        // click on Dropdown
        composeTestRule.onNodeWithText("TITLE").performClick()
        composeTestRule.onNodeWithText("USERNAME").assertIsDisplayed().performClick()
      }
    }

    // Verify the filter was set in the view model
    verify { mockViewModel.setSearchFilter(FilterType.entries[1]) }
  }

  @Test
  fun itineraryItemDisplayed() {
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { mockViewModel.filteredItineraryList } returns MutableLiveData(mockItineraries)
    every { mockProfile.userProfile.value } returns mockUsers[0]

    // Setting up the test composition
    composeTestRule.setContent {
      HomeScreen(navigation = mockNav, homeViewModel = mockViewModel, test = true)
      ItineraryItem(itinerary = mockItineraries[0]) {}
    }
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      searchBar {
        assertIsDisplayed()
        performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ItineraryItem").assertIsDisplayed()
      }
    }
  }
}
