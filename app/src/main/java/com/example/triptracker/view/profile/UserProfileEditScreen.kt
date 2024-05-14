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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.Response
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_error
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.UserProfileViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * This composable function displays the user profile edit screen.
 *
 * @param navigation : Navigation object that manages the navigation in the application.
 */
@Composable
fun UserProfileEditScreen(
    navigation: Navigation,
    profile: MutableUserProfile,
    userProfileViewModel: UserProfileViewModel = viewModel(),
    isCreated: Boolean = false
) {

  /* Mutable state variable that holds the name of the user profile */
  var name by remember { mutableStateOf(profile.userProfile.value.name) }
  var isNameEmpty by remember { mutableStateOf(profile.userProfile.value.name.isEmpty()) }

  /* Mutable state variable that holds the surname of the user profile */
  var surname by remember { mutableStateOf(profile.userProfile.value.surname) }
  var isSurnameEmpty by remember { mutableStateOf(profile.userProfile.value.surname.isEmpty()) }

  /* Mutable state variable that holds the mail of the user profile */
  var mail by remember { mutableStateOf(profile.userProfile.value.mail) }
  var isMailEmpty by remember { mutableStateOf(profile.userProfile.value.mail.isEmpty()) }

  /* Mutable state variable that holds the birthdate of the user profile */
  var birthdate by remember { mutableStateOf(profile.userProfile.value.birthdate) }
  var isBirthdateEmpty by remember { mutableStateOf(false) }

  /* Mutable state variable that holds the username of the user profile */
  var username by remember { mutableStateOf(profile.userProfile.value.username) }
  var isUsernameEmpty by remember { mutableStateOf(profile.userProfile.value.username.isEmpty()) }

  /* Mutable state variable that holds the image url of the user profile */
  var imageUrl by remember { mutableStateOf(profile.userProfile.value.profileImageUrl) }
  var isImageUrlEmpty by remember {
    mutableStateOf(profile.userProfile.value.profileImageUrl?.isEmpty())
  }

  var isLoading by remember { mutableStateOf(false) }

  val scrollState = rememberScrollState()

  // Variable to store the state of the new profile picture
  var selectedPicture by remember { mutableStateOf<Uri?>(null) }
  // Launcher for the pick multiple media activity
  val pickMedia =
      rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { picture ->
        // Callback is invoked after the user selects media items or closes the
        // photo picker.
        if (picture != null) {
          Log.d("PhotoPicker", picture.toString())
          selectedPicture = picture
        } else {
          Log.d("PhotoPicker", "No media selected.")
        }
      }

  /** Alpha value for the screen depending on loading state */
  val alpha = if (!isLoading) 1f else 0.9f

  /**
   * his function updates the user profile in the database on save.
   *
   * @param navigation : Navigation object that manages the navigation in the application.
   * @param isCreated : Boolean indicating if the user profile is created. Navigation needs to be
   *   used after the callback else the view will be destroyed and resulting in a crash of the
   *   upload of the picture.
   */
  fun updateProfile(navigation: Navigation, isCreated: Boolean) {
    isLoading = true
    if (selectedPicture != null) {
      userProfileViewModel.addProfilePictureToStorage(selectedPicture!!) { resp ->
        imageUrl =
            if (resp is Response.Success) {
              resp.data!!.toString()
            } else {
              imageUrl // Keep the old image if the new one could not be uploaded
            }
        val newProfile =
            UserProfile(
                mail = mail,
                name = name,
                surname = surname,
                birthdate = birthdate,
                username = username,
                profileImageUrl = imageUrl,
                profile.userProfile.value.followers,
                profile.userProfile.value.following)
        Log.d("TRALALALALALL", newProfile.toString())
        userProfileViewModel.updateUserProfileInDb(newProfile)
        profile.userProfile.value = newProfile
        if (!isCreated) {
          navigation.goBack()
        } else {
          navigation.navigateTo(navigation.getStartingDestination())
        }
        isLoading = false
      }
    } else {
      val newProfile =
          UserProfile(
              mail = mail,
              name = name,
              surname = surname,
              birthdate = birthdate,
              username = username,
              profileImageUrl = imageUrl,
              profile.userProfile.value.followers,
              profile.userProfile.value.following)
      userProfileViewModel.updateUserProfileInDb(newProfile)
      profile.userProfile.value = newProfile
      if (!isCreated) {
        navigation.goBack()
      } else {
        navigation.navigateTo(navigation.getStartingDestination())
      }
      isLoading = false
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
                    .background(md_theme_light_dark.copy(alpha), shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.TopCenter) {

              // Loading bar for when the save button is clicked
              when (isLoading) {
                true -> {
                  CircularProgressIndicator(
                      modifier = Modifier.width(64.dp).align(Alignment.Center),
                      color = md_theme_orange,
                      trackColor = md_theme_grey,
                  )
                }
                false -> {}
              }
              Column(
                  modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
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
                                  InsertPicture(pickMedia, selectedPicture, imageUrl)
                                }
                            // Position the small orange circle with a plus sign
                            when (selectedPicture == null && imageUrl.isNullOrEmpty()) {
                              true -> {
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
                              false -> {}
                            }
                          }
                          Spacer(modifier = Modifier.width(25.dp))
                          Column(
                              modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
                              verticalArrangement = Arrangement.Center) {
                                Text(
                                    modifier = Modifier.padding(end = 30.dp),
                                    text = "Username",
                                    fontSize = 14.sp,
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight.Normal,
                                    color = md_theme_grey)
                                OutlinedTextField(
                                    singleLine = true,
                                    value = username,
                                    label = {},
                                    onValueChange = {
                                      if (it.length <= 30) {
                                        username = it
                                        isUsernameEmpty = it.isEmpty()
                                      }
                                    },
                                    modifier =
                                        Modifier.height(65.dp).padding(bottom = 5.dp, end = 30.dp),
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
                    Spacer(modifier = Modifier.height(20.dp))
                    ProfileEditTextField(
                        "Name",
                        name,
                        {
                          name = it
                          isNameEmpty = it.isEmpty()
                        },
                        isNameEmpty)
                    Spacer(modifier = Modifier.height(20.dp))
                    ProfileEditTextField(
                        "Surname",
                        surname,
                        {
                          surname = it
                          isSurnameEmpty = it.isEmpty()
                        },
                        isSurnameEmpty)
                    Spacer(modifier = Modifier.height(20.dp))
                    ProfileEditTextField(
                        "Mail",
                        mail,
                        {
                          mail = it
                          isMailEmpty = it.isEmpty()
                        },
                        isMailEmpty,
                        true)
                    Spacer(modifier = Modifier.height(20.dp))

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
                          value = birthdate,
                          label = {},
                          onValueChange = {},
                          modifier =
                              Modifier.height(65.dp)
                                  .padding(bottom = 5.dp, start = 30.dp)
                                  .weight(1f),
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
                      Box(modifier = Modifier.testTag("CustomDatePickerDialog")) {
                        CustomDatePickerDialog(
                            onAccept = {
                              isOpen.value = false // close dialog

                              if (it != null) { // Set the date
                                birthdate =
                                    Instant.ofEpochMilli(it)
                                        .atZone(ZoneId.of("UTC"))
                                        .toLocalDate()
                                        .format(DateTimeFormatter.ISO_DATE)
                              }
                            },
                            onCancel = {
                              isOpen.value = false // close dialog
                            })
                      }
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
                              action = { updateProfile(navigation, isCreated) })
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
    isEmpty: Boolean,
    isReadOnly: Boolean = false,
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
      readOnly = isReadOnly,
      enabled = !isReadOnly,
      singleLine = true,
      value = value,
      onValueChange = { onValueChange(it) },
      label = {},
      modifier =
          Modifier.height(65.dp)
              .fillMaxWidth(1f)
              .padding(bottom = 5.dp, start = 30.dp, end = 30.dp),
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
  FilledTonalButton(
      onClick = {
        if (canSave) {
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
  val maxDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

  val selectableDates =
      object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
          return utcTimeMillis <= maxDate
        }
      }

  val state = rememberDatePickerState(selectableDates = selectableDates)

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
    oldPicture: String?
) {
  when (selectedPicture != null) {
    // when no picture was selected show the add picture icon
    false -> {
      if (!oldPicture.isNullOrEmpty()) {
        Box(modifier = Modifier.fillMaxSize().testTag("ProfilePicture")) {
          AsyncImage(
              model = oldPicture,
              contentDescription = "Profile picture",
              modifier =
                  Modifier.fillMaxSize().clip(CircleShape).clickable {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                  },
              contentScale = ContentScale.Crop)
        }
      } else {
        Box(
            modifier =
                Modifier.fillMaxSize().testTag("NoProfilePicture").clickable {
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
    }
    // when a picture was selected show the picture
    true -> {
      Box(modifier = Modifier.fillMaxSize().testTag("ProfilePicture")) {
        AsyncImage(
            model = selectedPicture,
            contentDescription = "Profile picture",
            modifier =
                Modifier.fillMaxSize().clip(CircleShape).clickable {
                  pickMedia.launch(
                      PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                },
            contentScale = ContentScale.Crop)
      }
    }
  }
}

/** This function previews the UserProfileEditScreen. */
// @Preview
// @Composable
// fun UserProfileEditScreenPreview() {
//   val navController = rememberNavController()
//   val navigation = remember(navController) { Navigation(navController) }
//   UserProfileEditScreen(navigation)
// }
