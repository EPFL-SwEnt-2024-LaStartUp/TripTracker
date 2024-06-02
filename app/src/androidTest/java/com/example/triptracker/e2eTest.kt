package com.example.triptracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.profile.UserProfileEditScreen
import com.example.triptracker.view.profile.UserProfileFavourite
import com.example.triptracker.view.profile.UserProfileFriendsFinder
import com.example.triptracker.view.profile.UserProfileMyTrips
import com.example.triptracker.view.profile.UserProfileOverview
import com.example.triptracker.view.profile.UserProfileSettings
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class E2ETest {

  @get:Rule val composeTestRule = createComposeRule()

  var profile = MutableUserProfile()

  /*
     Test to check the flow of the profile screen navigating through different screens
  */
  @Test
  fun profileScreenFlow() {

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
          composable(Route.MYTRIPS) {
            UserProfileMyTrips(
                navigation = navigation,
                userProfile = profile,
            )
          }
          composable(Route.EDIT) {
            UserProfileEditScreen(navigation = navigation, profile = profile)
          }
          composable(Route.SETTINGS) { UserProfileSettings(navigation) }
          composable(Route.FAVORITES) {
            UserProfileFavourite(navigation = navigation, userProfile = profile)
          }
        }
      }
    }

    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // travel to the friends screen
    composeTestRule.onNodeWithTag("FriendsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("FriendsButton").performClick()

    // verify that the favorites screen is open
    composeTestRule.onNodeWithTag("FriendsFinderScreen").assertIsDisplayed()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // travel to the favorites
    composeTestRule.onNodeWithTag("FavoritesButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("FavoritesButton").performClick()

    // verify that the favorites screen is open
    composeTestRule.onNodeWithTag("UserProfileFavouriteScreen").assertIsDisplayed()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // travel to the settings
    composeTestRule.onNodeWithTag("SettingsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("SettingsButton").performClick()

    // verify that the followers screen is open
    composeTestRule.onNodeWithTag("UserProfileSettings").assertIsDisplayed()

    // Change privacy to private
    composeTestRule.onNodeWithTag("ProfilePrivacyButton").performClick()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // travel to the my trips
    composeTestRule.onNodeWithTag("MyTripsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("MyTripsButton").performClick()

    // verify that the my trips screen is open
    composeTestRule.onNodeWithTag("UserProfileMyTripsScreen").assertIsDisplayed()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()

    // Edit profile
    composeTestRule.onNodeWithContentDescription("Edit").assertHasClickAction()
    composeTestRule.onNodeWithContentDescription("Edit").performClick()

    // verify that the edit screen is open
    composeTestRule.onNodeWithTag("UserProfileEditScreen").assertIsDisplayed()

    // input into the field
    composeTestRule.onNodeWithText("Save").isDisplayed()

    // Go back and assert we are in the profile screen
    composeTestRule.onNodeWithTag("GoBackButton").performClick()
    composeTestRule.onNodeWithTag("ProfileOverview").assertIsDisplayed()
  }

  /*
   Test to check the flow of the app navigating through different places, to be implemented
  */
  @Test fun appFlow() {}
}
