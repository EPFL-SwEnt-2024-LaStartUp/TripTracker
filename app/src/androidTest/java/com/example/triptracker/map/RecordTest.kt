package com.example.triptracker.map

import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.MainActivity
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.map.RecordScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordTest {

//  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()
//
//  @Test
//  fun openRecordScreen() {
//    val scenario = ActivityScenario.launch(MainActivity::class.java)
//    scenario.moveToState(Lifecycle.State.RESUMED)
//    scenario.onActivity { activity ->
//      // Set up your Compose UI within the test method
//      composeTestRule.setContent {
//        val navController = rememberNavController()
//        val navigation = remember(navController) { Navigation(navController) }
//        RecordScreen(context = activity, navigation = navigation)
//      }
//
//      // Perform actions on the UI
//      composeTestRule.onNodeWithText("Allow").performClick()
//      composeTestRule.onNodeWithText("While using the app").performClick()
//      // Add assertions if needed
//      // For example:
//      // composeTestRule.onNodeWithText("Record").assertExists()
//      // composeTestRule.onNodeWithText("Start Recording").assertExists()
//    }
//  }
}
