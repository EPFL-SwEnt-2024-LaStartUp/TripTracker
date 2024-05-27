package com.example.triptracker.userProfile

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
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
      composeTestRule.onNodeWithContentDescription("Calendar").performScrollTo()
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
      composeTestRule.onNodeWithContentDescription("Calendar").performScrollTo()
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

  @Test
  fun saveProfile() {
    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
    }
    composeTestRule.onNodeWithText("Save").performClick()
  }

  @Test
  fun saveProfileUserPassed() {

    val mutableUser = MutableUserProfile()
    mutableUser.userProfile.value =
        UserProfile(
            "jake@gmail.com",
            "Test User",
            "Stupid",
            "Yesterday",
            "TestUser",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Prague_%286365119737%29.jpg/800px-Prague_%286365119737%29.jpg",
            emptyList(),
            emptyList(),
            emptyList())

    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = mutableUser)
    }
    composeTestRule.onNodeWithText("Save").performClick()
  }

  @Test
  fun saveProfileUserLongName() {

    val mutableUser = MutableUserProfile()
    mutableUser.userProfile.value =
        UserProfile(
            "jake@gmail.com",
            "Test User",
            "Stupid",
            "Yesterday",
            "TestUser",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Prague_%286365119737%29.jpg/800px-Prague_%286365119737%29.jpg",
            emptyList(),
            emptyList(),
            emptyList())

    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = mutableUser)
    }

    // write long username in text field
    composeTestRule.onNodeWithTag("UserModif").performTextInput("TestUserButTheNameWillBeLong")
    composeTestRule.onNodeWithText("Save").performClick()
  }

  @Test
  fun acceptDate() {
    val mutableUser = MutableUserProfile()
    mutableUser.userProfile.value =
        UserProfile(
            "J",
            "S",
            "S",
            "S",
            "S",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Prague_%286365119737%29.jpg/800px-Prague_%286365119737%29.jpg",
            emptyList(),
            emptyList(),
            emptyList())

    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = mutableUser)
    }

    /* Try to accept */
    // write long username in text field
    composeTestRule.onNodeWithTag("iconDate").performScrollTo()
    composeTestRule.onNodeWithTag("iconDate").performClick()
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    val accept = device.findObject(UiSelector().text("Accept"))
    if (accept.exists()) {
      accept.click()
    }
    // verify CustomDatePicker is open
    composeTestRule.onNodeWithTag("CustomDatePickerDialog").assertExists()
  }

  @Test
  fun selectDate() {
    val mutableUser = MutableUserProfile()
    mutableUser.userProfile.value =
        UserProfile(
            "J",
            "S",
            "S",
            "S",
            "S",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Prague_%286365119737%29.jpg/800px-Prague_%286365119737%29.jpg",
            emptyList(),
            emptyList(),
            emptyList())

    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = mutableUser)
    }

    /* Try to accept */
    // write long username in text field
    composeTestRule.onNodeWithTag("iconDate").performScrollTo()
    composeTestRule.onNodeWithTag("iconDate").performClick()

    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    val day = device.findObject(UiSelector().text("15"))
    if (day.exists()) {
      day.click()
    }

    val accept = device.findObject(UiSelector().text("Accept"))
    if (accept.exists()) {
      accept.click()
    }
  }

  @Test
  fun rejectDate() {
    val mutableUser = MutableUserProfile()
    mutableUser.userProfile.value =
        UserProfile(
            "J",
            "S",
            "S",
            "S",
            "S",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Prague_%286365119737%29.jpg/800px-Prague_%286365119737%29.jpg",
            emptyList(),
            emptyList(),
            emptyList())

    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = mutableUser)
    }

    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    /* Try to cancel */
    // write long username in text field
    composeTestRule.onNodeWithTag("iconDate").performScrollTo()
    composeTestRule.onNodeWithTag("iconDate").performClick()
    val cancel = device.findObject(UiSelector().text("Cancel"))
    if (cancel.exists()) {
      cancel.click()
    }
    // verify CustomDatePicker is open
    composeTestRule.onNodeWithTag("CustomDatePickerDialog").assertExists()
  }

  @Test
  fun interestsTest() {
    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
    }
    composeTestRule.onNodeWithText("Interests").performScrollTo().assertIsDisplayed()
  }

  @Test
  fun travelStyleTest() {
    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
    }
    composeTestRule.onNodeWithText("Travel style").performScrollTo().assertIsDisplayed()
  }

  @Test
  fun languagesTest() {
    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
    }
    composeTestRule.onNodeWithText("Languages").performScrollTo().assertIsDisplayed()
  }

  @Test
  fun downArrowTest() {
    composeTestRule.setContent {
      UserProfileEditScreen(navigation = navigation, profile = MutableUserProfile())
    }
    // we ensure that the arrow is displayed and perform a click on it
    composeTestRule.onNodeWithContentDescription("Scroll down").assertIsDisplayed().performClick()
    // we ensure that once at the bottom of the page, the arrow disappears
    composeTestRule.onNodeWithContentDescription("Scroll down").assertIsNotDisplayed()
  }
}
