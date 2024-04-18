package com.example.triptracker.userProfile

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.MainActivity
import com.example.triptracker.screens.userProfile.UserProfileFollowing
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileFollowingTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun titleAndBackButtonAreCorrectlyDisplayed() {
    ComposeScreen.onComposeScreen<UserProfileFollowing>(composeTestRule) {
      // Test UI elements
      followingTitle {
        assertIsDisplayed()
        assertTextEquals("Following")
      }
      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }
}
