package com.example.triptracker.view

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.MainActivity.Companion.applicationContext
import com.example.triptracker.R
import com.example.triptracker.authentication.AuthResponse
import com.example.triptracker.authentication.GoogleAuthenticator
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.profile.UserProfileEditScreen
import com.example.triptracker.view.theme.md_theme_light_inverseSurface
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.viewmodel.LoginViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Composable that displays the login screen or the user's information if they are already
 * authenticated or an error screen if the login fails
 *
 * @param navigation: Navigation object to navigate to other screens
 * @param profile: MutableUserProfile object to store the user's profile
 * @param loginViewModel: ViewModel to handle the login logic
 * @param profileViewModel: ViewModel to handle the user's profile
 */
@Composable
fun LoginScreen(
    navigation: Navigation,
    profile: MutableUserProfile,
    loginViewModel: LoginViewModel = viewModel(),
    profileViewModel: UserProfileViewModel = viewModel()
) {
  val context = applicationContext()
  val authenticator = GoogleAuthenticator()

  val signInLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result
        ->
        if (result.resultCode == Activity.RESULT_OK) {
          val intent = result.data
          if (intent != null) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            task.addOnCompleteListener { signInTask ->
              if (signInTask.isSuccessful) {
                val googleSignInAccount = signInTask.result
                if (googleSignInAccount != null) {
                  profileViewModel.getUserProfile(googleSignInAccount.email ?: "") {
                    if (it != null) {
                      profile.userProfile.value = it
                      loginViewModel.onSignInResult(LoginViewModel.AuthStatus.LOGGED_IN)
                    } else {

                      val currentDate = LocalDate.now()
                      val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                      profile.userProfile.value =
                          UserProfile(
                              googleSignInAccount.email ?: "",
                              googleSignInAccount.familyName ?: "",
                              googleSignInAccount.givenName ?: "",
                              currentDate.format(formatter),
                              (googleSignInAccount.givenName + "_" + googleSignInAccount.familyName)
                                  ?: "",
                              // No null values are allowed for the profile or the app will crash
                              googleSignInAccount.photoUrl?.toString() ?: "defaultProfile.com",
                              emptyList(),
                              emptyList())

                      loginViewModel.onSignInResult(LoginViewModel.AuthStatus.CREATE_ACCOUNT)
                    }
                  }
                } else {
                  // googleSignInAccount is null, handle the case accordingly
                  loginViewModel.onSignInResult(LoginViewModel.AuthStatus.ERROR)
                }
              } else {
                // Sign-in failed
                loginViewModel.onSignInResult(LoginViewModel.AuthStatus.ERROR)
              }
            }
          }
        } else {
          loginViewModel.onSignInResult(LoginViewModel.AuthStatus.ERROR)
        }
      }

  authenticator.signInLauncher = signInLauncher

  val loginResult = loginViewModel.authResult.observeAsState()

  when (val response = loginResult.value) {
    is AuthResponse.Success -> {
      val home = navigation.getStartingDestination().route
      navigation.navController.navigate(home)
    }
    is AuthResponse.Loading -> {
      UserProfileEditScreen(navigation = navigation, profile = profile, isCreated = true)
    }
    is AuthResponse.Error -> {
      LoginResponseFailure(message = response.errorMessage)
    }
    else -> {
      if (authenticator.isSignedIn(context)) {
        val home = navigation.getStartingDestination().route
        navigation.navController.navigate(home)
      } else {
        Login(context, authenticator)
      }
    }
  }
}

/**
 * Composable that displays the login screen screen and button when no user is authenticated
 *
 * @param context: Context of the application
 * @param authenticator: GoogleAuthenticator object to handle the login logic
 */
@Composable
fun Login(
    context: Context,
    authenticator: GoogleAuthenticator,
) {
  val isDark = isSystemInDarkTheme()
  Column(
      modifier = Modifier.fillMaxSize().padding(15.dp).testTag("LoginScreen"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
        modifier = Modifier.width(189.dp).height(189.dp),
        painter =
            painterResource(
                id =
                    if (!isDark) R.drawable.logo_no_background
                    else R.drawable.logo_no_background_dark),
        contentDescription = "image logo",
        contentScale = ContentScale.FillBounds)
    Spacer(modifier = Modifier.height(40.dp))
    Text(
        text = "Welcome",
        modifier = Modifier.testTag("LoginTitle"),
        style =
            TextStyle(
                fontSize = 57.sp,
                lineHeight = 64.sp,
                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
            ))
    Button(
        onClick = {
          val signInIntent = authenticator.createSignInIntent(context)
          authenticator.signIn(signInIntent)
        },
        modifier = Modifier.width(250.dp).height(40.dp).testTag("LoginButton"),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = md_theme_light_onPrimary,
            ),
        border = BorderStroke(1.dp, md_theme_light_inverseSurface),
        shape = RoundedCornerShape(size = 20.dp),
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.googlelogo),
            contentDescription = "image description",
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Sign in with Google",
            textAlign = TextAlign.Center,
            color = md_theme_light_inverseSurface)
      }
    }
  }
}

/**
 * Composable that displays an error message when the login fails
 *
 * @param message: String containing the error message to be displayed
 */
@Composable
fun LoginResponseFailure(message: String) {
  Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
    Card(modifier = Modifier.padding(16.dp)) {
      Text(
          text = "$message : Restart the app and try to sign in again.",
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(16.dp))
    }
  }
}
