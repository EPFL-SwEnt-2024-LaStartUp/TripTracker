package com.example.triptracker.userProfile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.network.Connection
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.UserProfileOverview
import com.example.triptracker.view.profile.subviews.ProfileButton
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileOverviewTest {

  @get:Rule val composeTestRule = createComposeRule()

  @RelaxedMockK private lateinit var userProfilevm: UserProfileViewModel
  @RelaxedMockK private lateinit var homevm: HomeViewModel
  @RelaxedMockK private lateinit var mockNavigation: Navigation
  @RelaxedMockK private lateinit var connection: Connection

  @Before
  fun setup() {

    mockNavigation = mockk(relaxed = true)
    userProfilevm = mockk(relaxed = true)
    homevm = mockk(relaxed = true)
    connection = mockk(relaxed = true)
  }

  @Test
  fun testUserProfileOverview() {

    userProfilevm = mockk {
      every { getUserProfileList() } returns
          listOf(UserProfile("email@example.com", "Test User", "Stupid", "Yesterday"))
      every { getUserProfile(any(), any()) } coAnswers
          {
            secondArg<(UserProfile?) -> Unit>()
                .invoke(UserProfile("email@example.com", "Test User", "Stupid", "Yesterday"))
          }
      every { onConnectionRefresh() } returns true
      every { setProfile(any()) } returns Unit
    }

    composeTestRule.setContent {
      UserProfileOverview(
          profile = MutableUserProfile(),
          navigation = mockNavigation,
          homeViewModel = homevm,
          userProfileViewModel = userProfilevm)
    }

    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()
  }

  @Test
  fun emptyProfile() {
    composeTestRule.setContent {
      UserProfileOverview(
          profile = MutableUserProfile(),
          navigation = mockNavigation,
          homeViewModel = homevm,
          userProfileViewModel = userProfilevm)
    }
  }

  @Test
  fun testUserProfileOverviewLongName() {

    userProfilevm = mockk {
      every { setProfile(any()) } returns Unit
      every { getUserProfileList() } returns
          listOf(
              UserProfile(
                  "email@example.com",
                  username = "ThisNameIsSuperLongICan'tBelieveIt",
                  name = "Stupid",
                  birthdate = "Yesterday"))
      every { getUserProfile(any(), any()) } coAnswers
          {
            secondArg<(UserProfile?) -> Unit>()
                .invoke(
                    UserProfile(
                        "email@example.com",
                        username = "ThisNameIsSuperLongICan'tBelieveIt",
                        name = "Stupid",
                        birthdate = "Yesterday"))
          }
      every { onConnectionRefresh() } returns true
    }

    composeTestRule.setContent {
      UserProfileOverview(
          profile = MutableUserProfile(),
          navigation = mockNavigation,
          homeViewModel = homevm,
          userProfileViewModel = userProfilevm)
    }

    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()
  }

  @Test
  fun testUserProfileNavigateWindows() {
    userProfilevm = mockk {
      every { setProfile(any()) } returns Unit
      every { getUserProfileList() } returns
          listOf(
              UserProfile(
                  "email@example.com", "ThisNameIsSuperLongICan'tBelieveIt", "Stupid", "Yesterday"))
      every { getUserProfile(any(), any()) } coAnswers
          {
            secondArg<(UserProfile?) -> Unit>()
                .invoke(
                    UserProfile(
                        "email@example.com",
                        "ThisNameIsSuperLongICan'tBelieveIt",
                        "Stupid",
                        "Yesterday"))
          }
      every { onConnectionRefresh() } returns true
    }

    composeTestRule.setContent {
      UserProfileOverview(
          profile = MutableUserProfile(),
          navigation = mockNavigation,
          homeViewModel = homevm,
          userProfileViewModel = userProfilevm)
    }
    composeTestRule.onNodeWithTag("FavoritesButton").performClick()
    composeTestRule.onNodeWithTag("FriendsButton").performClick()
    composeTestRule.onNodeWithTag("SettingsButton").performClick()
    composeTestRule.onNodeWithTag("MyTripsButton").performClick()
  }

  @Test
  fun profileButtonDisplayed() {

    composeTestRule.setContent {
      ProfileButton(label = "Profile", icon = Icons.Filled.AccountCircle, onClick = {})
    }

    composeTestRule.onNodeWithTag("ProfileButton").assertIsDisplayed()
    // is clickable
    composeTestRule.onNodeWithTag("ProfileButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("ProfileButton").performClick()
  }

  @Test
  fun profileButtonIconDisplayed() {

    composeTestRule.setContent {
      ProfileButton(label = "Swent", icon = Icons.Filled.AccountCircle, onClick = {})
    }

    composeTestRule.onNodeWithText("Swent").assertIsDisplayed()
  }
}
