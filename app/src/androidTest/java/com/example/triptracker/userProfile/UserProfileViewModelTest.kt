package com.example.triptracker.userProfile

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileViewModelTest {
  @get:Rule val composeTestRule = createComposeRule()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule val mockkRule = MockKRule(this)

  // Relaxed mocks methods have a default implementation returning values

  @RelaxedMockK private lateinit var mockUserProfileRepository: UserProfileRepository

  private val mockList = MockUserList()
  private val mockUserProfiles = mockList.getUserProfiles()
  /**
   * This method is run before each test to set up the necessary mocks. It is used to initialize the
   * mocks and set up the necessary dependencies.
   *
   * Had to copy paste in everyTest so that could specify the mock data for each test: every {
   * mockItineraryRepository.getAllItineraries() } returns mockItineraries every {
   * mockViewModel.itineraryList } returns MutableLiveData(mockItineraries) // Setting up the test
   * composition composeTestRule.setContent { HomeScreen(navigation = mockNav, homeViewModel =
   * mockViewModel) }
   */
  @Before
  fun setUp() {
    // Mocking necessary components
    mockUserProfileRepository = mockk(relaxed = true)
    mockkStatic(Log::class)
  }

  @Test
  fun getUserProfileListTest() {
    every { mockUserProfileRepository.getAllUserProfiles() } returns mockUserProfiles
    val viewModel = UserProfileViewModel(mockUserProfileRepository)

    val userProfileList = viewModel.getUserProfileList()
    assert(userProfileList == mockUserProfiles)
  }

  @Test
  fun getAllUserProfilesFromDbTest() {
    // Have to repeat code to have specific mock data for each test!!
    every { mockUserProfileRepository.getAllUserProfiles() } returns mockUserProfiles
    val viewModel = UserProfileViewModel(mockUserProfileRepository)

    viewModel.getAllUserProfilesFromDb()
    assert(viewModel.getUserProfileList() == mockUserProfiles)
  }

  @Test
  fun addNewUserProfileToDbTest() {
    every { mockUserProfileRepository.addNewUserProfile(any()) } answers
        {
          Log.d("FirebaseConnection - UserProfileRepository", "User profile added successfully")
        }

    val viewModel = UserProfileViewModel(mockUserProfileRepository)
    val newUser = mockList.getNewMockUser()

    viewModel.addNewUserProfileToDb(newUser)
  }

  @Test
  fun updateUserProfileInDbTest() {
    every { mockUserProfileRepository.updateUserProfile(any()) } answers
        {
          Log.d("FirebaseConnection - UserProfileRepository", "User profile updated successfully")
        }

    val mockViewModel = UserProfileViewModel(mockUserProfileRepository)
    val user = mockList.getUserProfiles()[0]
    val updatedUser = user.copy(name = "David")

    mockViewModel.updateUserProfileInDb(updatedUser)
    verify {
      Log.d("FirebaseConnection - UserProfileRepository", "User profile updated successfully")
    }
  }

  @Test
  fun removeUserProfileInDbTest() {
    every { mockUserProfileRepository.removeUserProfile(any()) } answers
        {
          Log.d("FirebaseConnection - UserProfileRepository", "User profile removed successfully")
        }

    val mockViewModel = UserProfileViewModel(mockUserProfileRepository)
    val mail = mockList.getUserProfiles()[0].mail

    mockViewModel.removeUserProfileInDb(mail)
    verify {
      Log.d("FirebaseConnection - UserProfileRepository", "User profile removed successfully")
    }
  }
}
