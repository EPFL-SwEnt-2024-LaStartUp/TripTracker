package com.example.triptracker.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.network.Connection
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

  @get:Rule val composeTestRule = createComposeRule()

  @RelaxedMockK private lateinit var mockConnection: Connection

  @RelaxedMockK private lateinit var mockNavHostController: NavHostController

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    mockConnection = mockk(relaxed = true)
    mockNavHostController = mockk(relaxed = true)
  }

  @Test
  fun testNavigationIsStartingHome() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val destinations = navigation.getTopLevelDestinations()
    assertEquals(navigation.getCurrentDestination(), destinations[0])
  }

  @Test
  fun testNavigateToHome() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val homeDestination = navigation.getTopLevelDestinations().first { it.route == Route.HOME }

    every { mockConnection.isDeviceConnectedToInternet() } returns true

    navigation.navigateTo(homeDestination)

    verify {
      mockNavHostController.navigate(homeDestination.route, any<NavOptionsBuilder.() -> Unit>())
    }
    assertEquals(homeDestination, navigation.getCurrentDestination())
  }

  @Test
  fun testNavigateToProfileOffline() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val profileDestination =
        navigation.getTopLevelDestinations().first { it.route == Route.PROFILE }

    every { mockConnection.isDeviceConnectedToInternet() } returns false

    navigation.navigateTo(profileDestination)

    verify {
      mockNavHostController.navigate(profileDestination.route, any<NavOptionsBuilder.() -> Unit>())
    }
  }

  @Test
  fun testNavigateToProfileOnline() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val profileDestination =
        navigation.getTopLevelDestinations().first { it.route == Route.PROFILE }

    every { mockConnection.isDeviceConnectedToInternet() } returns true

    navigation.navigateTo(profileDestination)

    verify {
      mockNavHostController.navigate(profileDestination.route, any<NavOptionsBuilder.() -> Unit>())
    }
  }

  @Test
  fun testNavigateToOfflineWhenNoInternet() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val homeDestination = navigation.getTopLevelDestinations().first { it.route == Route.HOME }

    every { mockConnection.isDeviceConnectedToInternet() } returns false

    navigation.navigateTo(homeDestination)

    verify { mockNavHostController.navigate(Route.OFFLINE) }
  }

  @Test
  fun testRetryNavigateTo() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val mapDestination = navigation.getTopLevelDestinations().first { it.route == Route.MAPS }

    every { mockConnection.isDeviceConnectedToInternet() } returns false

    navigation.navigateTo(mapDestination)
    verify { mockNavHostController.navigate(Route.OFFLINE) }

    every { mockConnection.isDeviceConnectedToInternet() } returns true

    navigation.retryNavigateTo()
    verify {
      mockNavHostController.navigate(mapDestination.route, any<NavOptionsBuilder.() -> Unit>())
    }
  }

  @Test
  fun testRetryNavigateTo2() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val mapDestination = navigation.getTopLevelDestinations().first { it.route == Route.MAPS }
    val mapId = "12345"
    every { mockConnection.isDeviceConnectedToInternet() } returns false

    navigation.navigateTo(mapDestination.route, mapId)
    verify { mockNavHostController.navigate(Route.OFFLINE) }

    every { mockConnection.isDeviceConnectedToInternet() } returns true

    navigation.retryNavigateTo()
    verify { mockNavHostController.navigate("MAPS?id=$mapId", any<NavOptionsBuilder.() -> Unit>()) }
  }

  @Test
  fun testNavigateToMapsWithId() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val mapRoute = Route.MAPS
    val mapId = "12345"

    every { mockConnection.isDeviceConnectedToInternet() } returns true

    navigation.navigateTo(mapRoute, mapId)

    verify { mockNavHostController.navigate("MAPS?id=$mapId", any<NavOptionsBuilder.() -> Unit>()) }
  }

  @Test
  fun testGoBack() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    navigation.goBack()
    verify { mockNavHostController.popBackStack() }
  }

  @Test
  fun testGetTopLevelDestinations() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val topLevelDestinations = navigation.getTopLevelDestinations()
    assertEquals(4, topLevelDestinations.size)
  }

  @Test
  fun testGetStartingDestination() {
    val navigation = Navigation(mockNavHostController, mockConnection)
    val startingDestination = navigation.getStartingDestination()
    assertEquals(Route.HOME, startingDestination.route)
  }
}
