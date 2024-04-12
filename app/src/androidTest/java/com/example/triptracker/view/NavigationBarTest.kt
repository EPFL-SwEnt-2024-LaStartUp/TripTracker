package com.example.triptracker.view

import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationBarTest {
  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule
  val permissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  @Before
  fun setUp() {
    composeTestRule.setContent {
      val navController = rememberNavController()
      val navigation = remember(navController) { Navigation(navController) }
      NavigationBar(navigation = navigation)
    }
  }

  @Test
  fun testNavigationBarHasTheFourMainTabs() {
    composeTestRule.onNodeWithText("Home").assertIsDisplayed()
    composeTestRule.onNodeWithText("Maps").assertIsDisplayed()
    composeTestRule.onNodeWithText("Record").assertIsDisplayed()
    composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
  }

  @Test
  fun testNavigationBarHasTheFourMainTabsClickable() {
    composeTestRule.onNodeWithText("Home").assertHasClickAction()
    composeTestRule.onNodeWithText("Maps").assertHasClickAction()
    composeTestRule.onNodeWithText("Record").assertHasClickAction()
    composeTestRule.onNodeWithText("Profile").assertHasClickAction()
  }

  // TODO test redirection on button clicked once problem with mockK resolved

}
