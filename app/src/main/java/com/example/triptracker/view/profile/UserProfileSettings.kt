package com.example.triptracker.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.MainActivity
import com.example.triptracker.authentication.GoogleAuthenticator
import com.example.triptracker.model.profile.AmbientUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.profile.subviews.ScaffoldTopBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.view.theme.md_theme_tri_state
import com.example.triptracker.view.theme.md_theme_warning_orange
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * Composable function to display the user's settings
 *
 * @param navigation: Navigation object to navigate to other screens
 */
@Composable
fun UserProfileSettings(
    navigation: Navigation,
    userProfileViewModel: UserProfileViewModel = viewModel()
) {
  val userProfile = AmbientUserProfile.current.userProfile.value
  val userAmbient = AmbientUserProfile.current.userProfile

  Scaffold(
      topBar = { ScaffoldTopBar(navigation = navigation, label = "Settings") },
      bottomBar = { NavigationBar(navigation) }) { paddingValues ->
        Box(
            modifier =
                Modifier.testTag("UserProfileSettings")
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp, top = 0.dp, bottom = 0.dp)) {
              Column {
                Spacer(modifier = Modifier.height(50.dp))

                SettingsGroup("Account Privacy")
                SettingsElement(
                    "Profile",
                    actions = {
                      // Remember the state of the button to toggle between texts
                      var curr = false
                      if (userAmbient.value.profilePrivacy == 0) {
                        curr = true
                      }
                      val (isPublic, setIsPrivate) = remember { mutableStateOf(curr) }

                      // Determine the text and background colors based on the state
                      val textColor = md_theme_light_onPrimary
                      val backgroundColor = if (isPublic) md_theme_tri_state else md_theme_orange
                      val buttonText = if (isPublic) "Public" else "Private"
                      FilledTonalButton(
                          modifier =
                              Modifier.size(
                                      width = (LocalConfiguration.current.screenWidthDp * 0.30).dp,
                                      height =
                                          (LocalConfiguration.current.screenHeightDp * 0.06).dp)
                                  .testTag("ProfilePrivacyButton"),
                          onClick = {
                            setIsPrivate(!isPublic)
                            var privacy = userProfile.profilePrivacy
                            if (!isPublic) {
                              privacy = 0
                            } else {
                              privacy = 1
                            }
                            val newProfile =
                                UserProfile(
                                    mail = userProfile.mail,
                                    name = userProfile.name,
                                    surname = userProfile.surname,
                                    birthdate = userProfile.birthdate,
                                    username = userProfile.username,
                                    profileImageUrl = userProfile.profileImageUrl,
                                    followers = userProfile.followers,
                                    following = userProfile.following,
                                    profilePrivacy = privacy,
                                    itineraryPrivacy = userProfile.itineraryPrivacy)
                            userAmbient.value = newProfile
                            userProfileViewModel.updateUserProfileInDb(newProfile)
                          },
                          colors =
                              ButtonDefaults.filledTonalButtonColors(
                                  containerColor = backgroundColor, contentColor = textColor)) {
                            Text(
                                text = buttonText,
                                fontSize = (LocalConfiguration.current.screenHeightDp * 0.016f).sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center)
                          }
                    })
                Divider()
                SettingsElement(
                    "Path Visibility",
                    actions = {
                      // Remember the state of the button to toggle between texts
                      val (buttonState, setButtonState) =
                          remember { mutableIntStateOf(userProfile.itineraryPrivacy) }
                      TriStateButton(
                          state1 = "Public",
                          state2 = "Friends",
                          state3 = "Me",
                          state = buttonState,
                          modifier =
                              Modifier.size(
                                      width = (LocalConfiguration.current.screenWidthDp * 0.30).dp,
                                      height =
                                          (LocalConfiguration.current.screenHeightDp * 0.06).dp)
                                  .testTag("PathVisibilityButton"),
                          onClick = {
                            setButtonState((buttonState + 1) % 3)

                            val itinPrivacy = (buttonState + 1) % 3
                            val newProfile =
                                UserProfile(
                                    mail = userProfile.mail,
                                    name = userProfile.name,
                                    surname = userProfile.surname,
                                    birthdate = userProfile.birthdate,
                                    username = userProfile.username,
                                    profileImageUrl = userProfile.profileImageUrl,
                                    followers = userProfile.followers,
                                    following = userProfile.following,
                                    profilePrivacy = userProfile.profilePrivacy,
                                    itineraryPrivacy = itinPrivacy)
                            userAmbient.value = newProfile
                            userProfileViewModel.updateUserProfileInDb(newProfile)
                          })
                    })
                Divider()
                SettingsElement(
                    "Location",
                    actions = {
                      // Remember the state of the button to toggle between texts
                      val (buttonState, setButtonState) = remember { mutableIntStateOf(0) }
                      TriStateButton(
                          state1 = "Never",
                          state2 = "When the app is running",
                          state3 = "Always",
                          state = buttonState,
                          modifier =
                              Modifier.size(
                                  width = (LocalConfiguration.current.screenWidthDp * 0.50).dp,
                                  height = (LocalConfiguration.current.screenHeightDp * 0.06).dp),
                          onClick = { setButtonState((buttonState + 1) % 3) })
                    })

                Spacer(modifier = Modifier.height(100.dp))

                SettingsGroup("Account Settings")
                SettingsElement(
                    "Account",
                    actions = {
                      FilledTonalButton(
                          modifier =
                              Modifier.size(
                                      width = (LocalConfiguration.current.screenWidthDp * 0.30).dp,
                                      height =
                                          (LocalConfiguration.current.screenHeightDp * 0.06).dp)
                                  .testTag("LogOutButton"),
                          onClick = {
                            val context = MainActivity.applicationContext()
                            GoogleAuthenticator().signOut(context)
                            // Go back to the main profile page
                            navigation.goBack()
                            // Go to the home screen per default when restarting the app
                            navigation.navigateTo(navigation.getStartingDestination())
                            // Go to the login screen
                            navigation.navController.navigate("login")
                          },
                          colors =
                              ButtonDefaults.filledTonalButtonColors(
                                  containerColor = md_theme_warning_orange,
                                  contentColor = md_theme_light_onPrimary)) {
                            Text(
                                text = "Sign out",
                                fontSize = (LocalConfiguration.current.screenHeightDp * 0.016f).sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold)
                          }
                    })
              }
            }
      }
}

/** Composable function to display a divider */
@Composable
fun Divider() {
  Spacer(modifier = Modifier.height(15.dp))
  HorizontalDivider(thickness = 1.5.dp, color = md_theme_grey)
  Spacer(modifier = Modifier.height(15.dp))
}

/**
 * Composable function to display a settings group name and its divider
 *
 * @param groupName: Name of the settings group
 */
@Composable
fun SettingsGroup(groupName: String) {
  Text(
      text = groupName,
      fontSize = 18.sp,
      fontFamily = Montserrat,
      fontWeight = FontWeight.SemiBold,
      color = md_theme_grey)
  Spacer(modifier = Modifier.height(10.dp))
  HorizontalDivider(thickness = 1.5.dp, color = md_theme_grey)
  Spacer(modifier = Modifier.height(15.dp))
}

/**
 * Composable function to display a tri-state button
 *
 * @param state1: First state of the button
 * @param state2: Second state of the button
 * @param state3: Third state of the button
 * @param onClick: Action to be performed on the button click
 */
@Composable
fun TriStateButton(
    state1: String,
    state2: String,
    state3: String,
    state: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {

  // Determine the text and background colors based on the state
  val buttonText: String
  val backgroundColor: Color

  when (state) {
    1 -> {
      buttonText = state2
      backgroundColor = MaterialTheme.colorScheme.scrim
    }
    0 -> {
      buttonText = state1
      backgroundColor = md_theme_tri_state
    }
    else -> {
      buttonText = state3
      backgroundColor = md_theme_orange
    }
  }

  FilledTonalButton(
      modifier = modifier,
      onClick = { onClick() },
      colors =
          ButtonDefaults.filledTonalButtonColors(
              containerColor = backgroundColor, contentColor = md_theme_light_onPrimary)) {
        Text(
            text = buttonText,
            lineHeight = (LocalConfiguration.current.screenHeightDp * 0.016f).sp,
            fontSize = (LocalConfiguration.current.screenHeightDp * 0.016f).sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = md_theme_light_onPrimary)
      }
}

/**
 * Composable function to display a setting element line
 *
 * @param elementName: Name of the setting element
 * @param actions: Actions to be performed on the setting element
 */
@Composable
fun SettingsElement(elementName: String, actions: @Composable () -> Unit = {}) {
  Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.padding(start = 10.dp).weight(1f)) {
          Text(
              text = elementName,
              fontSize = (LocalConfiguration.current.screenHeightDp * 0.025f).sp,
              fontFamily = Montserrat,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onBackground)
        }
        actions()
      }
}
