package com.example.triptracker.view

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.authentication.AuthResponse
import com.example.triptracker.authentication.GoogleAuthenticator
import com.example.triptracker.model.authentication.SignInResult
import com.example.triptracker.view.theme.md_theme_light_inverseSurface
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

@Composable
/**
 * @param navigation: Navigation object to navigate to other screens
 * @param loginViewModel: ViewModel to handle the login logic @return: Composable function to
 *   Displays the login screen or the user's information if they are already authenticated or an
 *   error screen if the login fails
 */
fun LoginScreen(navigation: Navigation, loginViewModel: LoginViewModel = viewModel()) {

  val context = LocalContext.current
  val authenticator = GoogleAuthenticator(context)

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
                  val userName = googleSignInAccount.displayName
                  val email = googleSignInAccount.email
                  val photoUrl = googleSignInAccount.photoUrl?.toString()

                  loginViewModel.onSignInResult(true, userName, email, photoUrl)
                } else {
                  // googleSignInAccount is null, handle the case accordingly
                  loginViewModel.onSignInResult(false)
                }
              } else {
                // Sign-in failed
                loginViewModel.onSignInResult(false)
              }
            }
          }
        } else {
          loginViewModel.onSignInResult(false)
        }
      }

  authenticator.signInLauncher = signInLauncher

  val loginResult = loginViewModel.authResult.observeAsState()
  when (val response = loginResult.value) {
    is AuthResponse.Success -> {
      LoginResponseOk(
          result = response.data,
          onSignOut = {
            authenticator.signOut()
            navigation.navController.navigate(Route.LOGIN)
          })
      //            onNavigateToOverview() //TODO call this once new screens are added
    }
    is AuthResponse.Error -> {
      LoginResponseFailure(message = response.errorMessage)
    }
    is AuthResponse.Loading -> {
      LoginResponseFailure(message = "Loading")
    }
    else -> {
      Login(context, authenticator)
    }
  }
}

@Composable
/**
 * @param context: Context of the application
 * @param authenticator: GoogleAuthenticator object to handle the login logic @return: Composable
 *   Displays the login screen screen and button when no user is authenticated
 */
fun Login(
    context: Context,
    authenticator: GoogleAuthenticator,
) {
  Column(
      modifier = Modifier.fillMaxSize().padding(15.dp).testTag("LoginScreen"),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(modifier = Modifier.height(152.dp))
    Image(
        modifier = Modifier.width(189.dp).height(189.dp),
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "image description",
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
    Spacer(modifier = Modifier.height(120.dp))
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

@Composable
/**
 * @param result: SignInResult object containing the user's information to be displayed
 * @param onSignOut: Function to sign out the user Displays the user's information and a button to
 *   sign out
 */
fun LoginResponseOk(result: SignInResult, onSignOut: () -> Unit) {
  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        if (result.imageUrl != null) {
          AsyncImage(
              model = result.imageUrl,
              contentDescription = "Profile picture",
              modifier = Modifier.size(150.dp).clip(CircleShape),
              contentScale = ContentScale.Crop)
          Spacer(modifier = Modifier.height(16.dp))
        }
        if (result.name != null) {
          Text(
              text = result.name,
              textAlign = TextAlign.Center,
              fontSize = 36.sp,
              fontWeight = FontWeight.SemiBold)
          Spacer(modifier = Modifier.height(16.dp))
        }
        androidx.compose.material.Button(
            onClick = {} /* TODO logic to navigate to overview screen : onNavigateTo */) {
              androidx.compose.material.Text(text = "Go to overview")
            }
        // UNCOMMENT THIS CODE IF YOU WANT TO ADD A SIGN OUT BUTTON
        androidx.compose.material.Button(onClick = onSignOut) {
          androidx.compose.material.Text(text = "Sign out")
        }
      }
}

@Composable
/**
 * @param message: String containing the error message to be displayed Displays an error message
 *   when the login fails
 */
fun LoginResponseFailure(message: String) {
  Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
    Card(modifier = Modifier.padding(16.dp)) {
      Text(text = message, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
    }
  }
}

@Preview
@Composable
fun PreviewLoginResponseFailure() {
  LoginResponseFailure("Error")
}
