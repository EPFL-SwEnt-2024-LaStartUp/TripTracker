package com.example.triptracker.map

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.map.StartScreen
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.MapViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @RelaxedMockK private lateinit var userViewModel: UserProfileViewModel
  @RelaxedMockK private lateinit var homeViewModel: HomeViewModel
  @RelaxedMockK private lateinit var mapViewModel: MapViewModel
  @RelaxedMockK private lateinit var userProfile: MutableUserProfile

  @Before
  fun setUp() {
    userViewModel = mockk(relaxed = true)
    homeViewModel = mockk(relaxed = true)
    mapViewModel = mockk(relaxed = true)
    userProfile = mockk(relaxed = true)

    userViewModel = mockk {
      coEvery { fetchAllUserProfiles(any()) } just io.mockk.Runs
      coEvery { getUserProfileList() } returns listOf()
      coEvery { getUserProfile(any(), any()) } coAnswers
          {
            secondArg<(UserProfile?) -> Unit>()
                .invoke(UserProfile("polfuentescam@gmail.com", "Pol", "", ""))
          }
      coEvery { addNewUserProfileToDb(any()) } just io.mockk.Runs
      coEvery { updateUserProfileInDb(any()) } just io.mockk.Runs
      coEvery { addFollower(any(), any()) } just io.mockk.Runs
      coEvery { removeFollower(any(), any()) } just io.mockk.Runs
      coEvery { removeUserProfileInDb(any()) } just io.mockk.Runs
    }

    homeViewModel = mockk {
      coEvery { incrementClickCount(any()) } just Runs
      coEvery { incrementSaveCount(any()) } just Runs
      coEvery { incrementNumStarts(any()) } just Runs
    }

    mapViewModel = mockk {
      coEvery { getPathById(any(), any()) } returns
          Itinerary(
              id = "1",
              title = "Jetbrains Island",
              userMail = "joe@gmail.com",
              location = Location(50.05186463055543, 14.43129605385369, "HQ"),
              clicks = 500,
              flameCount = 4,
              numStarts = 5,
              saves = 3,
              startDateAndTime = "2024-04-24",
              endDateAndTime = "2024-04-25",
              pinnedPlaces = listOf(pin1, pin2, pin3),
              description = "test",
              route = emptyList())
      coEvery { getFilteredPaths(any()) } just Runs
    }

    userProfile = MutableUserProfile()
  }

  val pin1 = Pin(50.05186463055543, 14.43129605385369, "HQ", "Jetbrains HQ", emptyList())
  val pin2 = Pin(50.0792573623994, 14.418225529534855, "U Fleku", "Oldest restaurant", emptyList())
  val pin3 = Pin(50.08731011666294, 14.420438033846013, "Clock", "Astronomical Clock", emptyList())

  val loc = Location(50.05186463055543, 14.43129605385369, "HQ")
  val sampleItinerary =
      Itinerary(
          "1",
          "Jetbrains Island",
          "polfuentescam@gmail.com",
          loc,
          500,
          4,
          5,
          3,
          "2024-04-24",
          "2024-04-25",
          listOf(pin1, pin2, pin3),
          "test",
          emptyList())
  val profile =
      UserProfile(
          "pol@gmail.com",
          "Foo",
          "Fighter",
          "29/12/1999",
          "",
          "",
          emptyList(),
          emptyList(),
      )

  @Test
  fun userAvatarIsDisplayed() {
    composeTestRule.setContent {
      StartScreen(
          itinerary = sampleItinerary,
          userViewModel,
          onClick = {},
          mapViewModel = mapViewModel,
          userProfile = userProfile)
    }
    composeTestRule.onNodeWithTag("ProfilePic").assertIsDisplayed()
  }

  @Test
  fun usernameIsDisplayed() {
    composeTestRule.setContent {
      StartScreen(
          itinerary = sampleItinerary,
          userViewModel,
          onClick = {},
          mapViewModel = mapViewModel,
          userProfile = userProfile)
    }
    composeTestRule.onNodeWithTag("Username").assertTextEquals("")
  }

  @Test
  fun itineraryTitleIsDisplayed() {
    composeTestRule.setContent {
      StartScreen(
          itinerary = sampleItinerary,
          userViewModel,
          onClick = {},
          mapViewModel = mapViewModel,
          userProfile = userProfile)
    }
    composeTestRule.onNodeWithTag("Title").assertTextEquals(sampleItinerary.title)
  }

  @Test
  fun flameCountIsDisplayed() {
    composeTestRule.setContent {
      StartScreen(
          itinerary = sampleItinerary,
          userViewModel,
          onClick = {},
          mapViewModel = mapViewModel,
          userProfile = userProfile)
    }
    composeTestRule.onNodeWithTag("FlameCount").assertIsDisplayed()
  }

  @Test
  fun startButtonIsDisplayedAndClickable() {
    composeTestRule.setContent {
      StartScreen(
          itinerary = sampleItinerary,
          userViewModel,
          onClick = {},
          mapViewModel = mapViewModel,
          userProfile = userProfile)
    }
    composeTestRule.onNodeWithText("Start").assertIsDisplayed()
  }
}
