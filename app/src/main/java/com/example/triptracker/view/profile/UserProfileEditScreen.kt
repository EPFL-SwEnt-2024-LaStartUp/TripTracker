package com.example.triptracker.view.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.triptracker.model.profile.UserProfile
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
  // TODO: implement the retrieval of the ambient user profile
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

  /* Mutable state variable that holds the name of the user profile */
  var name by remember { mutableStateOf(profile.name) }
  var isNameEmpty by remember { mutableStateOf(profile.name.isEmpty()) }

  /* Mutable state variable that holds the surname of the user profile */
  var surname by remember { mutableStateOf(profile.surname) }
  var isSurnameEmpty by remember { mutableStateOf(profile.surname.isEmpty()) }

  /* Mutable state variable that holds the mail of the user profile */
  var mail by remember { mutableStateOf(profile.mail) }
  var isMailEmpty by remember { mutableStateOf(profile.mail.isEmpty()) }

  /* Mutable state variable that holds the birthdate of the user profile */
  var birthdate by remember { mutableStateOf(LocalDate.now()) }
  var isBirthdateEmpty by remember { mutableStateOf(false) }

  /* Mutable state variable that holds the username of the user profile */
  var username by remember { mutableStateOf(profile.username) }
  var isUsernameEmpty by remember { mutableStateOf(profile.username.isEmpty()) }

  /* Mutable state variable that holds the image url of the user profile */
  var imageUrl by remember { mutableStateOf(profile.profileImageUrl) }
  var isImageUrlEmpty by remember { mutableStateOf(profile.profileImageUrl?.isEmpty()) }

  // Variable to store the state of the new profile picture
  var selectedPicture by remember { mutableStateOf<Uri?>(null) }
  // Launcher for the pick multiple media activity
  val pickMedia =
      rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { picture ->
        // Callback is invoked after the user selects media items or closes the
        // photo picker.
        if (picture != null) {
          Log.d("PhotoPicker", "One media selected.")
          selectedPicture = picture
        } else {
          Log.d("PhotoPicker", "No media selected.")
        }
      }

  Scaffold(
      topBar = {},
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.testTag("UserProfileEditScreen")) { innerPadding ->
        Box(
            modifier =
                Modifier.fillMaxHeight()
                    .padding(innerPadding)
                    .padding(top = 30.dp, bottom = 30.dp, start = 25.dp, end = 25.dp)
                    .fillMaxWidth()
                    .background(md_theme_light_dark, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.TopCenter) {
              Column(
                  horizontalAlignment = Alignment.Start,
                  verticalArrangement = Arrangement.SpaceEvenly) {
                    Spacer(modifier = Modifier.height(25.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.17f),
                        verticalAlignment = Alignment.CenterVertically) {
                          Spacer(modifier = Modifier.width(25.dp))
                          Box(modifier = Modifier.size(90.dp)) {
                            Box(
                                modifier =
                                    Modifier.size(90.dp)
                                        .background(Color.White, shape = CircleShape)) {
                                  InsertPicture(pickMedia, selectedPicture)
                                }
                            // Position the small orange circle with a plus sign
                            when (selectedPicture != null) {
                              true -> {}
                              false -> {
                                Box(
                                    modifier =
                                        Modifier.size(24.dp)
                                            .background(md_theme_orange, shape = CircleShape)
                                            .align(Alignment.BottomEnd)) {
                                      Icon(
                                          imageVector = Icons.Default.Add,
                                          contentDescription = "Add",
                                          tint = Color.White,
                                          modifier = Modifier.padding(4.dp))
                                    }
                              }
                            }
                          }
                          Spacer(modifier = Modifier.width(25.dp))
                          Column(
                              modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
                              verticalArrangement = Arrangement.Center) {
                                Text(
                                    text = "Username",
                                    fontSize = 14.sp,
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight.Normal,
                                    color = md_theme_grey)
                                OutlinedTextField(
                                    value = username,
                                    label = {},
                                    onValueChange = {
                                      username = it
                                      isUsernameEmpty = it.isEmpty()
                                    },
                                    modifier =
                                        Modifier.padding(bottom = 5.dp, end = 30.dp).weight(1f),
                                    textStyle =
                                        TextStyle(
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontFamily = Montserrat,
                                            fontWeight = FontWeight.Normal),
                                    colors =
                                        OutlinedTextFieldDefaults.colors(
                                            unfocusedTextColor = md_theme_grey,
                                            unfocusedBorderColor =
                                                if (isUsernameEmpty) md_theme_light_error
                                                else md_theme_grey,
                                            unfocusedLabelColor =
                                                if (isUsernameEmpty) md_theme_light_error
                                                else md_theme_grey,
                                            cursorColor = md_theme_grey,
                                            focusedBorderColor =
                                                if (isUsernameEmpty) md_theme_light_error
                                                else md_theme_grey,
                                            focusedLabelColor = Color.White,
                                        ))
                              }
                        }
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
                                  unfocusedTextColor = md_theme_grey,
                                  unfocusedBorderColor =
                                      if (isBirthdateEmpty) md_theme_light_error else md_theme_grey,
                                  unfocusedLabelColor =
                                      if (isBirthdateEmpty) md_theme_light_error else md_theme_grey,
                                  cursorColor = md_theme_grey,
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
                    Spacer(modifier = Modifier.height(25.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        contentAlignment = Alignment.Center) {
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
 */
@Composable
fun ProfileEditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEmpty: Boolean
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
      modifier = Modifier.fillMaxWidth(1f).padding(bottom = 5.dp, start = 30.dp, end = 30.dp),
      textStyle =
          TextStyle(
              color = Color.White,
              fontSize = 16.sp,
              fontFamily = Montserrat,
              fontWeight = FontWeight.Normal),
      colors =
          OutlinedTextFieldDefaults.colors(
              unfocusedTextColor = md_theme_grey,
              unfocusedBorderColor = if (isEmpty) md_theme_light_error else md_theme_grey,
              unfocusedLabelColor = if (isEmpty) md_theme_light_error else md_theme_grey,
              cursorColor = md_theme_grey,
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
/**
 * InsertPicture is a composable function that allows the user to insert his profile picture
 * Inspiration was taken from Jérémy Barghorn's function in AddSpot.kt
 *
 * @param pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> launcher for the
 *   activity
 * @param selectedPicture: Uri? selected item will be written here
 */
fun InsertPicture(
    pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    selectedPicture: Uri?,
) {
  when (selectedPicture != null) {
    // when no picture was selected show the add picture icon
    false -> {
      Box(
          modifier =
              Modifier.fillMaxSize().clickable {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
              },
          contentAlignment = Alignment.Center // Center the content
          ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = "Add Picture",
                tint = md_theme_orange)
          }
    }
    // when a picture was selected show the picture
    true -> {
      Box(modifier = Modifier.fillMaxSize().testTag("EditPicture")) {
        AsyncImage(
            model = selectedPicture,
            contentDescription = "Profile picture",
            modifier =
                Modifier.fillMaxSize().clip(CircleShape).clickable {
                  pickMedia.launch(
                      PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                })
      }
    }
  }
}

/** This function previews the UserProfileEditScreen. */
// @Preview
// @Composable
// fun UserProfileEditScreenPreview() {
//  val navController = rememberNavController()
//  val navigation = remember(navController) { Navigation(navController) }
//  UserProfileEditScreen(navigation)
// }
