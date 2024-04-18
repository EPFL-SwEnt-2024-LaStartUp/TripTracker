package com.example.triptracker.userProfile

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.view.profile.UserProfileFollowersPreview
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileFollowersTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun titleAndBackButtonAreCorrectlyDisplayed() {
    composeTestRule.setContent { UserProfileFollowersPreview() }

    composeTestRule.onNodeWithTag("FollowersTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("GoBackButton").assertIsDisplayed().assertHasClickAction()
    // ComposeScreen.onComposeScreen<UserProfileFollowers>(composeTestRule) {
    //            // Test UI elements
    //            followingTitle {
    //                assertIsDisplayed()
    //                assertTextEquals("Followers")
    //            }
    //            goBackButton {
    //                assertIsDisplayed()
    //                assertHasClickAction()
    //            }
    //        }
  }
}
