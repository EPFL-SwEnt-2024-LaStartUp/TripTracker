package com.example.triptracker.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationBarTest {
  private val navigation: Navigation = mockk<Navigation>()

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule
  val permissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  @Before
  fun setUp() {
    every { navigation.getTopLevelDestinations() } returns
        listOf(
            TopLevelDestination(Route.HOME, Icons.Outlined.Home, "Home"),
            TopLevelDestination(Route.MAPS, Icons.Outlined.Place, "Maps"),
            TopLevelDestination(Route.RECORD, Icons.Outlined.RadioButtonChecked, "Record"),
            TopLevelDestination(Route.PROFILE, Icons.Outlined.Person, "Profile"),
        )

    every { navigation.getCurrentDestination() } returns
        TopLevelDestination(Route.PROFILE, Icons.Outlined.Person, "Profile")

    every { navigation.navigateTo(any()) } returns Unit

      composeTestRule.setContent {
          NavigationBar(navigation = navigation)
      }
  }

  @Test
  fun testNavigationBarRetrievesScreens() {
    verify { navigation.getTopLevelDestinations() }
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
