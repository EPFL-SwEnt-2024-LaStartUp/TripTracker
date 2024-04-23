package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.location.Pin
import com.example.triptracker.navigation.compareDistance
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_dark_error
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@Composable
/**
 * AddSpot is a composable function that allows the user to add a new spot to the path. This will
 * only be shown when pressing + in the record screen
 *
 * @param recordViewModel: RecordViewModel
 * @param latLng: LatLng at where the spot is going to be added
 */
fun AddSpot(recordViewModel: RecordViewModel, latLng: LatLng, context :Context, onDismiss: () -> Unit = {}) {

  // Variables to store the state of the add spot box
  var boxDisplayed by remember { mutableStateOf(true) }

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
          pictures ->
        // Callback is invoked after the user selects media items or closes the
        // photo picker.
        if (pictures.isNotEmpty()) {
          Log.d("PhotoPicker", "Number of items selected: ${pictures.size}")
          selectedPictures = pictures
        } else {
          Log.d("PhotoPicker", "No media selected")
        }
      }

  // Get the point of interest of the current location if it exists
  LaunchedEffect(Unit) {
    recordViewModel.getPOI(latLng)
    Log.d("POI", recordViewModel.namePOI.value)
  }

  when (boxDisplayed) {
    true ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(15.dp)
                .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))
                .testTag("AddSpotScreen")) {
              Column(modifier = Modifier.matchParentSize()) {

                // Close button
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                  IconButton(
                      modifier = Modifier.padding(10.dp), onClick = { alertIsDisplayed = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                            tint = md_theme_light_onPrimary)
                      }
                }

                // Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("SpotTitle"),
                    horizontalArrangement = Arrangement.Center) {
                      Text(
                          text = "Add New Spot To Path",
                          color = md_theme_orange,
                          fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                          fontWeight = FontWeight.Normal,
                          fontSize = 30.sp,
                      )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                        .testTag("SpotLocation"),
                    horizontalArrangement = Arrangement.Center) {
                      TextField(
                          enabled = recordViewModel.namePOI.value.isEmpty(),
                          modifier =
                          Modifier
                              .padding(horizontal = 20.dp)
                              .fillMaxWidth()
                              .testTag("LocationText"),
                          value =
                              if (recordViewModel.namePOI.value.isEmpty() && !isError) location
                              else recordViewModel.namePOI.value,
                          onValueChange = {
                            location = it
                            recordViewModel.getSuggestion(it) { latLng -> pos = latLng }
                            Log.d("Suggestion", pos.toString())
                            expanded.value = true
                          },
                          label = {
                            Text("Point of Interest name", color = md_theme_light_onPrimary)
                          },
                          leadingIcon = {
                            Icon(
                                Icons.Outlined.Map,
                                contentDescription = "Location",
                                tint = md_theme_orange)
                          },
                          colors =
                              TextFieldDefaults.textFieldColors(
                                  unfocusedLabelColor = md_theme_light_onPrimary,
                                  focusedLabelColor = md_theme_orange,
                                  cursorColor = md_theme_orange,
                                  backgroundColor =
                                      if (!isError) md_theme_dark_gray else md_theme_dark_error,
                                  focusedIndicatorColor = md_theme_orange,
                              ),
                          shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                          minLines = 1,
                          maxLines = 1,
                          isError = isError,
                          placeholder = { if (isError) Text(placeHolderError) else Text("") },
                      )

                      DropdownMenu(
                          expanded = expanded.value,
                          onDismissRequest = { expanded.value = false },
                          modifier =
                          Modifier
                              .fillMaxWidth()
                              .align(Alignment.CenterVertically)
                              .testTag("LocationDropDown"),
                          properties = PopupProperties(focusable = false),
                      ) {
                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("LocationDropDown"),
                            onClick = {
                              if (compareDistance(position, pos, 500.0)) {
                                position = pos
                                location = recordViewModel.displayNameDropDown.value
                                recordViewModel.namePOI.value =
                                    recordViewModel.displayNameDropDown.value
                                isError = false
                              } else {
                                isError = true
                                location = ""
                                recordViewModel.namePOI.value = ""
                              }

                              expanded.value = false
                            }) {
                              Text(
                                  text = recordViewModel.displayNameDropDown.value,
                                  modifier = Modifier.testTag("LocationDropDown"))
                            }
                      }
                    }

                // Description text box to fill some information about the spot
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("SpotDescription"),
                    horizontalArrangement = Arrangement.Center) {
                      TextField(
                          modifier =
                          Modifier
                              .padding(horizontal = 20.dp, vertical = 20.dp)
                              .fillMaxWidth()
                              .onFocusChanged {},
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
                              TextFieldDefaults.textFieldColors(
                                  unfocusedLabelColor = md_theme_light_onPrimary,
                                  focusedLabelColor = md_theme_orange,
                                  cursorColor = md_theme_orange,
                                  backgroundColor = md_theme_dark_gray,
                                  focusedIndicatorColor = md_theme_orange,
                              ),
                          shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                          minLines = 5,
                          maxLines = 5,
                      )
                    }

                // Insert pictures (max 5)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .testTag("SpotPictures"),
                    horizontalArrangement = Arrangement.Center) {
                      InsertPictures(pickMultipleMedia = pickMultipleMedia, selectedPictures, recordViewModel, context)
                    }

                when (alertIsDisplayed) {
                  true -> {
                    AlertDialog(
                        icon = { Icons.Filled.LocationOn },
                        title = {
                          Text(
                              text = "Path incomplete or don't save this spot")
                        },
                        text = {
                          Text(
                              text =
                                  "There are some missing informations and you are about to leave this page. Do you want to save the spot or dismiss it ?")
                        },
                        onDismissRequest = {
                          alertIsDisplayed = false
                          boxDisplayed = false
                          onDismiss()
                        },
                        confirmButton = {
                          TextButton(onClick = { alertIsDisplayed = false }) {
                            Text("Stay on this page")
                          }
                        },
                        dismissButton = {
                          TextButton(
                              onClick = {
                                alertIsDisplayed = false
                                boxDisplayed = false
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .testTag("SaveButton"),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom) {
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
                                    selectedPictures)
                                // Or save the spot with the location found by nominatim
                              } else {
                                saveSpot(
                                    recordViewModel,
                                    recordViewModel.namePOI.value,
                                    description,
                                    position,
                                    selectedPictures)
                              }
                              alertIsDisplayed = false
                              boxDisplayed = false
                              onDismiss()
                            }
                          },
                          modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp),
                          colors =
                              ButtonDefaults.filledTonalButtonColors(
                                  containerColor = md_theme_orange, contentColor = Color.White),
                      ) {
                        Text(
                            text = "Save",
                            fontSize = 24.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White)
                      }
                    }
              }
            }
    false -> {}
  }
}

private fun saveSpot(
    recordViewModel: RecordViewModel,
    location: String,
    description: String,
    position: LatLng,
    selectedPictures: List<Uri?>
) {

  val pin =
      Pin(
          latitude = position.latitude,
          longitude = position.longitude,
          name = location,
          description = description,
          image_url =
              if (selectedPictures.isNotEmpty()) selectedPictures[0].toString()
              else "" // TODO CHANGE THIS LATER TO A LIST IN THE DB
          )

  Log.d("Spot", pin.toString())

  // TODO recordViewModel.addSpot(spot)
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
    recordViewModel: RecordViewModel,
    context: Context
) {
  when (selectedPictures.isNotEmpty()) {
    // when no selection was done show a clickable dashed box that will allow the user to select
    // pictures
    false -> {
      val stroke =
          Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))

      Box(
          modifier =
          Modifier
              .fillMaxSize()
              .padding(horizontal = 20.dp, vertical = 5.dp)
              .drawBehind {
                  drawRoundRect(
                      color = md_theme_orange,
                      style = stroke,
                      cornerRadius = CornerRadius(16.dp.toPx())
                  )
              }
              .clip(RoundedCornerShape(16.dp))
              .clickable {
                  pickMultipleMedia.launch(
                      PickVisualMediaRequest(
                          ActivityResultContracts.PickVisualMedia.ImageAndVideo
                      )
                  )
              },
      ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(bottom = 5.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = "Add Picture",
                        tint = md_theme_orange)
                  }

              Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Add new pictures (max. 5)",
                        color = md_theme_light_onPrimary,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                    )
                  }
            }
      }
    }
    // when selection was done show the selected pictures in a scrollable row
    // Add an edit button to allow the user to change the selection
    true -> {
      Column(
          modifier = Modifier
              .fillMaxSize()
              .testTag("EditPicture"),
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
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                  selectedPictures.forEach { picture ->
                      recordViewModel.addImageToStorage(picture!!)
                    AsyncImage(
                        model = picture,
                        contentDescription = "Image",
                        modifier = Modifier
                            .height(300.dp)
                            .padding(horizontal = 2.dp))
                  }
                Toast.makeText(context, "${selectedPictures.size} Pictures are uploaded", Toast.LENGTH_SHORT).show()
                }
          }
    }
  }
}

@Preview
@Composable
fun AddSpotPreview() {
  AddSpot(RecordViewModel(), LatLng(46.519053, 6.568287), context = LocalContext.current)
  //  AddSpot(RecordViewModel(), LatLng(46.519879, 6.560632))
}
