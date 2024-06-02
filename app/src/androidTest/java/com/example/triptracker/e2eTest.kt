package com.example.triptracker

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.userProfile.MockUserList
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class E2ETest {


    @get:Rule
    val composeTestRule = createComposeRule()
    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var mockNav: Navigation
    @RelaxedMockK
    private lateinit var mockViewModel: HomeViewModel
    @RelaxedMockK
    private lateinit var mockItineraryRepository: ItineraryRepository
    // @RelaxedMockK private  lateinit var mockUserProfileRepository: UserProfileRepository
    @RelaxedMockK
    private lateinit var mockUserProfileRepository: UserProfileRepository
    @RelaxedMockK
    private lateinit var mockUserProfileViewModel: UserProfileViewModel
    // private lateinit var mockUserProfileViewModel: UserProfileViewModel
    @RelaxedMockK
    private lateinit var mockProfile: MutableUserProfile

    val mockList = MockItineraryList()
    val mockItineraries = mockList.getItineraries()

    val mockUserList = MockUserList()
    val mockUsers = mockUserList.getUserProfiles()
    val mockMail = "test@gmail.com"


    @Before
    fun setUp() {

    }


    @Test
    fun navigateUserScreen(){

        composeTestRule.setContent {
            HomeScreen(
                navigation = mockNav,
                homeViewModel = mockViewModel,
                userProfileViewModel = mockUserProfileViewModel,
                test = true)
        }




    }






}