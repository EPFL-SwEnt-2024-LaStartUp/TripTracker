package com.example.triptracker.userProfile

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.screens.userProfile.UserView
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.profile.UserView
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserViewTest {
  @get:Rule val composeTestRule = createComposeRule()

  @RelaxedMockK private lateinit var mockUserVm: UserProfileViewModel
  @RelaxedMockK private lateinit var homevm: HomeViewModel
  @RelaxedMockK private lateinit var mockNavigation: Navigation
  @RelaxedMockK private lateinit var mockRepo: UserProfileRepository

  private val mockList = MockUserList()
  private val mockUserProfiles = mockList.getUserProfiles()

  private val mockItineraryList = MockItineraryList()
  private val mockItineraries = mockItineraryList.getItineraries()

  @Before
  fun setup() {
    mockNavigation = mockk(relaxed = true)
    mockUserVm = mockk(relaxed = true)
    homevm = mockk(relaxed = true)
    mockRepo = mockk(relaxed = true)
  }

  @Test
  fun componentsAreCorrectlyDisplayed() {
    mockUserVm = mockk {
      every { getUserProfileList() } returns mockUserProfiles
      every { getUserProfile(any(), any()) } coAnswers
          {
            secondArg<(UserProfile?) -> Unit>().invoke(mockUserProfiles[0])
          }
      every { removeFollowing(any(), any()) } just Runs
      every { addFollowing(any(), any()) } just Runs
    }

    homevm = mockk {
      every { setSearchFilter(any()) } just Runs
      every { setSearchQuery(any()) } just Runs
      every { filteredItineraryList.value } returns mockItineraries
      every { filteredItineraryList } returns MutableLiveData(mockItineraries)
    }

    // Setting up the test composition
    composeTestRule.setContent {
      UserView(
          profile = MutableUserProfile(mutableStateOf(mockUserProfiles[1])),
          navigation = mockNavigation,
          userMail = "example@gmail.com",
          homeViewModel = homevm,
          test = true)
    }

    composeTestRule.waitForIdle() // Wait for the UI to stabilize

    ComposeScreen.onComposeScreen<UserView>(composeTestRule) {
      usernameTitle { assertIsDisplayed() }
      profilePicture { assertIsDisplayed() }
      username { assertIsDisplayed() }
      nameAndSurname { assertIsDisplayed() }
      interestTitle {
        assertIsDisplayed()
        assertTextEquals("Interests")
      }
      interestList { assertIsDisplayed() }
      travelStyleTitle {
        assertIsDisplayed()
        assertTextEquals("Travel Style")
      }
      travelStyleList { assertIsDisplayed() }
      languagesTitle {
        assertIsDisplayed()
        assertTextEquals("Languages")
      }
      languagesList { assertIsDisplayed() }
      followingButton {
        assertIsDisplayed()
        //        assertTextEquals("Follow")
        //        assertHasClickAction()
        //        performClick()
        //        assertTextEquals("Following")
      }
      tripsTitle {
        assertIsDisplayed()
        assertTextEquals("Trips")
      }
      tripsCount { assertIsDisplayed() }
      followersTitle {
        assertIsDisplayed()
        assertTextEquals("Followers")
      }
      followersCount { assertIsDisplayed() }
      followingTitle {
        assertIsDisplayed()
        assertTextEquals("Following")
      }
      followingCount { assertIsDisplayed() }
      goBackButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Test
  fun Test2UserProfiles() {

    mockUserVm = mockk {
      every { getUserProfileList() } returns mockUserProfiles
      every { getUserProfile(any(), any()) } coAnswers
          {
            secondArg<(UserProfile?) -> Unit>().invoke(mockUserProfiles[0])
          }
      every { removeFollowing(any(), any()) } just Runs
      every { addFollowing(any(), any()) } just Runs
    }

    homevm = mockk {
      every { setSearchFilter(any()) } just Runs
      every { setSearchQuery(any()) } just Runs
      every { filteredItineraryList.value } returns mockItineraries
      every { filteredItineraryList } returns MutableLiveData(emptyList())
    }

    // Setting up the test composition
    composeTestRule.setContent {
      UserView(
          profile = MutableUserProfile(mutableStateOf(mockUserProfiles[1])),
          navigation = mockNavigation,
          userMail = "example@gmail.com",
          homeViewModel = homevm,
          test = true)
    }

    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("NoTripsText").assertIsDisplayed()
  }
}
