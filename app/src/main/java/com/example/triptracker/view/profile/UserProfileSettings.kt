package com.example.triptracker.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_orange

/**
 * Composable function to display the user's settings
 *
 * @param navigation: Navigation object to navigate to other screens
 */
@Composable
fun UserProfileSettings(navigation: Navigation) {
  Scaffold(bottomBar = { NavigationBar(navigation) }) { paddingValues ->
    Box(
        modifier =
            Modifier.padding(paddingValues)
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp, top = 0.dp, bottom = 0.dp)) {
          Column() {
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f)) {
              Row(
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navigation.goBack() }) {
                      Icon(
                          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                          contentDescription = "Back")
                    }
                    Box(
                        modifier = Modifier.fillMaxSize().padding(end = 52.dp),
                        contentAlignment = Alignment.Center) {
                          Text(
                              text = "Settings",
                              fontSize = 25.sp,
                              fontFamily = Montserrat,
                              fontWeight = FontWeight.Bold)
                        }
                  }
            }

            Spacer(modifier = Modifier.height(60.dp))

            SettingsGroup("Account Privacy")
            SettingsElement(
                "Profile",
                actions = {
                  // Remember the state of the button to toggle between texts
                  val (isPrivate, setIsPrivate) = remember { mutableStateOf(true) }

                  // Determine the text and background colors based on the state
                  val textColor = if (isPrivate) md_theme_dark_gray else Color.White
                  val backgroundColor = if (isPrivate) md_theme_grey else md_theme_orange
                  val buttonText = if (isPrivate) "Private" else "Public"
                  FilledTonalButton(
                      modifier = Modifier.size(width = 94.dp, height = 35.dp),
                      onClick = { setIsPrivate(!isPrivate) },
                      colors =
                          ButtonDefaults.filledTonalButtonColors(
                              containerColor = backgroundColor, contentColor = textColor)) {
                        Text(
                            text = buttonText,
                            fontSize = 12.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold)
                      }
                })
            Divider()
            SettingsElement(
                "Path Visibility",
                actions = {
                  // Remember the state of the button to toggle between texts
                  val (buttonState, setButtonState) = remember { mutableStateOf(0) }

                  // Determine the text and background colors based on the state
                  val buttonText: String
                  val textColor: Color
                  val backgroundColor: Color

                  when (buttonState) {
                    0 -> {
                      buttonText = "Me"
                      textColor = md_theme_dark_gray
                      backgroundColor = md_theme_light_dark
                    }
                    1 -> {
                      buttonText = "Friends"
                      textColor = md_theme_dark_gray
                      backgroundColor = md_theme_grey
                    }
                    else -> {
                      buttonText = "Public"
                      textColor = Color.White
                      backgroundColor = md_theme_orange
                    }
                  }

                  FilledTonalButton(
                      modifier = Modifier.size(width = 94.dp, height = 35.dp),
                      onClick = { setButtonState((buttonState + 1) % 3) },
                      colors =
                          ButtonDefaults.filledTonalButtonColors(
                              containerColor = backgroundColor, contentColor = textColor)) {
                        Text(
                            text = buttonText,
                            fontSize = 12.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold)
                      }
                })
            Divider()
            SettingsElement(
                "Location",
                actions = {
                  // Remember the state of the button to toggle between texts
                  val (buttonState, setButtonState) = remember { mutableStateOf(0) }

                  // Determine the text and background colors based on the state
                  val buttonText: String
                  val textColor: Color
                  val backgroundColor: Color

                  when (buttonState) {
                    0 -> {
                      buttonText = "Never"
                      textColor = md_theme_dark_gray
                      backgroundColor = md_theme_light_dark
                    }
                    1 -> {
                      buttonText = "When the app is running"
                      textColor = md_theme_dark_gray
                      backgroundColor = md_theme_grey
                    }
                    else -> {
                      buttonText = "Always"
                      textColor = Color.White
                      backgroundColor = md_theme_orange
                    }
                  }

                  FilledTonalButton(
                      modifier = Modifier.size(width = 208.dp, height = 35.dp),
                      onClick = { setButtonState((buttonState + 1) % 3) },
                      colors =
                          ButtonDefaults.filledTonalButtonColors(
                              containerColor = backgroundColor, contentColor = textColor)) {
                        Text(
                            text = buttonText,
                            fontSize = 12.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            color = textColor)
                      }
                })

            Spacer(modifier = Modifier.height(100.dp))

            SettingsGroup("Account Settings")
            SettingsElement(
                "Account",
                actions = {
                  FilledTonalButton(
                      modifier = Modifier.size(width = 120.dp, height = 35.dp),
                      onClick = {},
                      colors =
                          ButtonDefaults.filledTonalButtonColors(
                              containerColor = md_theme_grey, contentColor = md_theme_dark_gray)) {
                        Text(
                            text = "Sign out",
                            fontSize = 12.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold)
                      }
                })
          }
        }
  }
}

@Composable
fun Divider() {
  Spacer(modifier = Modifier.height(15.dp))
  HorizontalDivider(thickness = 1.5.dp, color = md_theme_grey)
  Spacer(modifier = Modifier.height(15.dp))
}

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

@Composable
fun SettingsElement(elementName: String, actions: @Composable () -> Unit = {}) {
  Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.padding(start = 10.dp).weight(1f)) {
          Text(
              text = elementName,
              fontSize = 18.sp,
              fontFamily = Montserrat,
              fontWeight = FontWeight.SemiBold,
              color = md_theme_dark_gray)
        }
        actions()
      }
}

// @Preview
// @Composable
// fun UserProfileSettingsPreview() {
//  val navController = rememberNavController()
//  val navigation = remember(navController) { Navigation(navController) }
//  UserProfileSettings(navigation = navigation)
// }
