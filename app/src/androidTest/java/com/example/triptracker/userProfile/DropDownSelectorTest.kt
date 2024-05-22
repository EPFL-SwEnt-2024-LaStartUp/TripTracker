package com.example.triptracker.userProfile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.view.profile.Interests
import com.example.triptracker.view.profile.Languages
import com.example.triptracker.view.profile.TravelStyle
import com.example.triptracker.view.profile.subviews.DropdownSelector
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DropDownSelectorTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun languagesSelectorTest() {
    // Test the drop down selector
    composeTestRule.setContent {
      var selectedLanguages by remember {
        mutableStateOf(listOf(Languages.Dutch.toString(), Languages.Arabic.toString()))
      }

      DropdownSelector(
          label = "Languages",
          options = Languages.entries,
          selectedOptions = selectedLanguages,
          onOptionSelected = { selectedLanguages = it },
          placeholder = "No Languages")
    }
    // we check that the "Language" label is displayed
    composeTestRule.onNodeWithTag("Label").assertIsDisplayed().assertTextEquals("Languages")
    // we check that the selected languages are displayed
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("Dutch, Arabic")
    // we check that the drop down button is displayed and we click on it to open the drop down menu
    composeTestRule.onNodeWithTag("DropDownButton").assertIsDisplayed().performClick()
    // we check that the drop down menu is displayed
    composeTestRule.onNodeWithTag("DropDownMenu").assertIsDisplayed()
    // we check that the drop down menu items are displayed
    composeTestRule.onNodeWithTag("DropDownMenuItemDutch").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemEnglish").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemFrench").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemGerman").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemItalian").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemJapanese").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemKorean").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemPortuguese").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemRussian").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemSpanish").assertIsDisplayed()
    // we check that once unselected, the selected language is removed from the drop down text field
    composeTestRule.onNodeWithTag("DropDownMenuItemArabic").assertIsDisplayed().performClick()
    composeTestRule.onNodeWithTag("DropDownTextField").assertIsDisplayed().assertTextEquals("Dutch")
    // we check that once selected, the selected language is added to the drop down text field
    composeTestRule.onNodeWithTag("DropDownMenuItemChinese").assertIsDisplayed().performClick()
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("Dutch, Chinese")
  }

  @Test
  fun emptyLanguagesSelectorTest() {
    composeTestRule.setContent {
      var selectedLanguages by remember { mutableStateOf(emptyList<String>()) }

      DropdownSelector(
          label = "Languages",
          options = Languages.entries,
          selectedOptions = selectedLanguages,
          onOptionSelected = { selectedLanguages = it },
          placeholder = "No Languages")
    }
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("No Languages")
  }

  @Test
  fun travelStyleSelectorTest() {
    // Test the drop down selector
    composeTestRule.setContent {
      var selectedTravelStyle by remember {
        mutableStateOf(
            listOf(TravelStyle.Adventurous.toString(), TravelStyle.Backpacking.toString()))
      }

      DropdownSelector(
          label = "Travel Style",
          options = TravelStyle.entries,
          selectedOptions = selectedTravelStyle,
          onOptionSelected = { selectedTravelStyle = it },
          placeholder = "No Travel Style")
    }
    // we check that the "Travel Style" label is displayed
    composeTestRule.onNodeWithTag("Label").assertIsDisplayed().assertTextEquals("Travel Style")
    // we check that the selected languages are displayed
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("Adventurous, Backpacking")
    // we check that the drop down button is displayed and we click on it to open the drop down menu
    composeTestRule.onNodeWithTag("DropDownButton").assertIsDisplayed().performClick()
    // we check that the drop down menu is displayed
    composeTestRule.onNodeWithTag("DropDownMenu").assertIsDisplayed()
    // we check that the drop down menu items are displayed
    composeTestRule.onNodeWithTag("DropDownMenuItemBackpacking").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemCultural").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemGroup").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemLuxury").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemRelaxing").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemRoadTrip").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemSolo").assertIsDisplayed()
    // we check that once unselected, the selected travel style is removed from the drop down text
    // field
    composeTestRule.onNodeWithTag("DropDownMenuItemAdventurous").assertIsDisplayed().performClick()
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("Backpacking")
    // we check that once selected, the selected travel style is added to the drop down text field
    composeTestRule.onNodeWithTag("DropDownMenuItemCamping").assertIsDisplayed().performClick()
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("Backpacking, Camping")
  }

  @Test
  fun emptyTravelStyleSelectorTest() {
    composeTestRule.setContent {
      var selectedTravelStyle by remember { mutableStateOf(emptyList<String>()) }

      DropdownSelector(
          label = "TravelStyle",
          options = TravelStyle.entries,
          selectedOptions = selectedTravelStyle,
          onOptionSelected = { selectedTravelStyle = it },
          placeholder = "No Travel Style")
    }
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("No Travel Style")
  }

  @Test
  fun interestsSelectorTest() {
    // Test the drop down selector
    composeTestRule.setContent {
      var selectedInterests by remember {
        mutableStateOf(listOf(Interests.Culture.toString(), Interests.Cycling.toString()))
      }

      DropdownSelector(
          label = "Interests",
          options = Interests.entries,
          selectedOptions = selectedInterests,
          onOptionSelected = { selectedInterests = it },
          placeholder = "No Interests")
    }
    // we check that the "Interests" label is displayed
    composeTestRule.onNodeWithTag("Label").assertIsDisplayed().assertTextEquals("Interests")
    // we check that the selected languages are displayed
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("Culture, Cycling")
    // we check that the drop down button is displayed and we click on it to open the drop down menu
    composeTestRule.onNodeWithTag("DropDownButton").assertIsDisplayed().performClick()
    // we check that the drop down menu is displayed
    composeTestRule.onNodeWithTag("DropDownMenu").assertIsDisplayed()
    // we check that the drop down menu items are displayed
    composeTestRule.onNodeWithTag("DropDownMenuItemCycling").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemHiking").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemNature").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemPhotography").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemRunning").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DropDownMenuItemWalking").assertIsDisplayed()
    // we check that once unselected, the selected interest is removed from the drop down text field
    composeTestRule.onNodeWithTag("DropDownMenuItemCulture").assertIsDisplayed().performClick()
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("Cycling")
    // we check that once selected, the selected interest is added to the drop down text field
    composeTestRule.onNodeWithTag("DropDownMenuItemFood").assertIsDisplayed().performClick()
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("Cycling, Food")
  }

  @Test
  fun emptyInterestsSelectorTest() {
    composeTestRule.setContent {
      var selectedInterests by remember { mutableStateOf(emptyList<String>()) }

      DropdownSelector(
          label = "Interests",
          options = Interests.entries,
          selectedOptions = selectedInterests,
          onOptionSelected = { selectedInterests = it },
          placeholder = "No Interests")
    }
    composeTestRule
        .onNodeWithTag("DropDownTextField")
        .assertIsDisplayed()
        .assertTextEquals("No Interests")
  }
}
