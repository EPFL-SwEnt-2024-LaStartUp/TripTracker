package com.example.triptracker.userProfile

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.screens.userProfile.UserProfileFriendsScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.UserProfileFriendsFinder
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileFriendsTest {
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
      every { searchQuery } returns MutableLiveData("") // Default empty search query
    }
  }

  @Test
  fun componentAreCorrectlyDisplayed() {
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileFriendsFinder(
          navigation = mockNav,
          profile = MutableUserProfile(mutableStateOf(mockUserProfiles[0])),
          userProfileViewModel = mockViewModel,
      )
    }
    ComposeScreen.onComposeScreen<UserProfileFriendsScreen>(composeTestRule) {
      friendsTitle { assertIsDisplayed() }
      friendsList { assertIsDisplayed() }
      notDisplayingProfile { assertIsDisplayed() }
      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }

    composeTestRule.onNodeWithTag("FriendsFinderScreen").assertIsDisplayed()
    composeTestRule.onNodeWithText("Friends Finder").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SearchBar").assertIsDisplayed().assertHasClickAction()
  }

  @Test
  fun navigationTest() {
    every { mockNav.goBack() } answers {}
    // Setting up the test composition
    composeTestRule.setContent {
      UserProfileFriendsFinder(
          navigation = mockNav,
          profile = MutableUserProfile(mutableStateOf(mockUserProfiles[0])),
          userProfileViewModel = mockViewModel,
      )
    }

    ComposeScreen.onComposeScreen<UserProfileFriendsScreen>(composeTestRule) {
      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
    composeTestRule.onNodeWithTag("GoBackButton").assertHasClickAction().performClick()
    verify { mockNav.goBack() }
  }
}
