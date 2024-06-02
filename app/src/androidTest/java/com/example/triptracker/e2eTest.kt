package com.example.triptracker

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.network.Connection
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.userProfile.MockUserList
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.profile.UserProfileOverview
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class E2ETest {

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
    fun profileScreenFlow(){

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
        composeTestRule.onNodeWithTag("FavoritesButton").assertHasClickAction()
        composeTestRule.onNodeWithTag("FavoritesButton").performClick()



    }






}