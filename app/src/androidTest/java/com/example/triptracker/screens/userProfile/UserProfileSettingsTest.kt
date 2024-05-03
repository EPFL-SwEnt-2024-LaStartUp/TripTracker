package com.example.triptracker.screens.userProfile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.UserProfileSettings
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Tests for the User Profile Edit Screen */
@RunWith(AndroidJUnit4::class)
class UserProfileSettingsTest : TestCase() {

  @get:Rule val composeTestRule = createComposeRule()
  @RelaxedMockK private lateinit var navigation: Navigation

  @Before
  fun setUp() {
    navigation = mockk(relaxed = true)
    composeTestRule.setContent { UserProfileSettings(navigation) }
  }

  @Test
  fun testSettingsIsDisplayed() {
    composeTestRule.onNodeWithTag("UserProfileSettings").assertExists()
    composeTestRule.onNodeWithTag("UserProfileSettings").assertIsDisplayed()
  }
}
