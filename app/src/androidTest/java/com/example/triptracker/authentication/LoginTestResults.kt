package com.example.triptracker.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTestResults : TestCase() {
  /*
  private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
  @get:Rule val composeTestRule = createComposeRule()

  // The IntentsTestRule simply calls Intents.init() before the @Test block
  // and Intents.release() after the @Test block is completed. IntentsTestRule
  // is deprecated, but it was MUCH faster than using IntentsRule in our tests
  @get:Rule val intentsTestRule = IntentsTestRule(MainActivity::class.java)
  @RelaxedMockK private lateinit var mockViewModel: LoginViewModel
  @RelaxedMockK private lateinit var mockNavigation: Navigation

  @get:Rule
  val permissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  @Before
  fun setUp() {
    mockNavigation = mockk(relaxed = true)
    mockViewModel = mockk(relaxed = true)
  }

  @Test
  fun loginOKTest() {
    every { mockViewModel.authResult } returns
        MutableLiveData<AuthResponse<SignInResult>>(
            AuthResponse.Success(SignInResult("name", "email", "imageUrl")))

    composeTestRule.setContent {
      LoginScreen(navigation = mockNavigation, loginViewModel = mockViewModel)
    }
  }

  @Test
  fun loginNotOKTest() {
    every { mockViewModel.authResult } returns
        MutableLiveData<AuthResponse<SignInResult>>(AuthResponse.Error("Error"))

    composeTestRule.setContent {
      LoginScreen(navigation = mockNavigation, loginViewModel = mockViewModel)
    }
  }

   */

  @Test
  fun dummy() {
    assert(true)
  }
}
