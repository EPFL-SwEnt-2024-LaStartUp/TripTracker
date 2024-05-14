package com.example.triptracker.userProfile

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.screens.userProfile.UserProfileFollowingScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.UserProfileFollowing
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileFollowingTest {
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

    mockViewModel = mockk {
      every { getUserProfileList() } returns mockUserProfiles
      every { getUserProfile(any(), any()) } answers
              {
                secondArg<(UserProfile?) -> Unit>().invoke(mockUserProfiles[0])
              }
      every { userProfileList.value } returns mockUserProfiles
      every { userProfileList } returns MutableLiveData(mockUserProfiles)
      every { setListToFilter(any()) } just Runs
      every { filteredUserProfileList.value } returns mockUserProfiles
      every { filteredUserProfileList } returns MutableLiveData(mockUserProfiles)
      every { setSearchQuery(any()) } just Runs
      every { searchQuery } returns MutableLiveData("") // Default empty search query
      every { addFollowing(any(), any()) } just Runs
      every { removeFollowing(any(), any()) } just Runs
    }
  }

  @Test
  fun componentsAreCorrectlyDisplayed() {
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileFollowing(
          navigation = mockNav,
          profile = MutableUserProfile(mutableStateOf(mockUserProfiles[0])),
          userProfileViewModel = mockViewModel,
      )
    }
    ComposeScreen.onComposeScreen<UserProfileFollowingScreen>(composeTestRule) {
      // Test the UI elements
      followingTitle {
        assertIsDisplayed()
        assertTextEquals("Following")
      }
      followingList { assertIsDisplayed() }

      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Test
  fun removeButtonWorks() {
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileFollowing(
          navigation = mockNav,
          profile = MutableUserProfile(mutableStateOf(mockUserProfiles[0])),
          userProfileViewModel = mockViewModel,
      )
    }

    ComposeScreen.onComposeScreen<UserProfileFollowingScreen>(composeTestRule) {
      friendProfile {
        assertIsDisplayed()
        assertHasClickAction()
      }
      removeButton {
        assertIsDisplayed()
        assertTextEquals("Follow")
        assertHasClickAction()
        performClick()
      }
    }
  }

  @Test
  fun backButtonWorks() {
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileFollowing(
          navigation = mockNav,
          profile = MutableUserProfile(mutableStateOf(mockUserProfiles[0])),
          userProfileViewModel = mockViewModel,
      )
    }

    ComposeScreen.onComposeScreen<UserProfileFollowingScreen>(composeTestRule) {
      // Test the UI elements
      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }
  }
}
