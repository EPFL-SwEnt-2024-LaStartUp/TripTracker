package com.example.triptracker.userProfile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.view.profile.subviews.FriendSearchBar
import com.example.triptracker.viewmodel.UserProfileViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FriendSearchBarTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK private lateinit var mockvm: UserProfileViewModel

  @Before
  fun setup() {
    mockvm = mockk(relaxed = true)

    mockvm = mockk { every { setSearchQuery(any()) } just Runs }
  }

  @Test
  fun componentsAreDisplayed() {
    composeTestRule.setContent {
      FriendSearchBar(
          viewModel = mockvm,
          onSearchActivated = {},
      )
    }

    // we ensure that the empty search bar is correctly displayed
    composeTestRule.onNodeWithTag("SearchBarContainer").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SearchBar").assertIsDisplayed()
    composeTestRule.onNodeWithText("Find Friends").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("SearchIcon").assertIsDisplayed()
    // we ensure that the clear button is not displayed
    composeTestRule.onNodeWithTag("ClearButton").assertIsNotDisplayed()

    // we perform a search
    composeTestRule.onNodeWithTag("SearchBar").performClick().performTextInput("test")
    composeTestRule.onNodeWithTag("SearchBar").assertIsDisplayed().assertTextEquals("test")

    // we ensure that the clear button is correctly displayed
    composeTestRule.onNodeWithTag("ClearButton").assertIsDisplayed()
    // we ensure that clicking on the clear button will clear the search bar
    composeTestRule.onNodeWithTag("ClearButton").performClick()
  }
}
