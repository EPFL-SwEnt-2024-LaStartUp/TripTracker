package com.example.triptracker.userProfile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.screens.userProfile.UserProfileFollowingScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.profile.UserProfileFollowing
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
class UserProfileFollowingTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation
  @RelaxedMockK private lateinit var mockViewModel: UserProfileViewModel
  @RelaxedMockK private lateinit var mockUserProfileRepository: UserProfileRepository

  private val mockList = MockUserList()
  private val mockUserProfiles = mockList.getUserProfiles()

  @Before
  fun setUp() {// Mocking necessary components
    mockNav = mockk(relaxed = true)
    mockUserProfileRepository = mockk(relaxed = true)
    mockViewModel = mockk(relaxed = true)

    // Log.d("ItineraryList", mockViewModel.itineraryList.value.toString())
    every { mockNav.getTopLevelDestinations()[0] } returns
            TopLevelDestination(Route.HOME, Icons.Outlined.Home, "Home")
  }
  @Test
  fun componentsAreCorrectlyDisplayed() {
    // Have to repeat code to have specific mock data for each test!!
    every { mockUserProfileRepository.getAllUserProfiles() } returns mockUserProfiles
    every { mockViewModel.userProfileList } returns MutableLiveData(mockUserProfiles)
    // Setting up the test composition
    composeTestRule.setContent { UserProfileFollowing(navigation = mockNav, userProfileViewModel = mockViewModel, userProfile = mockList.getUserProfiles()[2]) }
    ComposeScreen.onComposeScreen<UserProfileFollowingScreen>(composeTestRule) {
      // Test the UI elements
      followingTitle {
        assertIsDisplayed()
        assertTextEquals("Following")
      }
      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
      followingList {
        assertIsDisplayed()
      }
      removeButton {
        assertIsDisplayed()
        assertIsEnabled()
        assertTextEquals("Remove")
      }
      undoButton {
        assertIsDisplayed()
        assertIsNotEnabled()
        assertTextEquals("Undo")
      }
    }
  }

}
