package com.example.triptracker.userProfile

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.profile.subviews.ProfileCounts
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileCountsTest {
  @get:Rule val composeTestRule = createComposeRule()

  @RelaxedMockK private lateinit var mockNavigation: Navigation

  @Before
  fun setup() {
    mockNavigation = mockk(relaxed = true)

    every { mockNavigation.navController.navigate(Route.FOLLOWERS) } answers {}
    every { mockNavigation.navController.navigate(Route.FOLLOWING) } answers {}
  }

  @Test
  fun testProfileCounts() {
    // Test the profile counts
    composeTestRule.setContent {
      ProfileCounts(
          navigation = mockNavigation,
          profile =
              UserProfile(
                  mail = "test@gmail.com",
                  followers = listOf("follower1", "follower2"),
                  following = listOf("following1", "following2", "following3"),
              ),
          tripsCount = 4)
    }

    composeTestRule.onNodeWithTag("ProfileCounts").assertIsDisplayed()

    composeTestRule.onNodeWithTag("TripsColumn").assertIsDisplayed().assertHasNoClickAction()
    composeTestRule.onNodeWithText("Trips").assertIsDisplayed()
    composeTestRule.onNodeWithText("4").assertIsDisplayed()

    composeTestRule.onNodeWithTag("FollowersColumn").assertIsDisplayed().assertHasClickAction()
    composeTestRule.onNodeWithText("Followers").assertIsDisplayed()
    composeTestRule.onNodeWithText("2").assertIsDisplayed()

    composeTestRule.onNodeWithTag("FollowingColumn").assertIsDisplayed().assertHasClickAction()
    composeTestRule.onNodeWithText("Following").assertIsDisplayed()
    composeTestRule.onNodeWithText("3").assertIsDisplayed()
  }

  @Test
  fun testProfileCountsNavigation() {
    // Test the profile counts
    composeTestRule.setContent {
      ProfileCounts(
          navigation = mockNavigation,
          profile =
              UserProfile(
                  mail = "test@gmail.com",
                  followers = listOf("follower1", "follower2"),
                  following = listOf("following1", "following2", "following3"),
              ),
          tripsCount = 4)
    }

    composeTestRule.onNodeWithTag("FollowersColumn").assertHasClickAction().performClick()
    verify { mockNavigation.navController.navigate(Route.FOLLOWERS) }

    composeTestRule.onNodeWithTag("FollowingColumn").assertHasClickAction().performClick()
    verify { mockNavigation.navController.navigate(Route.FOLLOWING) }
  }
}
