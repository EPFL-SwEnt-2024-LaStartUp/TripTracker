package com.example.triptracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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

    var profile= MutableUserProfile()

    @Test
    fun profileScreenFlow(){


        composeTestRule.setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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

        }

        composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()
        composeTestRule.onNodeWithTag("FriendsButton").assertHasClickAction()
        composeTestRule.onNodeWithTag("FriendsButton").performClick()
        //verify that the favorites screen is open





    }






}