package com.example.triptracker.screens.userProfile

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.InsertPicture
import com.example.triptracker.view.profile.UserProfileEditScreen
import com.google.common.base.CharMatcher.any
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Tests for the User Profile Edit Screen */
@RunWith(AndroidJUnit4::class)
class UserProfileEditScreenTest : TestCase() {

  @get:Rule val composeTestRule = createComposeRule()
  @RelaxedMockK private lateinit var navigation: Navigation
  @RelaxedMockK
  private lateinit var manager: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
  @RelaxedMockK private lateinit var picture: Uri

  @Before
  fun setUp() {
    navigation = mockk(relaxed = true)
    manager = mockk(relaxed = true)
    picture = mockk(relaxed = true)
  }

  @Test
  fun testUserProfileEditScreenDisplays() {
    // Verify that the screen is displayed
    composeTestRule.setContent { UserProfileEditScreen(navigation) }
    composeTestRule.onNodeWithTag("UserProfileEditScreen").assertExists()
    composeTestRule.onNodeWithTag("UserProfileEditScreen").assertIsDisplayed()
  }

  @Test
  fun testCalendarIconHasClickableButton() {
    composeTestRule.setContent { UserProfileEditScreen(navigation) }
    composeTestRule.onNodeWithContentDescription("Calendar").assertHasClickAction()
    composeTestRule.onNodeWithContentDescription("Calendar").performClick()
    composeTestRule.onNodeWithTag("CustomDatePickerDialog").assertExists()
  }

  @Test
  fun testSaveButtonHasClickableAction() {
    composeTestRule.setContent { UserProfileEditScreen(navigation) }
    composeTestRule.onNodeWithText("Save").assertHasClickAction()
    composeTestRule.onNodeWithText("Save").performClick()
  }

  @Test
  fun testInsertPictureWhenPicture() {
    composeTestRule.setContent { InsertPicture(pickMedia = manager, selectedPicture = picture) }
    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("ProfilePicture").performClick()
    verify { manager.launch(any()) }
  }

  @Test
  fun testInsertPictureWhenNoPicture() {
    composeTestRule.setContent { InsertPicture(pickMedia = manager, selectedPicture = null) }
    composeTestRule.onNodeWithTag("NoProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("NoProfilePicture").performClick()
    verify { manager.launch(any()) }
  }
}
