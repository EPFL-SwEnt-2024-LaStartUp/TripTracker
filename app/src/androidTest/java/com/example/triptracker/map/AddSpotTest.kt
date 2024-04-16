package com.example.triptracker.map

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.screens.AddSpotScreen
import com.example.triptracker.view.map.AddSpot
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.model.LatLng
import io.github.kakaocup.compose.node.element.ComposeScreen
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
      AddSpot(recordViewModel = RecordViewModel(), latLng = LatLng(46.519879, 6.560632))
    }
  }

  @Test
  fun everythingDisplayed() {
    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
      assertIsDisplayed()

      title { assertIsDisplayed() }

      locationRow { assertIsDisplayed() }

      description { assertIsDisplayed() }

      pictures { assertIsDisplayed() }

      saveButton { assertIsDisplayed() }
    }
  }

  @Test
  fun addPicturesTest() {
    Intents.init()
    val imageUri = Uri.parse("content://media/external/images/media/1")
    intending(not(isInternal()))
        .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent().setData(imageUri)))
    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
      pictures { performClick() }
      editPicture { assertIsDisplayed() }
    }
    Intents.release()
  }

  //  @Test
  //  fun dropDownTest() {
  //    ComposeScreen.onComposeScreen<AddSpotScreen>(composeTestRule) {
  //      locationRow { assertIsDisplayed() }
  //      locationText {
  //        assertIsDisplayed()
  //
  //        performTextClearance()
  //
  //        performTextInput("statue")
  //      }
  //
  //      inputLocationProposal {
  //        assertIsDisplayed()
  //        performClick()
  //      }
  //    }
  //  }
}
