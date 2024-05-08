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
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.map.StartScreen
import com.example.triptracker.viewmodel.UserProfileViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartScreenTest {

  val userViewModel = UserProfileViewModel()
  val pin1 = Pin(50.05186463055543, 14.43129605385369, "HQ", "Jetbrains HQ", emptyList())
  val pin2 = Pin(50.0792573623994, 14.418225529534855, "U Fleku", "Oldest restaurant", emptyList())
  val pin3 = Pin(50.08731011666294, 14.420438033846013, "Clock", "Astronomical Clock", emptyList())

  val loc = Location(50.05186463055543, 14.43129605385369, "HQ")
  val sampleItinerary =
      Itinerary(
          "1",
          "Jetbrains Island",
          "json@kotlin.com",
          loc,
          500,
          "4",
          "5",
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

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun userAvatarIsDisplayed() {
    composeTestRule.setContent {
      StartScreen(itinerary = sampleItinerary, profile = profile, userViewModel, onClick = {})
    }
    composeTestRule.onNodeWithTag("ProfilePic").assertIsDisplayed()
  }

  @Test
  fun usernameIsDisplayed() {
    composeTestRule.setContent {
      StartScreen(itinerary = sampleItinerary, profile = profile, userViewModel, onClick = {})
    }
    composeTestRule.onNodeWithTag("Username").assertTextEquals("json@kotlin.com")
  }

  @Test
  fun itineraryTitleIsDisplayed() {
    composeTestRule.setContent {
      StartScreen(itinerary = sampleItinerary, profile = profile, userViewModel, onClick = {})
    }
    composeTestRule.onNodeWithTag("Title").assertTextEquals(sampleItinerary.title)
  }

  @Test
  fun flameCountIsDisplayed() {
    composeTestRule.setContent {
      StartScreen(itinerary = sampleItinerary, profile = profile, userViewModel, onClick = {})
    }
    composeTestRule.onNodeWithText("${sampleItinerary.flameCount}🔥").assertIsDisplayed()
  }

  @Test
  fun startButtonIsDisplayedAndClickable() {
    composeTestRule.setContent {
      StartScreen(itinerary = sampleItinerary, profile = profile, userViewModel, onClick = {})
    }
    composeTestRule.onNodeWithText("Start").assertIsDisplayed()
  }
}
