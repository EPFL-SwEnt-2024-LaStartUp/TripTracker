package com.example.triptracker.map

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.example.triptracker.view.map.AddressText
import com.example.triptracker.view.map.PathOverlaySheet
import com.example.triptracker.viewmodel.MapPopupViewModel
import com.example.triptracker.viewmodel.MapViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapPopupTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testPathOverlaySheetDisplaysCorrectly() {
        val itinerary = Itinerary(
            "1", "Test Path", "Test User", Location(0.0, 0.0, "Test Location"),
            0, "start", "end",
            listOf(Pin(0.0, 0.0, "Test Pin", "hi", "https://www.google.com")),
            "description", listOf()
        )

        composeTestRule.setContent {
            PathOverlaySheet(itinerary, MapViewModel())
        }

        composeTestRule.onNodeWithText("Test User's path").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Pin").assertIsDisplayed()
    }

    @Test
    fun testPathOverlaySheetDisplays() {
        // Setup the test environment with the same data used in the @Preview
        val itinerary = Itinerary(
            "1",
            "Jack's Path",
            "Jack",
            Location(34.5, 34.5, "jo"),
            0,
            "start",
            "end",
            listOf(
                Pin(51.509953155490976, -0.1345062081810831, "Picadilly Circus", "hi", "https://www.google.com"),
                Pin(51.501370650469, -0.14182562962180675, "Buckingham Palace", "hi", "https://www.google.com"),
                Pin(51.537120465492286, -0.18335994496202418, "Abbey Road", "hi", "https://www.google.com")
            ),
            "description",
            listOf()
        )

        composeTestRule.setContent {
            PathOverlaySheet(itinerary, MapViewModel())
        }

        // Assertions to check if the UI components are displayed correctly
        composeTestRule.onNodeWithTag("PathOverlaySheet").assertIsDisplayed()
    }


}
