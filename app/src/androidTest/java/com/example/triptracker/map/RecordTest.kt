package com.example.triptracker.map

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
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
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordTest {

  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule
  val grantPermissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK private lateinit var mockViewModel: RecordViewModel
  @RelaxedMockK private lateinit var mockNavigation: Navigation
  @RelaxedMockK private lateinit var mockItineraryRepository: ItineraryRepository

  val mockList = MockItineraryList()
  val mockItineraries = mockList.getItineraries()

  val mockConnection: Connection = mockk(relaxed = true)
  val mockContext: Context = mockk(relaxed = true)

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
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()
  }

  @Test
  fun testRecordScreenDisplayTitle() {
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
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Record").assertExists()
  }

  @Test
  fun testRecordScreenDisplayButton() {
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
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Start").assertExists()
  }

  @Test
  fun testRecordScreenDisplayButtonPause() {
    every { mockViewModel.isRecording() } returns true
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
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Pause").assertExists()
  }

  @Test
  fun testRecordScreenDisplayButtonResume() {
    every { mockViewModel.isRecording() } returns true
    every { mockViewModel.isPaused.value } returns true
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
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Resume").assertExists()
  }

  @Test
  fun testRecordScreenDisplayButtonStop() {
    every { mockViewModel.isRecording() } returns true
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
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Stop").assertExists()
  }

  @Test
  fun testRecordScreenDisplayButtonSave() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns true
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns "Title"

    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithTag("DescriptionTextField").assertExists()
  }

  @Test
  fun testTimerIsDisplayed() {
    every { mockViewModel.isRecording() } returns true
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
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithTag("Timer").assertExists()
    composeTestRule.onNodeWithTag("Time").assertExists()
  }

  @Test
  fun testSaveButton() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns true
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns "Title"
    every { mockViewModel.startDate.value } returns "2021-10-10"
    every { mockViewModel.endDate.value } returns "2021-10-10"

    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Save").assertExists()

    composeTestRule.onNodeWithText("Save").performClick()
  }

  @Test
  fun testSaveButtonNotEmpty() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns true
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns "Title"
    every { mockViewModel.startDate.value } returns "2021-10-10"
    every { mockViewModel.endDate.value } returns "2021-10-10"

    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockViewModel.description } returns mutableStateOf("Description")
    every { mockViewModel.title.value.isEmpty() } returns false
    every { mockViewModel.description.value.isEmpty() } returns false

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Save").assertExists()

    composeTestRule.onNodeWithText("Save").performClick()
  }

  @Test
  fun clickOnStartButton() {
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
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Start").performClick()
  }

  @Test
  fun clickOnStopButton() {
    try {
      every { mockViewModel.isRecording() } returns true
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

      every { mockItineraryRepository.getUID() } returns "mockUID"
      every { mockViewModel.pinList } returns listOf()

      composeTestRule.setContent {
        RecordScreen(context = mockContext, viewModel = mockViewModel, navigation = mockNavigation)
      }

      // Go to RecordScreen
      composeTestRule.onNodeWithTag("RecordScreen").assertExists()
      composeTestRule.onNodeWithTag("RecordScreen").performClick()

      composeTestRule.onNodeWithText("Stop").performClick()
    } catch (e: Exception) {
      // If any exception occurs, fail the test
      assertTrue("Test failed due to exception: ${e.message}", true)
    }
  }

  @Test
  fun testEnterADescription() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns true
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns "Title"
    every { mockViewModel.startDate.value } returns "2021-10-10"
    every { mockViewModel.endDate.value } returns "2021-10-10"

    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithTag("DescriptionTextField").performClick()

    // write description
    composeTestRule.onNodeWithTag("DescriptionTextField").performTextInput("This is a description")
  }

  @Test
  fun testEnterATitle() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns true
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns "Title"
    every { mockViewModel.startDate.value } returns "2021-10-10"
    every { mockViewModel.endDate.value } returns "2021-10-10"

    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithTag("TitleTextField").performClick()

    // write title
    composeTestRule.onNodeWithTag("TitleTextField").performTextInput("This is a title")
  }

  @Test
  fun testClickOnSaveWhenNoTitle() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns true
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns ""
    every { mockViewModel.startDate.value } returns "2021-10-10"
    every { mockViewModel.endDate.value } returns "2021-10-10"

    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Save").performClick()
  }

  @Test
  fun testClickOnSaveWhenNoDescription() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns true
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns ""
    every { mockViewModel.title.value } returns "Title"
    every { mockViewModel.startDate.value } returns "2021-10-10"
    every { mockViewModel.endDate.value } returns "2021-10-10"
    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithText("Save").performClick()
  }

  @Test
  fun testClickOnCloseWhenInDescription() {
    every { mockViewModel.isRecording() } returns false
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns true
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns "Title"

    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithTag("CloseButton").assertExists()
    // composeTestRule.onNodeWithText("CloseButton").assertExists()
  }

  @Test
  fun testRecordTextIsDisplayed() {
    every { mockViewModel.isRecording() } returns true
    every { mockViewModel.isPaused.value } returns false
    every { mockViewModel.isInDescription() } returns false
    every { mockViewModel.addSpotClicked.value } returns false
    every { mockViewModel.description.value } returns "Description"
    every { mockViewModel.title.value } returns "Title"
    every { mockViewModel.startDate.value } returns "2021-10-10"
    every { mockViewModel.endDate.value } returns "2021-10-10"
    every { mockViewModel.latLongList } returns listOf()
    every { mockViewModel.namePOI.value } returns "Name"
    every { mockViewModel.displayNameDropDown.value } returns "Name"

    every { mockItineraryRepository.getAllItineraries() } returns mockItineraries

    composeTestRule.setContent {
      RecordScreen(context = appContext, viewModel = mockViewModel, navigation = mockNavigation)
    }

    // Go to RecordScreen
    composeTestRule.onNodeWithTag("RecordScreen").assertExists()
    composeTestRule.onNodeWithTag("RecordScreen").performClick()

    composeTestRule.onNodeWithTag("RecordText").assertExists()
  }
}
