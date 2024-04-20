package com.example.triptracker.map

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.map.MapOverview
import com.example.triptracker.viewmodel.MapViewModel
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Tests for MapOverview.
 */
@RunWith(AndroidJUnit4::class)
class MapTest : TestCase() {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    @get:Rule val composeTestRule = createComposeRule()

    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @RelaxedMockK
    lateinit var mockViewModel: MapViewModel
    @RelaxedMockK
    lateinit var mockContext: Context
    @RelaxedMockK lateinit var mockNavigation: Navigation

    /**
     * Test case to cover the scenario where location permission is granted.
     */
    //@Test
    //fun mapOverview_LocationPermissionGranted() {
    //    // Set up mocks
    //    every { mockViewModel.filteredPathList } returns MutableLiveData(mockFilteredPaths)
    //    every { mockViewModel.selectedPolylineState } returns mutableStateOf(null)
    //    every { mockViewModel.cityNameState } returns mutableStateOf("City Name")
    //
    //    every { mockContext.getString(any()) } returns "Permission granted"
    //    every { mockContext.getString(any()) } returns "Permission denied"
    //
    //    // Set up the test composition
    //    composeTestRule.setContent {
    //        MapOverview(mapViewModel = mockViewModel, context = mockContext, navigation = mockNavigation)
    //    }
    //
    //    // Verify that the map screen is loaded when location permission is granted
    //    composeTestRule.onNodeWithText("Permission granted").assertExists()
    //    // Add more assertions as needed
    //}
    //
    ///**
    // * Test case to cover the scenario where location permission is denied.
    // */
    //@Test
    //fun mapOverview_LocationPermissionDenied() {
    //    // Set up mocks
    //    every { mockViewModel.filteredPathList } returns MutableLiveData(mockFilteredPaths)
    //    every { mockViewModel.selectedPolylineState } returns mutableStateOf(null)
    //    every { mockViewModel.cityNameState } returns mutableStateOf("City Name")
    //
    //    every { mockContext.getString(any()) } returns "Permission granted"
    //    every { mockContext.getString(any()) } returns "Permission denied"
    //
    //    // Set up the test composition
    //    composeTestRule.setContent {
    //        MapOverview(mapViewModel = mockViewModel, context = mockContext, navigation = mockNavigation)
    //    }
    //
    //    // Verify that the appropriate message is displayed when location permission is denied
    //    composeTestRule.onNodeWithText("Permission denied").assertExists()
    //    // Add more assertions as needed
    //}

    // Add more test cases to cover other branches and conditions within MapOverview

    @Test
    fun emptyTest() {
        assert(true)
    }
}


