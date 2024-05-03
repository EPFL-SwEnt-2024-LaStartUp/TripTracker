package com.example.triptracker.userProfile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.screens.userProfile.UserProfileFollowersScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.UserProfileFollowers
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileFollowersTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation
  @RelaxedMockK private lateinit var mockViewModel: UserProfileViewModel
  @RelaxedMockK private lateinit var mockUserProfileRepository: UserProfileRepository

  private val mockList = MockUserList()
  private val mockUserProfiles = mockList.getUserProfiles()

  @Before
  fun setUp() { // Mocking necessary components
    mockNav = mockk(relaxed = true)
    mockUserProfileRepository = mockk(relaxed = true)
    mockViewModel = mockk(relaxed = true)
  }

  @Test
  fun componentsAreCorrectlyDisplayed() {
    every { mockUserProfileRepository.getAllUserProfiles(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<UserProfile>) -> Unit>(0)
          callback(mockUserProfiles)
        }
    every { mockViewModel.getUserProfileList() } returns mockUserProfiles
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileFollowers(navigation = mockNav, viewModel = mockViewModel)
    }
    ComposeScreen.onComposeScreen<UserProfileFollowersScreen>(composeTestRule) {
      // Test the UI elements
      followersTitle {
        assertIsDisplayed()
        assertTextEquals("Followers")
      }
      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
      followersList { assertIsDisplayed() }
      followerProfile { assertIsDisplayed() }
    }
  }

  @Test
  fun removeButtonWorks() {
    every { mockViewModel.getUserProfileList() } returns mockUserProfiles
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileFollowers(navigation = mockNav, viewModel = mockViewModel)
    }
    ComposeScreen.onComposeScreen<UserProfileFollowersScreen>(composeTestRule) {
      removeButton {
        assertIsDisplayed()
        assertTextEquals("Remove")
        assertHasClickAction()
        performClick()
        assertTextEquals("Undo")
        performClick()
        assertTextEquals("Remove")
      }
    }
  }

  @Test
  fun backButtonWorks() {
    every { mockUserProfileRepository.getAllUserProfiles(any()) } answers
        {
          // Invoke the callback with mock data
          val callback = arg<(List<UserProfile>) -> Unit>(0)
          callback(mockUserProfiles)
        }
    every { mockViewModel.getUserProfileList() } returns mockUserProfiles
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileFollowers(navigation = mockNav, viewModel = mockViewModel)
    }
    ComposeScreen.onComposeScreen<UserProfileFollowersScreen>(composeTestRule) {
      // Test the UI elements
      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }
  }
}
