package com.example.triptracker.map

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.screens.AddSpotScreen
import com.example.triptracker.view.map.AddSpot
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.model.LatLng
import io.github.kakaocup.compose.node.element.ComposeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddSpotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setUp() {
    composeTestRule.setContent {
      AddSpot(
          recordViewModel = RecordViewModel(),
          latLng = LatLng(46.519879, 6.560632),
      )
    }
  }

  @Test
  fun everythingDisplayed() {
    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
      assertIsDisplayed()

      close { assertIsDisplayed() }

      title { assertIsDisplayed() }

      locationRow {
        assertIsDisplayed()
        performClick()
      }

      description {
        assertIsDisplayed()
        performClick()
      }

      pictures {
        performScrollTo()
        assertIsDisplayed()
      }

      saveButton {
        performScrollTo()
        assertIsDisplayed()
      }
    }
  }

  @Test
  fun addPicturesTest() {
    Intents.init()
    val imageUri = Uri.parse("content://media/external/images/media/1")
    // Creates an intent and mock the answer. Since the picture picker is not inside our project but
    // is another activity launched by the phone this implies that the intent is not internal
    intending(not(isInternal()))
        .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent().setData(imageUri)))
    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
      pictures { performClick() }
      editPicture { assertIsDisplayed() }
    }
    Intents.release()
  }

  @Test
  fun dropDownTestOk() {
    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
      locationRow { assertIsDisplayed() }
      locationText {
        assertIsDisplayed()

        performTextClearance()

        performTextInput("ecole polytechnique federale")
        composeTestRule.onNodeWithTag("LocationDropDown").performClick()
      }
      runBlocking { delay(2000) }
      locationText {
        assertIsDisplayed()
        assertTextContains("École Polytechnique Fédérale de Lausanne", substring = true)
      }
    }
  }

  @Test
  fun dropDownTestNotOk() {
    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
      locationRow { assertIsDisplayed() }
      locationText {
        assertIsDisplayed()

        performTextClearance()

        performTextInput("statue")
        composeTestRule.onNodeWithTag("LocationDropDown").performClick()
      }

      locationText {
        assertIsDisplayed()
        assertTextContains("")
      }
    }
  }

  @Test
  fun quitPageTest() {
    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
      close {
        assertIsDisplayed()
        performClick()
      }
    }
  }

  @Test
  fun fillSpotAndSaveTest() {
    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
      locationText {
        assertIsDisplayed()

        performTextClearance()

        performTextInput("EPFL")
        composeTestRule.onNodeWithTag("LocationDropDown").performClick()
      }

      description {
        assertIsDisplayed()
        performClick()
      }

      descriptionText {
        assertIsDisplayed()
        performTextClearance()
        performTextInput("This is a test")
      }

      saveButton {
        performScrollTo()
        assertIsDisplayed()
        performClick()
      }
    }
  }
}
