package com.example.triptracker.map

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.example.triptracker.view.map.AddressText
import com.example.triptracker.view.map.PathOverlaySheet
import com.example.triptracker.viewmodel.MapPopupViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapPopupTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun testPathOverlaySheetDisplays() {
    // Setup the test environment with the same data used in the @Preview
    val itinerary =
        Itinerary(
            "1",
            "Jack's Path",
            "Jack",
            Location(34.5, 34.5, "jo"),
            0,
            "start",
            "end",
            listOf(
                Pin(
                    51.50991301840581,
                    -0.13424873072712565,
                    "Picadilly Circus",
                    "hi",
                    listOf("https://www.google.com")),
                Pin(
                    51.501370650469,
                    -0.14182562962180675,
                    "Buckingham Palace",
                    "hi",
                    listOf("https://www.google.com")),
                Pin(
                    51.537120465492286,
                    -0.18335994496202418,
                    "Abbey Road",
                    "hi",
                    listOf("https://www.google.com"))),
            "description",
            listOf())

    composeTestRule.setContent { PathOverlaySheet(itinerary) }

    // Assertions to check if the UI components are displayed correctly
    composeTestRule.onNodeWithText("Jack's Path").assertIsDisplayed()
    composeTestRule.onNodeWithText("Picadilly Circus").assertIsDisplayed()
    composeTestRule.onNodeWithText("Buckingham Palace").assertIsDisplayed()
    composeTestRule.onNodeWithText("Abbey Road").assertIsDisplayed()
  }

  // Test if address is correctly displayed
  @Test
  fun testAddressTextDisplays() {
    // Setup the test environment with the same data used in the @Preview
    val pin =
        Pin(
            51.50991301840581,
            -0.13424873072712565,
            "Picadilly Circus",
            "hi",
            listOf("https://www.google.com"))

    composeTestRule.setContent {
      AddressText(MapPopupViewModel(), pin.latitude.toFloat(), pin.longitude.toFloat())
    }

    // Assertions to check if the UI components are displayed correctly
    composeTestRule.onNodeWithTag("AddressText").assertIsDisplayed()
  }
}
