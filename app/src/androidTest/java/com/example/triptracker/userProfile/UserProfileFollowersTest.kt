//package com.example.triptracker.userProfile
//
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.lifecycle.MutableLiveData
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.triptracker.model.profile.UserProfile
//import com.example.triptracker.model.repository.UserProfileRepository
//import com.example.triptracker.screens.userProfile.UserProfileFollowersScreen
//import com.example.triptracker.view.Navigation
//import com.example.triptracker.view.profile.UserProfileFollowers
//import com.example.triptracker.viewmodel.UserProfileViewModel
//import io.github.kakaocup.compose.node.element.ComposeScreen
//import io.mockk.Runs
//import io.mockk.every
//import io.mockk.impl.annotations.RelaxedMockK
//import io.mockk.junit4.MockKRule
//import io.mockk.just
//import io.mockk.mockk
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class UserProfileFollowersTest {
//  @get:Rule val composeTestRule = createComposeRule()
//
//  // @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()  // Ensure LiveData executes
//  // immediately
//
//  @get:Rule val mockkRule = MockKRule(this)
//
//  @RelaxedMockK private lateinit var mockViewModel: UserProfileViewModel
//  @RelaxedMockK private lateinit var mockUserProfileRepository: UserProfileRepository
//  @RelaxedMockK private lateinit var mockNavigation: Navigation
//
//  private val mockList = MockUserList()
//  private val mockUserProfiles = mockList.getUserProfiles()
//
//  private lateinit var filteredProfilesLiveData: MutableLiveData<List<UserProfile>>
//
//  @Before
//  fun setUp() { // Mocking necessary components
//    mockNavigation = mockk(relaxed = true)
//    mockViewModel = mockk(relaxUnitFun = true)
//    mockUserProfileRepository = mockk(relaxUnitFun = true)
//
//    // Setup the LiveData with initial data or as per test case requirements
//    filteredProfilesLiveData = MutableLiveData<List<UserProfile>>(mockUserProfiles)
//
//    every { mockViewModel.filteredUserProfileList } returns filteredProfilesLiveData
//  }
//
//  @Test
//  fun componentsAreCorrectlyDisplayed() {
//    mockViewModel = mockk {
//      every { getUserProfileList() } returns mockUserProfiles
//      every { getUserProfile(any(), any()) } coAnswers
//          {
//            secondArg<(UserProfile?) -> Unit>().invoke(mockUserProfiles[6])
//          }
//      every { setListToFilter(any()) } just Runs
//      every { mockUserProfileRepository.getAllUserProfiles(any()) } answers {
//          // Invoke the callback with mock data
//          val callback = arg<(List<UserProfile>) -> Unit>(0)
//          callback(mockUserProfiles)
//        }
//    }
//
//    // Setting up the test composition
//    composeTestRule.setContent {
//      UserProfileFollowers(navigation = mockNavigation, userProfileViewModel = mockViewModel)
//    }
//    ComposeScreen.onComposeScreen<UserProfileFollowersScreen>(composeTestRule) {
//      // Test the UI elements
//      followersTitle {
//        assertIsDisplayed()
//        assertTextEquals("Followers")
//      }
//      //      goBackButton {
//      //        assertIsDisplayed()
//      //        assertHasClickAction()
//      //      }
//      //      followersList { assertIsDisplayed() }
//      //      followerProfile { assertIsDisplayed() }
//    }
//  }
//
//  //  @Test
//  //  fun removeButtonWorks() {
//  //    every { mockUserProfileRepository.getAllUserProfiles() } returns mockUserProfiles
//  //    every { mockViewModel.getUserProfileList() } returns mockUserProfiles
//  //    // Setting up the test composition
//  //    composeTestRule.setContent {
//  //      UserProfileFollowers(navigation = mockNav, viewModel = mockViewModel)
//  //    }
//  //    ComposeScreen.onComposeScreen<UserProfileFollowersScreen>(composeTestRule) {
//  //      removeButton {
//  //        assertIsDisplayed()
//  //        assertTextEquals("Remove")
//  //        assertHasClickAction()
//  //        performClick()
//  //        assertTextEquals("Undo")
//  //        performClick()
//  //        assertTextEquals("Remove")
//  //      }
//  //    }
//  //  }
//  //
//  //  @Test
//  //  fun backButtonWorks() {
//  //    every { mockUserProfileRepository.getAllUserProfiles() } returns mockUserProfiles
//  //    every { mockViewModel.getUserProfileList() } returns mockUserProfiles
//  //    // Setting up the test composition
//  //    composeTestRule.setContent {
//  //      UserProfileFollowers(navigation = mockNav, viewModel = mockViewModel)
//  //    }
//  //    ComposeScreen.onComposeScreen<UserProfileFollowersScreen>(composeTestRule) {
//  //      // Test the UI elements
//  //      goBackButton {
//  //        assertIsDisplayed()
//  //        assertHasClickAction()
//  //        performClick()
//  //      }
//  //    }
//  //  }
//}
