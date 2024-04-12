package com.example.triptracker.map

import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.invokeGlobalAssertions
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.triptracker.MainActivity
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.home.HomeScreen
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.view.map.MapOverviewPreview
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapTest : TestCase() {
  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
  @get:Rule val composeTestRule = createComposeRule()

  // The IntentsTestRule simply calls Intents.init() before the @Test block
  // and Intents.release() after the @Test block is completed. IntentsTestRule
  // is deprecated, but it was MUCH faster than using IntentsRule in our tests
  //@get:Rule val intentsTestRule = IntentsTestRule(MainActivity::class.java)

  @Test
  fun mapOverviewDisplaysCorrectly() {
    composeTestRule.setContent { MapOverviewPreview() }
    composeTestRule.onNodeWithText("Allow").performClick()
    composeTestRule.onNodeWithText("While using the app").performClick()



  }

  //@Test
  //fun test2() {
  //  composeTestRule.setContent {
  //    val navController = rememberNavController()
  //    val navigation = remember(navController) { Navigation(navController) }
  //    HomeScreen(navigation)
  //    //MapOverview(navigation = navigation, context = appContext)
  //  }
  //  composeTestRule.onNodeWithText("Map").performClick()
  //
  //}

}
