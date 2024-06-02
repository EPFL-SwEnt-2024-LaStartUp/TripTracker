package com.example.triptracker.map

import android.content.Context
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.network.Connection
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.map.RecordScreen
import com.example.triptracker.viewmodel.RecordViewModel
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DenyPermissionRule(private val permission: String) : TestWatcher() {
  private val instrumentation = InstrumentationRegistry.getInstrumentation()

  override fun starting(description: Description?) {
    super.starting(description)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      instrumentation.uiAutomation.executeShellCommand(
          "pm revoke ${instrumentation.targetContext.packageName} $permission")
    }
  }
}

fun denyPermission() {
  val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
  val denyButton = device.findObject(UiSelector().text("Don't Allow"))
  if (denyButton.exists()) {
    denyButton.click()
  }
}

class RecordTestNoPermission {

  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule
  val denyPermissionRule = DenyPermissionRule(android.Manifest.permission.ACCESS_FINE_LOCATION)

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK private lateinit var mockViewModel: RecordViewModel
  @RelaxedMockK private lateinit var mockNavigation: Navigation
  @RelaxedMockK private lateinit var mockItineraryRepository: ItineraryRepository

  val mockList = MockItineraryList()
  val mockItineraries = mockList.getItineraries()

  val mockConnection: Connection = mockk(relaxed = true)

  @Before
  fun setUp() {
    mockViewModel = mockk(relaxed = true)
    mockNavigation = mockk(relaxed = true)
    mockItineraryRepository = mockk(relaxed = true)

    every { mockNavigation.getTopLevelDestinations()[2] } returns
        TopLevelDestination(Route.RECORD, Icons.Outlined.RadioButtonChecked, "Record")
  }

  @Test
  fun testRecordScreenDisplay() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns false
    every { mockViewModel.addSpotClicked.value } returns false

    // set every other value
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns "Title"
    every { mockViewModel.startDate.value } returns "2021-10-10"
    every { mockViewModel.endDate.value } returns "2021-10-10"

    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(
          context = appContext,
          viewModel = mockViewModel,
          navigation = mockNavigation,
          connection = mockConnection)
    }

    denyPermission()

    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()
  }
}
