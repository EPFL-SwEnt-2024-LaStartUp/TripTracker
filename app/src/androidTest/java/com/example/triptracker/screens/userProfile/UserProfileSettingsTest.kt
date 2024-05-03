package com.example.triptracker.screens.userProfile

import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.TriStateButton
import com.example.triptracker.view.profile.UserProfileSettings
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Tests for the User Profile Settings Screen */
@RunWith(AndroidJUnit4::class)
class UserProfileSettingsTest : TestCase() {

  @get:Rule val composeTestRule = createComposeRule()
  @RelaxedMockK private lateinit var navigation: Navigation

  @Before
  fun setUp() {
    navigation = mockk(relaxed = true)
  }

  @Test
  fun testSettingsIsDisplayed() {
    composeTestRule.setContent { UserProfileSettings(navigation) }
    composeTestRule.onNodeWithTag("UserProfileSettings").assertExists()
    composeTestRule.onNodeWithTag("UserProfileSettings").assertIsDisplayed()
  }

  @Test
  fun testSettingsTriStateButton() {
    composeTestRule.setContent {
      Row() {
        TriStateButton(
            state1 = "button1",
            state2 = "button2",
            state3 = "button3",
            state = 0,
            modifier = Modifier) {}

        TriStateButton(
            state1 = "button1",
            state2 = "button2",
            state3 = "button3",
            state = 1,
            modifier = Modifier) {}

        TriStateButton(
            state1 = "button1",
            state2 = "button2",
            state3 = "button3",
            state = 2,
            modifier = Modifier) {}
      }
    }

    val button1 = composeTestRule.onNodeWithText("button1")
    button1.assertExists().assertHasClickAction().performClick()

    val button2 = composeTestRule.onNodeWithText("button2")
    button2.assertExists().assertHasClickAction().performClick()

    val button3 = composeTestRule.onNodeWithText("button3")
    button3.assertExists().assertHasClickAction().performClick()
  }
}
