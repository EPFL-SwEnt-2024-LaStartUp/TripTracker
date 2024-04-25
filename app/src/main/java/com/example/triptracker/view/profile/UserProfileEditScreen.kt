package com.example.triptracker.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.NavTopBar
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_error
import com.example.triptracker.view.theme.md_theme_orange
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * This function retrieves the user profile from the database.
 *
 * @return user profile.
 */
private fun retrieveProfile(): UserProfile {
  // TODO: implement the retrieval of the user profile from the database
  return UserProfile(
      "jean.rousseau@epfl.ch",
      "Jean-Jacques",
      "Rousseau",
      Date(),
      "DarkSkyMan",
      "profileImageUrl",
      listOf(),
      listOf())
}

/**
 * This function saves the user profile to the database.
 *
 * @param profile : user profile to be saved.
 */
private fun updateProfile(
    profile: UserProfile,
    name: String,
    surname: String,
    mail: String,
    birthdate: LocalDate,
    username: String,
    imageUrl: String?
) {
  // TODO: Implement saving the profile to the database
}

/**
 * This composable function displays the user profile edit screen.
 *
 * @param navigation : Navigation object that manages the navigation in the application.
 */
@Composable
fun UserProfileEditScreen(navigation: Navigation) {
  val profile = retrieveProfile()

  /** Mutable state variables for the user profile information */
  var name by remember { mutableStateOf(profile.name) }
  var isNameEmpty by remember { mutableStateOf(profile.name.isEmpty()) }

  var surname by remember { mutableStateOf(profile.surname) }
  var isSurnameEmpty by remember { mutableStateOf(profile.surname.isEmpty()) }

  var mail by remember { mutableStateOf(profile.mail) }
  var isMailEmpty by remember { mutableStateOf(profile.mail.isEmpty()) }

  var birthdate by remember { mutableStateOf(LocalDate.now()) }
  var isBirthdateEmpty by remember { mutableStateOf(false) }

  var username by remember {
    mutableStateOf(profile.pseudo)
  } // TODO: change when pseudo ---> username
  var isUsernameEmpty by remember { mutableStateOf(profile.pseudo.isEmpty()) }

  var imageUrl by remember { mutableStateOf(profile.profileImageUrl) }
  var isImageUrlEmpty by remember { mutableStateOf(profile.profileImageUrl?.isEmpty()) }

  Scaffold(
      topBar = {
        NavTopBar(
            title = "Edit Profile",
            canNavigateBack = true,
            navigateUp = { navigation.goBack() },
            actions = {})
      },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.testTag("UserProfileEditScreen")) { innerPadding ->
        Box(
            modifier =
                Modifier.fillMaxHeight().padding(innerPadding)
                    .padding(top = 35.dp, bottom = 35.dp, start = 25.dp, end = 25.dp)
                    .fillMaxWidth()
                    .background(md_theme_light_dark, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.TopCenter) {
              Column(
                  horizontalAlignment = Alignment.Start,
                  verticalArrangement = Arrangement.SpaceEvenly) {
                    Spacer(modifier = Modifier.height(25.dp))
                    ProfileEditTextField(
                        "Username",
                        username,
                        {
                          username = it
                          isUsernameEmpty = it.isEmpty()
                        },
                        isUsernameEmpty)
                    Spacer(modifier = Modifier.height(15.dp))
                    ProfileEditTextField(
                        "Name",
                        name,
                        {
                          name = it
                          isNameEmpty = it.isEmpty()
                        },
                        isNameEmpty)
                    Spacer(modifier = Modifier.height(15.dp))
                    ProfileEditTextField(
                        "Surname",
                        surname,
                        {
                          surname = it
                          isSurnameEmpty = it.isEmpty()
                        },
                        isSurnameEmpty)
                    Spacer(modifier = Modifier.height(15.dp))
                    ProfileEditTextField(
                        "Mail",
                        mail,
                        {
                          mail = it
                          isMailEmpty = it.isEmpty()
                        },
                        isMailEmpty)
                    Spacer(modifier = Modifier.height(15.dp))

                    val isOpen = remember { mutableStateOf(false) }
                    Text(
                        text = "Date of birth",
                        fontSize = 14.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Normal,
                        color = md_theme_grey,
                        modifier = Modifier.padding(start = 30.dp, end = 30.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      OutlinedTextField(
                          readOnly = true,
                          value = birthdate.format(DateTimeFormatter.ISO_DATE),
                          label = {},
                          onValueChange = {},
                          modifier = Modifier.padding(bottom = 5.dp, start = 30.dp).weight(1f),
                          textStyle =
                              TextStyle(
                                  color = Color.White,
                                  fontSize = 16.sp,
                                  fontFamily = Montserrat,
                                  fontWeight = FontWeight.Normal),
                          colors =
                              OutlinedTextFieldDefaults.colors(
                                  unfocusedTextColor = Color.White,
                                  unfocusedBorderColor =
                                      if (isBirthdateEmpty) md_theme_light_error else md_theme_grey,
                                  unfocusedLabelColor =
                                      if (isBirthdateEmpty) md_theme_light_error else md_theme_grey,
                                  cursorColor = Color.White,
                                  focusedBorderColor =
                                      if (isBirthdateEmpty) md_theme_light_error else md_theme_grey,
                                  focusedLabelColor = Color.White,
                              ))

                      IconButton(
                          modifier = Modifier.padding(end = 30.dp),
                          onClick = { isOpen.value = true } // show de dialog
                          ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                tint = Color.Gray)
                          }
                    }

                    if (isOpen.value) {
                      CustomDatePickerDialog(
                          onAccept = {
                            isOpen.value = false // close dialog

                            if (it != null) { // Set the date
                              birthdate =
                                  Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                            }
                          },
                          onCancel = {
                            isOpen.value = false // close dialog
                          })
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                      SaveButton(
                          canSave =
                              !isNameEmpty &&
                                  !isSurnameEmpty &&
                                  !isBirthdateEmpty &&
                                  !isUsernameEmpty,
                          action = {
                            updateProfile(
                                profile, name, surname, mail, birthdate, username, imageUrl)
                          })
                    }
                  }
            }
      }
}

/**
 * This composable function displays a profile edit text field.
 *
 * @param label : label of the text field.
 * @param value : value of the text field.
 * @param onValueChange : function to be called when the value of the text field changes.
 * @param isEmpty : boolean indicating if the text field is empty.
 * @param modifier : modifier for the text field.
 */
@Composable
fun ProfileEditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEmpty: Boolean,
    modifier: Modifier =
        Modifier.fillMaxWidth(1f).padding(bottom = 5.dp, start = 30.dp, end = 30.dp)
) {
  Text(
      text = label,
      fontSize = 14.sp,
      fontFamily = Montserrat,
      fontWeight = FontWeight.Normal,
      color = md_theme_grey,
      modifier = Modifier.padding(start = 30.dp, end = 30.dp),
  )
  OutlinedTextField(
      value = value,
      onValueChange = { onValueChange(it) },
      label = {},
      modifier = modifier,
      textStyle =
          TextStyle(
              color = Color.White,
              fontSize = 16.sp,
              fontFamily = Montserrat,
              fontWeight = FontWeight.Normal),
      colors =
          OutlinedTextFieldDefaults.colors(
              unfocusedTextColor = Color.White,
              unfocusedBorderColor = if (isEmpty) md_theme_light_error else md_theme_grey,
              unfocusedLabelColor = if (isEmpty) md_theme_light_error else md_theme_grey,
              cursorColor = Color.White,
              focusedBorderColor = if (isEmpty) md_theme_light_error else md_theme_grey,
              focusedLabelColor = Color.White,
          ))
}

/**
 * This composable function displays a save button.
 *
 * @param canSave : boolean indicating if the button is enabled.
 * @param action : function to be called when the button is clicked.
 */
@Composable
fun SaveButton(canSave: Boolean = true, action: () -> Unit = {}) {
  val showDialog = remember { mutableStateOf(false) }

  if (showDialog.value) {
    AlertDialog(
        backgroundColor = Color.White,
        onDismissRequest = {},
        title = { Text(text = "Profile saved! \uD83D\uDE0A", color = Color.Black) },
        confirmButton = {
          FilledTonalButton(
              onClick = { showDialog.value = false },
              colors = ButtonDefaults.filledTonalButtonColors(containerColor = md_theme_orange),
              modifier = Modifier.width(108.dp).height(30.dp),
          ) {
            Text(
                text = "Go back",
                fontSize = 14.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Color.White)
          }
        })
  }

  FilledTonalButton(
      onClick = {
        if (canSave) {
          showDialog.value = true
          action()
        }
      },
      colors =
          ButtonDefaults.filledTonalButtonColors(
              containerColor = md_theme_orange, contentColor = Color.White)) {
        Text(
            text = "Save",
            fontSize = 15.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = Color.White)
      }
}

/**
 * This composable function displays a custom date picker dialog.
 *
 * @param onAccept : function to be called when the user accepts the date.
 * @param onCancel : function to be called when the user cancels the date selection.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(onAccept: (Long?) -> Unit, onCancel: () -> Unit) {
  val state = rememberDatePickerState()

  DatePickerDialog(
      onDismissRequest = {},
      confirmButton = {
        Button(onClick = { onAccept(state.selectedDateMillis) }) { Text("Accept") }
      },
      dismissButton = { Button(onClick = onCancel) { Text("Cancel") } },
      colors = DatePickerDefaults.colors()) { // TODO: Change the colors here
        DatePicker(state = state)
      }
}

/** This function previews the UserProfileEditScreen. */
@Preview
@Composable
fun UserProfileEditScreenPreview() {
  val navController = rememberNavController()
  val navigation = remember(navController) { Navigation(navController) }
  UserProfileEditScreen(navigation)
}
