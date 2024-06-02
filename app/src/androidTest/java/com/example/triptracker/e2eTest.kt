package com.example.triptracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.network.Connection
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.userProfile.MockUserList
import com.example.triptracker.view.LoginScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.OfflineScreen
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.view.map.RecordScreen
import com.example.triptracker.view.profile.UserProfileEditScreen
import com.example.triptracker.view.profile.UserProfileFavourite
import com.example.triptracker.view.profile.UserProfileFollowers
import com.example.triptracker.view.profile.UserProfileFollowing
import com.example.triptracker.view.profile.UserProfileFriendsFinder
import com.example.triptracker.view.profile.UserProfileMyTrips
import com.example.triptracker.view.profile.UserProfileOverview
import com.example.triptracker.view.profile.UserProfileSettings
import com.example.triptracker.view.profile.UserView
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
    val profile = MutableUserProfile()


    //setup navhostcontroller

    @Before
    fun setup() {

        mockNavigation = mockk(relaxed = true)
        userProfilevm = mockk(relaxed = true)
        homevm = mockk(relaxed = true)
        connection = mockk(relaxed = true)


        every { mockNavigation.getTopLevelDestinations() } returns
                listOf(
                    TopLevelDestination(Route.HOME, Icons.Outlined.Home, "Home"),
                    TopLevelDestination(Route.MAPS, Icons.Outlined.Place, "Maps"),
                    TopLevelDestination(Route.RECORD, Icons.Outlined.RadioButtonChecked, "Record"),
                    TopLevelDestination(Route.PROFILE, Icons.Outlined.Person, "Profile"),
                )



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
            val navController = rememberNavController()
            val navigation = remember(navController) { Navigation(navController) }
            NavHost(
                navController = navController,
                startDestination = Route.PROFILE,
            ) {
                composable(Route.HOME) { HomeScreen(navigation) }
                composable(Route.FRIENDS) {
                    UserProfileFriendsFinder(navigation = navigation, profile = profile)
                }
                composable(Route.PROFILE) {
                    UserProfileOverview(navigation = navigation, profile = profile)
                }
            }

        }

        composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()
        composeTestRule.onNodeWithTag("FriendsButton").assertHasClickAction()
        composeTestRule.onNodeWithTag("FriendsButton").performClick()

        //verify that the favorites screen is open





    }






}