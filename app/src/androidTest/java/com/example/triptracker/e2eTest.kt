package com.example.triptracker

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.espresso.action.ViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.screens.home.HomeViewScreen
import com.example.triptracker.userProfile.MockUserList
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.view.map.RecordScreen
import com.example.triptracker.view.profile.UserProfileEditScreen
import com.example.triptracker.view.profile.UserProfileFavourite
import com.example.triptracker.view.profile.UserProfileFriendsFinder
import com.example.triptracker.view.profile.UserProfileMyTrips
import com.example.triptracker.view.profile.UserProfileOverview
import com.example.triptracker.view.profile.UserProfileSettings
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.MapViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class E2ETest {
  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

  @get:Rule val composeTestRule = createComposeRule()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule val mockkRule = MockKRule(this)

  // Relaxed mocks methods have a default implementation returning values

  @RelaxedMockK lateinit var mockNav: Navigation
  @RelaxedMockK private lateinit var homeViewModel: HomeViewModel
  @RelaxedMockK private lateinit var mapViewModel: MapViewModel

  @RelaxedMockK private lateinit var mockItineraryRepository: ItineraryRepository
  @RelaxedMockK private lateinit var mockUserProfileRepository: UserProfileRepository
  @RelaxedMockK private lateinit var mockUserProfileViewModel: UserProfileViewModel
  @RelaxedMockK private lateinit var mockProfile: MutableUserProfile
  var profile = MutableUserProfile()
  val mockList = MockItineraryList()
  val mockItineraries = mockList.getItineraries()

  val mockUserList = MockUserList()
  val mockUsers = mockUserList.getUserProfiles()
  val mockMail = "test@gmail.com"

  @Before
  fun setUp() {
    // Mocking necessary components
    mockNav = mockk(relaxed = true)
    mockItineraryRepository = mockk(relaxed = true)
    homeViewModel = mockk(relaxed = true)
    mapViewModel = mockk(relaxed = true)
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

  @Test
  fun E2ETestFinal() {

    every { mockUserProfileRepository.getUserProfileByEmail(mockMail) {} } returns
        mockk(relaxUnitFun = true)
    // this removes previous problem
    every { mockItineraryRepository.getAllItineraries(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<Itinerary>) -> Unit>(0)
          callback(mockItineraries)
        }
    every { homeViewModel.itineraryList } returns MutableLiveData(mockItineraries)
    every { homeViewModel.filteredItineraryList } returns MutableLiveData(mockItineraries)
    every { mockProfile.userProfile.value } returns mockUsers[0]

    composeTestRule.setContent {
      Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        val navController = rememberNavController()
        val navigation = remember(navController) { Navigation(navController) }
        NavHost(
            navController = navController,
            startDestination = Route.HOME,
        ) {
          composable(Route.HOME) {
            HomeScreen(navigation = navigation, homeViewModel = homeViewModel, test = true)
          }
          composable(Route.FRIENDS) {
            UserProfileFriendsFinder(navigation = navigation, profile = profile)
          }

          composable(Route.RECORD) { RecordScreen(appContext, navigation) }
          composable(Route.PROFILE) {
            UserProfileOverview(navigation = navigation, profile = profile)
          }
          composable(Route.MYTRIPS) {
            UserProfileMyTrips(
                navigation = navigation,
                userProfile = profile,
            )
          }
          composable(Route.EDIT) {
            UserProfileEditScreen(navigation = navigation, profile = profile)
          }
          composable("MAPS?id={id}", arguments = listOf(navArgument("id") { defaultValue = "" })) {
              backStackEntry ->
            MapOverview(
                context = appContext,
                navigation = navigation,
                selectedId = backStackEntry.arguments?.getString("id") ?: "",
                userProfile = profile)
          }
          composable(Route.SETTINGS) { UserProfileSettings(navigation) }
          composable(Route.FAVORITES) {
            UserProfileFavourite(navigation = navigation, userProfile = profile)
          }
        }
      }
    }

    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ItineraryList").assertIsDisplayed()
    ComposeScreen.onComposeScreen<HomeViewScreen>(composeTestRule) {
      // Test the UI elements
      composeTestRule
          .onNodeWithText("Find Itineraries", useUnmergedTree = true)
          .assertIsDisplayed()
          .assertTextEquals("Find Itineraries")

      profilePic {
        assertIsDisplayed()
        performClick()
      }

      profilePicScreen { assertIsDisplayed() }

      profilePicClose {
        assertIsDisplayed()
        performClick()
      }
      searchBar {
        assertIsDisplayed()
        composeTestRule.onNodeWithTag("searchBarText", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("searchBarText", useUnmergedTree = true)
            .assertTextEquals("Find Itineraries")
        performClick()
        composeTestRule.waitForIdle()
        ViewActions.pressKey(84) // letter t, doesn't update the UI yet

        // Wait for the UI to update with the LiveData change
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("BackButton", useUnmergedTree = true).performClick()
        performClick()
        composeTestRule.onNodeWithTag("ClearButton", useUnmergedTree = true).performClick()
      }
      itineraryUsernameBox {
        assertIsDisplayed()
        assertHasClickAction()
        Log.d("Itinerary", "username is displayed and clickable")
      }
      itinerary {
        assertIsDisplayed()
        assertHasClickAction()
        Log.d("Itinerary", "Itinerary is displayed and clickable")
        performClick()
      }
    }

    composeTestRule.onNodeWithText("Profile").performClick()

    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // travel to the friends screen
    composeTestRule.onNodeWithTag("FriendsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("FriendsButton").performClick()

    // verify that the favorites screen is open
    composeTestRule.onNodeWithTag("FriendsFinderScreen").assertIsDisplayed()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // travel to the favorites
    composeTestRule.onNodeWithTag("FavoritesButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("FavoritesButton").performClick()

    // verify that the favorites screen is open
    composeTestRule.onNodeWithTag("UserProfileFavouriteScreen").assertIsDisplayed()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // travel to the settings
    composeTestRule.onNodeWithTag("SettingsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("SettingsButton").performClick()

    // verify that the followers screen is open
    composeTestRule.onNodeWithTag("UserProfileSettings").assertIsDisplayed()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // travel to the my trips
    composeTestRule.onNodeWithTag("MyTripsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("MyTripsButton").performClick()

    // verify that the my trips screen is open
    composeTestRule.onNodeWithTag("UserProfileMyTripsScreen").assertIsDisplayed()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // Edit profile
    composeTestRule.onNodeWithContentDescription("Edit").assertHasClickAction()
    composeTestRule.onNodeWithContentDescription("Edit").performClick()

    // verify that the edit screen is open
    composeTestRule.onNodeWithTag("UserProfileEditScreen").assertIsDisplayed()

    // input into the field
    composeTestRule.onNodeWithText("Save").isDisplayed()
    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // Travel accross the application

    composeTestRule.onNodeWithText("Home").performClick()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()

    composeTestRule.onNodeWithText("Maps").performClick()
  }
}
