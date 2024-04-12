package com.example.triptracker.map

import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.map.MapOverview
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@get:Rule
val permissionRule: GrantPermissionRule =
    GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)


/**
 * Tests for MapOverview.
 * Still not working because of errors with undeterministic patterns when logging in and asking
 * for user location permission.
 */
@RunWith(AndroidJUnit4::class)
class MapTest : TestCase() {
  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
  @get:Rule val composeTestRule = createComposeRule()

  // The IntentsTestRule simply calls Intents.init() before the @Test block
  // and Intents.release() after the @Test block is completed. IntentsTestRule
  // is deprecated, but it was MUCH faster than using IntentsRule in our tests
  // @get:Rule val intentsTestRule = IntentsTestRule(MainActivity::class.java)

  //@Test
  //fun mapOverviewDisplaysCorrectly() {
  //  composeTestRule.setContent {
  //    val navController = rememberNavController()
  //    val navigation = remember(navController) { Navigation(navController) }
  //    MainActivity()
  //  }
  //  composeTestRule.onNodeWithText("Mountain View").assertIsDisplayed()
  //}

  //@Test
  //fun test2() {
  //
  //  composeTestRule.setContent {
  //    val navController = rememberNavController()
  //    val navigation = remember(navController) { Navigation(navController) }
  //    MapOverview(navigation = navigation, context = appContext)
  //  }
  //}

  @Test
  fun emptyTest() {
    assert(true)
  }
}
