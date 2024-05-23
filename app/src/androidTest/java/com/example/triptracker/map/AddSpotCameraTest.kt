package com.example.triptracker.map

import android.Manifest
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.example.triptracker.view.map.TakePicture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddSpotCameraTest {
  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule
  val permissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

  @Before fun setUp() {}

  @Test
  fun everythingDisplayed() {
    composeTestRule.setContent {
      val file = File("/storage/emulated/0/Download/TripTracker/")
      file.mkdirs()

      // Create the executor for the camera
      val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

      TakePicture(
          outputDirectory = file,
          executor = cameraExecutor,
          onCaptureClosedSuccess = {},
          onCaptureClosedError = {},
          context = appContext)
    }

    // Check if the camera is displayed
    composeTestRule.onNodeWithTag("CameraView").assertExists()
  }

  @Test
  fun takePictureTest() {
    val onCaptureClosedSuccessCalled = mutableStateOf(false)
    val onCaptureClosedErrorCalled = mutableStateOf(false)
    composeTestRule.setContent {
      val file = File("/storage/emulated/0/Download/TripTracker/")
      file.mkdirs()

      // Create the executor for the camera
      val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

      TakePicture(
          outputDirectory = file,
          executor = cameraExecutor,
          onCaptureClosedSuccess = { onCaptureClosedSuccessCalled.value = true },
          onCaptureClosedError = { onCaptureClosedErrorCalled.value = true },
          context = appContext)
    }

    // Check if the camera is displayed
    composeTestRule.onNodeWithTag("CameraView").assertExists()
    composeTestRule.onNodeWithTag("TakePictureButton").assertExists()
    composeTestRule.onNodeWithTag("TakePictureButton").performClick()
    composeTestRule.onNodeWithTag("TakePictureButton").isNotDisplayed()
    composeTestRule.onNodeWithTag("CameraView").isNotDisplayed()
    assert(!onCaptureClosedErrorCalled.value)
  }

  @Test
  fun closeCameraTest() {
    val onCaptureClosedSuccessCalled = mutableStateOf(false)
    val onCaptureClosedErrorCalled = mutableStateOf(false)
    composeTestRule.setContent {
      val file = File("/storage/emulated/0/Download/TripTracker/")
      file.mkdirs()

      // Create the executor for the camera
      val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

      TakePicture(
          outputDirectory = file,
          executor = cameraExecutor,
          onCaptureClosedSuccess = { onCaptureClosedSuccessCalled.value = true },
          onCaptureClosedError = { onCaptureClosedErrorCalled.value = true },
          context = appContext)
    }

    // Check if the camera is displayed
    composeTestRule.onNodeWithTag("CameraView").assertExists()
    composeTestRule.onNodeWithTag("TakePictureButton").assertExists()
    composeTestRule.onNodeWithTag("CloseCameraScreen").performClick()
    composeTestRule.onNodeWithTag("TakePictureButton").isNotDisplayed()
    composeTestRule.onNodeWithTag("CameraView").isNotDisplayed()
    assert(!onCaptureClosedSuccessCalled.value)
    assert(onCaptureClosedErrorCalled.value)
  }
}
