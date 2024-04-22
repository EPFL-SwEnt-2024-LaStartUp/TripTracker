// package com.example.triptracker.screens.home
//
// import android.util.Log
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.outlined.Home
// import androidx.compose.ui.test.assertHasClickAction
// import androidx.compose.ui.test.assertIsDisplayed
// import androidx.compose.ui.test.assertTextEquals
// import androidx.compose.ui.test.junit4.createComposeRule
// import androidx.compose.ui.test.onNodeWithContentDescription
// import androidx.compose.ui.test.onNodeWithTag
// import androidx.compose.ui.test.onNodeWithText
// import androidx.compose.ui.test.performClick
// import androidx.lifecycle.MutableLiveData
// import androidx.test.espresso.action.ViewActions.pressKey
// import androidx.test.ext.junit.runners.AndroidJUnit4
// import com.example.triptracker.itinerary.MockItineraryList
// import com.example.triptracker.model.itinerary.Itinerary
// import com.example.triptracker.model.repository.ItineraryRepository
// import com.example.triptracker.view.Navigation
// import com.example.triptracker.view.Route
// import com.example.triptracker.view.TopLevelDestination
// import com.example.triptracker.view.home.HomeScreen
// import com.example.triptracker.viewmodel.HomeViewModel
// import io.github.kakaocup.compose.node.element.ComposeScreen
// import io.mockk.every
// import io.mockk.impl.annotations.RelaxedMockK
// import io.mockk.junit4.MockKRule
// import io.mockk.mockk
// import org.junit.Before
// import org.junit.Rule
// import org.junit.Test
// import org.junit.runner.RunWith
//
// @RunWith(AndroidJUnit4::class)
// class HomeTest {
//    @get:Rule val composeTestRule = createComposeRule()
//
//    // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
//    @get:Rule val mockkRule = MockKRule(this)
//
//    // Relaxed mocks methods have a default implementation returning values
//
//    @RelaxedMockK lateinit var mockNav: Navigation
//    @RelaxedMockK private lateinit var mockViewModel: HomeViewModel
//    @RelaxedMockK private lateinit var mockItineraryRepository: ItineraryRepository
//
//    val mockList = MockItineraryList()
//    val mockItineraries = mockList.getItineraries()
//    /**
//     * This method is run before each test to set up the necessary mocks. It is used to initialize
// the
//     * mocks and set up the necessary dependencies.
//     *
//     * Had to copy paste in everyTest so that could specify the mock data for each test: every {
//     * mockItineraryRepository.getAllItineraries() } returns mockItineraries every {
//     * mockViewModel.itineraryList } returns MutableLiveData(mockItineraries) // Setting up the
// test
//     * composition composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
//     * mockViewModel) }
//     */
//    @Before
//    fun setUp() {
//        // Mocking necessary components
//        mockNav = mockk(relaxed = true)
//        mockItineraryRepository = mockk(relaxed = true)
//        mockViewModel = mockk(relaxed = true)
//
//        // Log.d("ItineraryList", mockViewModel.itineraryList.value.toString())
//        every { mockNav.getTopLevelDestinations()[0] } returns
//                TopLevelDestination(Route.HOME, Icons.Outlined.Home, "Home")
//    }
//
//    @Test
//    fun homeScreenComponentsAreDisplayed() {
//        // Have to repeat code to have specific mock data for each test!!
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
//        // Setting up the test composition
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
//            // Test the UI elements
//            composeTestRule
//                .onNodeWithText("Search for an itinerary", useUnmergedTree = true)
//                .assertIsDisplayed()
//                .assertTextEquals("Search for an itinerary")
//
//            itinerary {
//                assertIsDisplayed()
//                assertHasClickAction()
//                Log.d("Itinerary", "Itinerary is displayed and clickable")
//            }
//        }
//    }
//
//    @Test
//    fun clickingOnItineraryOpensItineraryPreview() {
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
//        // Setting up the test composition
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
//            itinerary {
//                assertIsDisplayed()
//                performClick()
//            }
//        }
//    }
//
//    @Test
//    fun testSearchBarDisplaysComponentsCorrectly() {
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
//        // Setting up the test composition
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        // Mock dependencies
//        // Check all components are displayed correctly
//        composeTestRule.onNodeWithTag("searchBar", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithContentDescription("Menu", useUnmergedTree =
// true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("searchBarText", useUnmergedTree = true).assertIsDisplayed()
//    }
//
//    @Test
//    fun searchBarIsDisplayed() {
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
//        // Setting up the test composition
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
//            searchBar {
//                assertIsDisplayed()
//                // This test is failing because of an internal error in Firestore
//            }
//        }
//    }
//
//    /**
//     * This test checks if the search bar is displayed correctly and if the search bar text is
//     * displayed correctly
//     */
//    @Test
//    fun setSearchQuery() {
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns
//                MutableLiveData(listOf(mockItineraries[0]))
//        every { mockViewModel.searchQuery } returns MutableLiveData("tr")
//        // Setting up the test composition
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
//            searchBar {
//                performClick()
//                pressKey(84) // letter t
//                pressKey(82) // letter r
//                mockViewModel.setSearchQuery("tr")
//                composeTestRule.waitForIdle()
//            }
//        }
//    }
//
//    @Test
//    fun itineraryIsClickable() {
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
//        // Setting up the test composition
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) { itinerary {
// performClick() } }
//    }
//
//    @Test
//    fun noItinerariesTextIsDisplayed() {
//        every { mockItineraryRepository.getAllItineraries() } returns emptyList()
//        every { mockViewModel.itineraryList } returns MutableLiveData(null)
//        every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
//            Log.d("ItineraryListInTestnoItToDisplay",
// mockViewModel.itineraryList.value.toString())
//            composeTestRule.onNodeWithTag("NoItinerariesText", useUnmergedTree =
// true).assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun profileIconClickOpensProfileScreen() {
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns MutableLiveData(null)
//        // Setting up the test composition
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) { profilePic {
// performClick() } }
//    }
//
//    /**
//     * This test checks if the search bar is displayed correctly and if the search bar text is
//     * displayed correctly Check if itineraries are displayed.
//     */
//    @Test
//    fun searchFilterTest() {
//        val filteredItineraryLiveData =
// MutableLiveData<List<Itinerary>>(listOf(mockItineraries[0]))
//
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns filteredItineraryLiveData
//        every { mockViewModel.searchQuery } returns MutableLiveData("Tr")
//
//        // This Log.d("FilteredItineraryList",
// mockViewModel.filteredItineraryList.value.toString())
//        // correctly shows [Itinerary(id=1, title=Trip to Paris, username=User1,...)]
//
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
//            // Check that the search bar is displayed
//            searchBar {
//                assertIsDisplayed()
//                composeTestRule.onNodeWithTag("searchBarText", useUnmergedTree =
// true).assertIsDisplayed()
//                composeTestRule
//                    .onNodeWithTag("searchBarText", useUnmergedTree = true)
//                    .assertTextEquals("Search for an itinerary")
//                performClick()
//                composeTestRule.waitForIdle()
//                pressKey(84) // letter t, doesn't update the UI yet
//
//                // Wait for the UI to update with the LiveData change
//                composeTestRule.waitForIdle()
//                // composeTestRule.onNodeWithTag("ItineraryItem", useUnmergedTree =
//                // true).assertIsDisplayed()
//                composeTestRule.onNodeWithTag("BackButton", useUnmergedTree = true).performClick()
//                performClick()
//                composeTestRule.onNodeWithTag("ClearButton", useUnmergedTree =
// true).performClick()
//            }
//        }
//    }
//
//    @Test
//    fun noResultWhenSearchingForInexistantItinerary() {
//        every { mockItineraryRepository.getAllItineraries() } returns mockItineraries
//        every { mockViewModel.itineraryList } returns MutableLiveData(mockItineraries)
//        every { mockViewModel.filteredItineraryList } returns MutableLiveData(emptyList())
//        every { mockViewModel.searchQuery } returns MutableLiveData("test")
//        composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
// mockViewModel) }
//        ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
//            searchBar {
//                assertIsDisplayed()
//                performClick()
//                for (i in 0..9) {
//                    pressKey(84) // letter t
//                }
//                composeTestRule.waitForIdle()
//                composeTestRule.onNodeWithTag("NoResultsFound", useUnmergedTree =
// true).assertIsDisplayed()
//            }
//        }
//    }
// }
