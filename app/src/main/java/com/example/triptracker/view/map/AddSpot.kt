package com.example.triptracker.view.map

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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun AddSpot(recordViewModel: RecordViewModel, latLng: LatLng) {

  var boxDisplayed by remember { mutableStateOf(true) }
  var location by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var position by remember { mutableStateOf(latLng) }
  var selectedPictures by remember { mutableStateOf<List<Uri?>>(emptyList()) }

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
  LaunchedEffect(Unit) { recordViewModel.getPOI(latLng) }

  when (boxDisplayed) {
    true ->
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .padding(15.dp)
                    .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))) {
              Column(modifier = Modifier.matchParentSize()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                  IconButton(
                      modifier = Modifier.padding(10.dp), onClick = { boxDisplayed = false }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                            tint = md_theme_light_onPrimary)
                      }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
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

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                    horizontalArrangement = Arrangement.Center) {
                      TextField(
                          enabled = recordViewModel.namePOI.value.isEmpty(),
                          modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                          value = recordViewModel.namePOI.value.ifEmpty { location },
                          onValueChange = {
                            location = it
                            if (location.length > 3) {
                              position = recordViewModel.getSuggestion(it)
                              expanded.value =
                                  recordViewModel.displayNameDropDown.value.isNotEmpty()
                            }
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
                                  backgroundColor = md_theme_dark_gray,
                                  focusedIndicatorColor = md_theme_orange,
                              ),
                          shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                          minLines = 1,
                          maxLines = 1)

                      DropdownMenu(
                          expanded = expanded.value,
                          onDismissRequest = { expanded.value = false },
                          modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                          properties = PopupProperties(focusable = false),
                      ) {
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                              expanded.value = false
                              location = recordViewModel.displayNameDropDown.value
                              recordViewModel.namePOI.value =
                                  recordViewModel.displayNameDropDown.value
                            }) {
                              Text(text = recordViewModel.displayNameDropDown.value)
                            }
                      }
                    }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                      TextField(
                          modifier =
                              Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
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

                Row(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    horizontalArrangement = Arrangement.Center) {
                      InsertPictures(pickMultipleMedia = pickMultipleMedia, selectedPictures)
                    }

                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom) {
                      FilledTonalButton(
                          onClick = {
                            // TODO save all the data on the DB
                            Pin(
                                latitude = position.latitude,
                                longitude = position.longitude,
                                name = location,
                                description = description,
                                image_url =
                                    selectedPictures[0]
                                        .toString() // TODO CHANGE THIS LATER TO A LIST IN THE DB
                                )
                            boxDisplayed = false
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

@Composable
fun InsertPictures(
    pickMultipleMedia:
        ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>,
    selectedPictures: List<Uri?>
) {
  when (selectedPictures.isNotEmpty()) {
    false -> {
      val stroke =
          Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))

      Box(
          modifier =
              Modifier.fillMaxSize()
                  .padding(horizontal = 20.dp, vertical = 5.dp)
                  .drawBehind {
                    drawRoundRect(
                        color = md_theme_orange,
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
    true -> {
      Column(
          modifier = Modifier.fillMaxSize(),
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

@Preview
@Composable
fun AddSpotPreview() {
  AddSpot(RecordViewModel(), LatLng(46.5191, 6.5668))
}
