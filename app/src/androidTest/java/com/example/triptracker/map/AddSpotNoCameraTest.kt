package com.example.triptracker.map

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.core.app.ActivityCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.triptracker.view.AllowCameraPermission
import com.example.triptracker.view.LaunchCameraPermissionRequest
import com.example.triptracker.view.checkForCameraPermission
import com.example.triptracker.view.map.TakePicture
import io.mockk.every
import io.mockk.mockk
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddSpotNoCameraTest {
  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
  @get:Rule val composeTestRule = createComposeRule()

  private val mockContext = mockk<Context>()

  @Test
  fun noCameraAllowedTest() {

    every { ActivityCompat.checkSelfPermission(mockContext, Manifest.permission.CAMERA) } returns
        android.content.pm.PackageManager.PERMISSION_DENIED

    val hasCameraPermission = checkForCameraPermission(mockContext)

    TestCase.assertEquals(false, hasCameraPermission)

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
          context = mockContext)
    }

    // Check if the camera is displayed
    composeTestRule.onNodeWithTag("CameraView").assertDoesNotExist()
    composeTestRule.onNodeWithTag("TakePictureButton").assertDoesNotExist()
  }

  @Test
  fun allowCameraPermissionTest() {
    composeTestRule.setContent {
      AllowCameraPermission(
          onPermissionGranted = { /*NOTHING*/}, onPermissionDenied = { /*NOTHING*/})
    }
    composeTestRule.onNodeWithTag("AllowCameraPermissionButton").assertExists()
    composeTestRule.onNodeWithTag("NoAllowCameraPermissionButton").assertExists()

    composeTestRule.onNodeWithTag("AllowCameraPermissionButton").performClick()
  }

  @Test
  fun noAllowCameraPermissionTest() {
    composeTestRule.setContent {
      AllowCameraPermission(
          onPermissionGranted = { /*NOTHING*/}, onPermissionDenied = { /*NOTHING*/})
    }
    composeTestRule.onNodeWithTag("AllowCameraPermissionButton").assertExists()
    composeTestRule.onNodeWithTag("NoAllowCameraPermissionButton").assertExists()

    composeTestRule.onNodeWithTag("NoAllowCameraPermissionButton").performClick()
  }

  @Test
  fun runPermissionRequestsAllowedTest() {
    every { ActivityCompat.checkSelfPermission(mockContext, Manifest.permission.CAMERA) } returns
        android.content.pm.PackageManager.PERMISSION_DENIED

    composeTestRule.setContent { LaunchCameraPermissionRequest(mockContext) }
    composeTestRule.onNodeWithTag("AllowCameraPermissionButton").assertExists()
    composeTestRule.onNodeWithTag("NoAllowCameraPermissionButton").assertExists()

    composeTestRule.onNodeWithTag("AllowCameraPermissionButton").performClick()
  }

  @Test
  fun runPermissionRequestsNotAllowedTest() {
    every { ActivityCompat.checkSelfPermission(mockContext, Manifest.permission.CAMERA) } returns
        android.content.pm.PackageManager.PERMISSION_DENIED

    composeTestRule.setContent { LaunchCameraPermissionRequest(mockContext) }
    composeTestRule.onNodeWithTag("AllowCameraPermissionButton").assertExists()
    composeTestRule.onNodeWithTag("NoAllowCameraPermissionButton").assertExists()

    composeTestRule.onNodeWithTag("NoAllowCameraPermissionButton").performClick()
  }
}
