package com.example.triptracker.map

import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.triptracker.MainActivity
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.map.RecordScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.Manifest
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class RecordTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)


    @Before
    fun setUp() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val options = FirebaseOptions.Builder()
            .setProjectId("triptracker-8adee") // replace with your Firebase project ID
            .setApplicationId("1:273594028474:android:25ff5774b0a482d15e0d83") // replace with your Firebase App ID
            .setApiKey("AIzaSyBGpk-ov2fPoieibzUisTzCB-EXav4pkKg\n") // replace with your Firebase API Key
            .setDatabaseUrl("https://default.firebaseio.com") // point to the local Firebase emulator
            .build()

        FirebaseApp.initializeApp(context, options, "test")

    }

//    @Test
//    fun openRecordScreen() {
//      val scenario = ActivityScenario.launch(MainActivity::class.java)
//      scenario.moveToState(Lifecycle.State.RESUMED)
//      scenario.onActivity { activity ->
//        // Set up your Compose UI within the test method
//        composeTestRule.setContent {
//          val navController = rememberNavController()
//          val navigation = remember(navController) { Navigation(navController) }
//          RecordScreen(context = activity, navigation = navigation)
//        }
//
//        // Perform actions on the UI
//        composeTestRule.onNodeWithText("Allow").performClick()
//        composeTestRule.onNodeWithText("While using the app").performClick()
//        // Add assertions if needed
//        // For example:
//        // composeTestRule.onNodeWithText("Record").assertExists()
//        // composeTestRule.onNodeWithText("Start Recording").assertExists()
//      }
//    }

    @Test
    fun testTitleDisplayed() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            val navigation = remember(navController) { Navigation(navController) }
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext

            RecordScreen(context = appContext , navigation = navigation)
        }

        composeTestRule.onNodeWithText("Record").assertExists()
    }
}
