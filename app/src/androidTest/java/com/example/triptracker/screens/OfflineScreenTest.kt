package com.example.triptracker.screens

import android.content.Context
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.OfflineScreen
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OfflineScreenTest {

  @get:Rule val composeTestRule = createComposeRule()
  @RelaxedMockK private lateinit var navigation: Navigation
  @RelaxedMockK private lateinit var context: Context

  @get:Rule
  val permissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  @Before
  fun setUp() {
    navigation = mockk(relaxed = true)
    context = mockk(relaxed = true)
    every { navigation.navigateTo(any()) } returns Unit
    composeTestRule.setContent { OfflineScreen(navigation = navigation, onRetry = {}) }
  }

  @Test
  fun testOfflineScreen() {
    composeTestRule.onNodeWithContentDescription("Airplane Mode Active").assertIsDisplayed()
    composeTestRule
        .onNodeWithContentDescription("Refresh")
        .assertIsDisplayed()
        .assertHasClickAction()
        .performClick()
  }
}
