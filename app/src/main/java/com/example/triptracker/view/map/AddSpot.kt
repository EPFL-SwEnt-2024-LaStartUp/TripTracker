package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.repository.Response
import com.example.triptracker.navigation.compareDistance
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_error
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_light_secondary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.model.LatLng
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** AddSpotStatus is an enum class that represents the different states of the AddSpot box */
enum class AddSpotStatus {
  DISPLAY_FORM,
  DISPLAY_CAMERA,
  HIDE
}

@Composable
/**
 * AddSpot is a composable function that allows the user to add a new spot to the path. This will
 * only be shown when pressing + in the record screen
 *
 * @param recordViewModel: RecordViewModel
 * @param latLng: LatLng at where the spot is going to be added
 */
fun AddSpot(
    recordViewModel: RecordViewModel,
    latLng: LatLng,
    context: Context,
    onDismiss: () -> Unit = {}
) {

  // Variables to store the state of the add spot box
  var boxDisplayed by remember { mutableStateOf(AddSpotStatus.DISPLAY_FORM) }

  // Get the point of interest of the current location if it exists
  LaunchedEffect(Unit) {
    recordViewModel.getPOI(latLng)
    Log.d("POI", recordViewModel.namePOI.value)
  }

  when (boxDisplayed) {
    AddSpotStatus.DISPLAY_FORM -> {
      FillAddSpot(
          latLng = latLng,
          recordViewModel = recordViewModel,
          context = context,
          onDismiss = {
            boxDisplayed = AddSpotStatus.HIDE
            onDismiss()
          },
          onCameraLaunch = { boxDisplayed = AddSpotStatus.DISPLAY_CAMERA })
    }
    AddSpotStatus.DISPLAY_CAMERA -> {

      // Create the directory where the pictures will be saved temporary
      val file = File("/storage/emulated/0/Download/TripTracker/")
      file.mkdirs()

      // Create the executor for the camera
      val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

      TakePicture(
          outputDirectory = file,
          executor = cameraExecutor,
          context = context,
          onCaptureClosedSuccess = { boxDisplayed = AddSpotStatus.DISPLAY_FORM },
          onCaptureClosedError = { boxDisplayed = AddSpotStatus.DISPLAY_FORM })
    }
    AddSpotStatus.HIDE -> {}
  }
}

/**
 * handleSelectedPictures is a function that handles the selected pictures
 *
 * @param pictures: List<Uri> list of selected pictures
 * @param callback: (List<Uri>) -> Unit callback function
 */
private fun handleSelectedPictures(pictures: List<Uri>, callback: (List<Uri>) -> Unit) {
  if (pictures.isNotEmpty()) {
    Log.d("PhotoPicker", "Number of items selected: ${pictures.size}")
    callback(pictures)
  } else {
    Log.d("PhotoPicker", "No media selected")
  }
}

/** textColor is a function that returns the color of the text in the text field */
private fun textColor(recordViewModel: RecordViewModel) =
    if (recordViewModel.namePOI.value.isNotEmpty()) md_theme_light_error
    else md_theme_light_onPrimary

/** locationText is a function that returns the text of the location field */
private fun locationText(recordViewModel: RecordViewModel, isError: Boolean, location: String) =
    if (recordViewModel.namePOI.value.isEmpty() && !isError) location
    else recordViewModel.namePOI.value

/** checkString is a function that checks if a string is empty */
private fun checkString(string: String, callback: (Boolean) -> Unit) {
  if (string.isNotEmpty()) {
    callback(false)
  }
}

/** PlaceHolderText is a composable function that displays the placeholder text */
@Composable
private fun PlaceHolderText(isError: Boolean, placeHolderError: String) {
  if (isError) Text(placeHolderError, color = md_theme_light_onPrimary)
  else Text("", color = md_theme_light_onPrimary)
}

private fun checkDistance(
    position: LatLng,
    pos: LatLng,
    recordViewModel: RecordViewModel,
    callback: (pos: LatLng, location: String, isError: Boolean) -> Unit
) {
  if (compareDistance(position, pos, 500.0)) {
    callback(pos, recordViewModel.displayNameDropDown.value, false)
    recordViewModel.namePOI.value = recordViewModel.displayNameDropDown.value
  } else {
    callback(position, "", true)
    recordViewModel.namePOI.value = ""
  }
}

/**
 * FillAddSpot is a composable function that allows the user to fill the information of the spot
 * that is going to be added to the path.
 */
@Composable
fun FillAddSpot(
    latLng: LatLng,
    recordViewModel: RecordViewModel,
    context: Context,
    onDismiss: () -> Unit = {},
    onCameraLaunch: () -> Unit = {}
) {

  // Variables to store the state of the location string of the spot
  var location by remember { mutableStateOf("") }

  // Variables to store the state of the description of the spot
  var description by remember { mutableStateOf("") }

  // Variables to store the state of the position of the spot in form of latlng
  var position by remember { mutableStateOf(latLng) }

  // Variable to store the state of the error
  var isError by remember { mutableStateOf(false) }

  // Variables to store the state of the selected pictures
  var selectedPictures by remember { mutableStateOf<List<Uri?>>(emptyList()) }

  // Variable to store the state of the alert
  var alertIsDisplayed by remember { mutableStateOf(false) }

  // Launcher for the pick multiple media activity
  val pickMultipleMedia =
      rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) {
          // Callback is invoked after the user selects media items or closes the
          // photo picker.
          pictures ->
        handleSelectedPictures(pictures) { selectedPictures = it }
      }

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .fillMaxHeight(0.85f)
              .padding(top = 80.dp, start = 15.dp, end = 15.dp, bottom = 20.dp)
              .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))
              .testTag("AddSpotScreen")) {
        Column(modifier = Modifier.matchParentSize().verticalScroll(rememberScrollState())) {
          Row(
              modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.weight(1f))
                // Title
                Text(
                    text = "Add Spot",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp).testTag("SpotTitle"))

                Spacer(modifier = Modifier.weight(0.7f))
                // Close button
                IconButton(
                    modifier = Modifier.testTag("CloseButton"),
                    onClick = { alertIsDisplayed = true }) {
                      Icon(
                          imageVector = Icons.Outlined.Close,
                          contentDescription = "Close",
                          tint = md_theme_light_onPrimary)
                    }
              }

          val expanded = remember { mutableStateOf(false) }
          var pos by remember { mutableStateOf(LatLng(0.0, 0.0)) }
          val placeHolderError by remember { mutableStateOf("Please enter a valid location") }

          /*
          TextField to input the name of the point of interest
          Enabled when no POI was found automatically with nominatim
          When enabled it will help completion of the POI with help of nominatim api
          If the users selects a suggestion that is too far away from the current position (>500m) it will not be accepted and a new input will be asked
          If a place in this range was provided then it is locked on and the LATLNG of the point is updated
          */

          Row(
              modifier = Modifier.fillMaxWidth().padding(top = 30.dp).testTag("SpotLocation"),
              horizontalArrangement = Arrangement.Center) {
                OutlinedTextField(
                    enabled = recordViewModel.namePOI.value.isEmpty(),
                    modifier =
                        Modifier.padding(horizontal = 20.dp).fillMaxWidth().testTag("LocationText"),
                    value = locationText(recordViewModel, isError, location),
                    onValueChange = {
                      location = it
                      recordViewModel.getSuggestion(it) { latLng -> pos = latLng }
                      Log.d("Suggestion", pos.toString())
                      expanded.value = true
                      checkString(it) { bool -> isError = bool }
                    },
                    label = { Text("Point of Interest name", color = md_theme_light_onPrimary) },
                    leadingIcon = {
                      Icon(
                          Icons.Outlined.Map,
                          contentDescription = "Location",
                          tint = md_theme_orange)
                    },
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = md_theme_light_onPrimary,
                            unfocusedBorderColor = textColor(recordViewModel),
                            unfocusedLabelColor = textColor(recordViewModel),
                            cursorColor = md_theme_orange,
                            focusedBorderColor = md_theme_light_onPrimary,
                            focusedLabelColor = md_theme_light_onPrimary,
                            disabledBorderColor = md_theme_light_secondary,
                        ),
                    minLines = 1,
                    maxLines = 1,
                    isError = isError,
                    placeholder = {
                      PlaceHolderText(isError = isError, placeHolderError = placeHolderError)
                    },
                    textStyle = TextStyle(color = md_theme_light_onPrimary),
                )

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                    properties = PopupProperties(focusable = false),
                ) {
                  DropdownMenuItem(
                      text = {
                        Text(
                            text = recordViewModel.displayNameDropDown.value,
                            modifier = Modifier.testTag("LocationDropDown"))
                      },
                      modifier = Modifier.fillMaxWidth().testTag("LocationDropDown"),
                      onClick = {
                        checkDistance(
                            position,
                            pos,
                            recordViewModel,
                            callback = { pos, loc, boolError ->
                              position = pos
                              location = loc
                              isError = boolError
                            })

                        expanded.value = false
                      })
                }
              }

          // Description text box to fill some information about the spot
          Row(
              modifier = Modifier.fillMaxWidth().testTag("SpotDescription"),
              horizontalArrangement = Arrangement.Center) {
                OutlinedTextField(
                    modifier =
                        Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                            .fillMaxWidth()
                            .onFocusChanged {}
                            .testTag("DescriptionText"),
                    value = description,
                    placeholder = {
                      Text(
                          "Give a short description of the spot you want to add to the path !",
                          color = md_theme_light_onPrimary)
                    },
                    onValueChange = { description = it },
                    label = { Text("Description", color = md_theme_light_onPrimary) },
                    leadingIcon = {
                      Icon(
                          Icons.Outlined.Description,
                          contentDescription = "Description",
                          tint = md_theme_orange)
                    },
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = md_theme_light_onPrimary,
                            unfocusedBorderColor = md_theme_light_onPrimary,
                            unfocusedLabelColor = md_theme_light_onPrimary,
                            cursorColor = md_theme_orange,
                            focusedBorderColor = md_theme_light_onPrimary,
                            focusedLabelColor = md_theme_light_onPrimary,
                        ),
                    minLines = 5,
                    maxLines = 5,
                    textStyle = TextStyle(color = md_theme_light_onPrimary),
                )
              }

          // Insert pictures (max 5)
          Row(
              modifier = Modifier.fillMaxWidth().height(150.dp).testTag("SpotPictures"),
              horizontalArrangement = Arrangement.Center) {
                // Button to launch the camera
                IconButton(
                    onClick = { onCameraLaunch() },
                    modifier =
                        Modifier.align(Alignment.CenterVertically)
                            .padding(10.dp)
                            .testTag("Camera")) {
                      Icon(
                          modifier = Modifier.size(50.dp),
                          imageVector = Icons.Outlined.Camera,
                          contentDescription = "Add Picture",
                          tint = md_theme_orange)
                    }
                InsertPictures(
                    pickMultipleMedia = pickMultipleMedia,
                    selectedPictures,
                )
              }

          when (alertIsDisplayed) {
            true -> {
              AlertDialog(
                  modifier = Modifier.testTag("AlertDialog"),
                  icon = { Icons.Filled.LocationOn },
                  title = { Text(text = "Path incomplete or don't save this spot") },
                  text = {
                    Text(
                        text =
                            "There are some missing informations and you are about to leave this page. Do you want to save the spot or dismiss it ?")
                  },
                  onDismissRequest = {
                    alertIsDisplayed = false

                    onDismiss()
                  },
                  confirmButton = {
                    TextButton(onClick = { alertIsDisplayed = false }) { Text("Stay on this page") }
                  },
                  dismissButton = {
                    TextButton(
                        onClick = {
                          alertIsDisplayed = false
                          onDismiss()
                        }) {
                          Text("Dismiss")
                        }
                  })
            }
            false -> {}
          }

          // Save button that will upload the data to the DB once completed
          Row(
              modifier = Modifier.fillMaxWidth().fillMaxHeight().testTag("SaveButton"),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.Top) {
                FilledTonalButton(
                    onClick = {
                      if ((location.isEmpty() && recordViewModel.namePOI.value.isEmpty()) ||
                          description.isEmpty()) {
                        alertIsDisplayed = true
                      } else {
                        // Save the spot with the entered location
                        if (location.isNotEmpty()) {
                          saveSpot(
                              recordViewModel,
                              location,
                              description,
                              position,
                              selectedPictures,
                              context)
                          // Or save the spot with the location found by nominatim
                        } else {
                          saveSpot(
                              recordViewModel,
                              recordViewModel.namePOI.value,
                              description,
                              position,
                              selectedPictures,
                              context)
                        }
                        alertIsDisplayed = false
                        onDismiss()
                      }
                    },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp),
                    colors =
                        ButtonDefaults.filledTonalButtonColors(
                            containerColor = md_theme_light_onPrimary, contentColor = Color.White),
                ) {
                  Text(
                      text = "Save",
                      fontSize = 24.sp,
                      fontFamily = Montserrat,
                      fontWeight = FontWeight.SemiBold,
                      color = md_theme_light_black)
                }
              }
        }
      }
}

/**
 * Function to save the spot to the database
 *
 * @param recordViewModel: the record ViewModel
 * @param location: the location of the spot
 * @param description: the description of the spot
 * @param position: the position of the spot
 * @param selectedPictures: the pictures of the spot
 * @param context: the context of the application
 */
private fun saveSpot(
    recordViewModel: RecordViewModel,
    location: String,
    description: String,
    position: LatLng,
    selectedPictures: List<Uri?>,
    context: Context
) {

  recordViewModel.addImageToStorageResponse = emptyList()
  val pin =
      mutableStateOf(
          Pin(
              latitude = position.latitude,
              longitude = position.longitude,
              name = location,
              description = description,
              imageUrl =
                  recordViewModel.addImageToStorageResponse.map { resp ->
                    if (resp is Response.Success) {
                      resp.data!!.toString()
                    } else {
                      Uri.EMPTY.toString()
                    }
                  }))
  var counter = 1
  selectedPictures.forEach { picture ->
    val newPictureUrl = getFilePathFromUri(picture, context)
    recordViewModel.addImageToStorage(newPictureUrl!!) { resp ->
      recordViewModel.addImageToStorageResponse += resp
      pin.value =
          Pin(
              latitude = position.latitude,
              longitude = position.longitude,
              name = location,
              description = description,
              imageUrl =
                  recordViewModel.addImageToStorageResponse.map { pictureUrl ->
                    if (pictureUrl is Response.Success) {
                      pictureUrl.data!!.toString()
                    } else {
                      Uri.EMPTY.toString()
                    }
                  })
      if (counter == selectedPictures.size) {
        recordViewModel.addPin(pin.value)
        Log.d("PIN_LIST", recordViewModel.pinList.toString())
      }
      counter++
    }
  }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
/**
 * InsertPictures is a composable function that allows the user to insert pictures to the spot
 *
 * @param pickMultipleMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>
 *   launcher for the activity
 * @param selectedPictures: List<Uri?> selected items will be written here
 */
fun InsertPictures(
    pickMultipleMedia:
        ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>,
    selectedPictures: List<Uri?>,
) {
  when (selectedPictures.isNotEmpty()) {
    // when no selection was done show a clickable dashed box that will allow the user to select
    // pictures
    false -> {
      val stroke =
          Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))

      Box(
          modifier =
              Modifier.fillMaxSize()
                  .padding(horizontal = 20.dp, vertical = 5.dp)
                  .drawBehind {
                    drawRoundRect(
                        color = md_theme_light_secondary,
                        style = stroke,
                        cornerRadius = CornerRadius(16.dp.toPx()))
                  }
                  .clip(RoundedCornerShape(16.dp))
                  .clickable {
                    pickMultipleMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                  },
      ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
              Row(
                  modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AddPhotoAlternate,
                        contentDescription = "Add Picture",
                        tint = md_theme_orange)
                  }

              Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Add new pictures (max. 5)",
                        color = md_theme_light_secondary,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                  }
            }
      }
    }
    // when selection was done show the selected pictures in a scrollable row
    // Add an edit button to allow the user to change the selection
    true -> {
      Column(
          modifier = Modifier.fillMaxSize().testTag("EditPicture"),
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                  Text(
                      text = "Selected Pictures",
                      color = md_theme_orange,
                      fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                      fontWeight = FontWeight.Normal,
                      fontSize = 20.sp,
                      modifier = Modifier.padding(10.dp))
                  Icon(
                      imageVector = Icons.Outlined.Edit,
                      contentDescription = "Edit",
                      tint = md_theme_orange,
                      modifier =
                          Modifier.clickable {
                            pickMultipleMedia.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                          })
                }

            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                  selectedPictures.forEach { picture ->
                    AsyncImage(
                        model = picture,
                        contentDescription = "Image",
                        modifier = Modifier.height(300.dp).padding(horizontal = 2.dp))
                  }
                }
          }
    }
  }
}
