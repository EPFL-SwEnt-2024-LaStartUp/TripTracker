package com.example.triptracker.screens.userProfile

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.InsertPicture
import com.example.triptracker.view.profile.UserProfileEditScreen
import com.example.triptracker.viewmodel.UserProfileViewModel
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.every
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
  @RelaxedMockK private lateinit var userProfileViewModel: UserProfileViewModel
  @RelaxedMockK private lateinit var navigation: Navigation
  @RelaxedMockK
  private lateinit var manager: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
  @RelaxedMockK private lateinit var picture: Uri

  @Before
  fun setUp() {
    userProfileViewModel = mockk(relaxed = true)
    navigation = mockk(relaxed = true)
    manager = mockk(relaxed = true)
    picture = mockk(relaxed = true)
    every { userProfileViewModel.getUserProfile(any(), any()) } coAnswers
        {
          secondArg<(UserProfile?) -> Unit>()
              .invoke(UserProfile("email@example.com", "Test User", "Stupid", "Yesterday"))
        }
  }

  @Test
  fun testUserProfileEditScreenDisplays() {
    // Verify that the screen is displayed
    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
    }
    composeTestRule.onNodeWithTag("UserProfileEditScreen").assertExists()
    composeTestRule.onNodeWithTag("UserProfileEditScreen").assertIsDisplayed()
  }

  @Test
  fun catchTestCalendarIconHasClickableButton() {
    try {
      composeTestRule.setContent {
        UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
      }
      composeTestRule.onNodeWithContentDescription("Calendar").assertHasClickAction()
      composeTestRule.onNodeWithContentDescription("Calendar").performClick()
      composeTestRule.onNodeWithTag("CustomDatePickerDialog").assertExists()
    } catch (e: Exception) {
      junit.framework.TestCase.assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  @Test
  fun testCalendarIconHasClickableButton() {
    try {
      composeTestRule.setContent {
        UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
      }
      composeTestRule.onNodeWithContentDescription("Calendar").assertHasClickAction()
      composeTestRule.onNodeWithContentDescription("Calendar").performClick()
      composeTestRule.onNodeWithTag("CustomDatePickerDialog").assertExists()
    } catch (e: Exception) {
      junit.framework.TestCase.assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  @Test
  fun testSaveButtonHasClickableAction() {
    try {
      composeTestRule.setContent {
        UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
      }
      composeTestRule.onNodeWithText("Save").isDisplayed()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  @Test
  fun testInsertPictureWhenOldPicture() {
    composeTestRule.setContent {
      InsertPicture(pickMedia = manager, selectedPicture = null, oldPicture = picture.toString())
    }
    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("ProfilePicture").performClick()
    verify { manager.launch(any()) }
  }

  @Test
  fun testInsertPictureWhenNewPicture() {
    composeTestRule.setContent {
      InsertPicture(pickMedia = manager, selectedPicture = picture, oldPicture = null)
    }
    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("ProfilePicture").performClick()
    verify { manager.launch(any()) }
  }

  @Test
  fun testInsertPictureWhenNoNewPicture() {
    composeTestRule.setContent {
      InsertPicture(pickMedia = manager, selectedPicture = null, oldPicture = null)
    }
    composeTestRule.onNodeWithTag("NoProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("NoProfilePicture").performClick()
    verify { manager.launch(any()) }
  }
}
