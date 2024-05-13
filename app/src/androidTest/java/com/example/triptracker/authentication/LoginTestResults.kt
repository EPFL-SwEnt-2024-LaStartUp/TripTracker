package com.example.triptracker.authentication

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.example.triptracker.MainActivity
import com.example.triptracker.model.authentication.SignInResult
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.LoginScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.viewmodel.LoginViewModel
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTestResults : TestCase() {

  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
  @get:Rule val composeTestRule = createComposeRule()

  // The IntentsTestRule simply calls Intents.init() before the @Test block
  // and Intents.release() after the @Test block is completed. IntentsTestRule
  // is deprecated, but it was MUCH faster than using IntentsRule in our tests
  @get:Rule val intentsTestRule = IntentsTestRule(MainActivity::class.java)
  @RelaxedMockK private lateinit var mockViewModel: LoginViewModel
  @RelaxedMockK private lateinit var mockNavigation: Navigation
  @RelaxedMockK private lateinit var mockProfile: MutableUserProfile
  @RelaxedMockK private lateinit var user: UserProfile

  @get:Rule
  val permissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  @Before
  fun setUp() {
    mockNavigation = mockk(relaxed = true)
    mockViewModel = mockk(relaxed = true)
    mockProfile = mockk(relaxed = true)
    user = UserProfile("hey@m", "Jack", "", "")
  }

  @Test
  fun loginOKTest() {

    every { mockViewModel.authResult } returns
        MutableLiveData<AuthResponse<SignInResult>>(AuthResponse.Success(SignInResult(user)))

    composeTestRule.setContent {
      LoginScreen(
          navigation = mockNavigation, profile = mockProfile, loginViewModel = mockViewModel)
    }
  }

  @Test
  fun loginNotOKTest() {
    every { mockViewModel.authResult } returns
        MutableLiveData<AuthResponse<SignInResult>>(AuthResponse.Error("Error"))

    composeTestRule.setContent {
      LoginScreen(
          navigation = mockNavigation, profile = mockProfile, loginViewModel = mockViewModel)
    }
  }
}
