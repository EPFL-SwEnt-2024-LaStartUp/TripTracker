package com.example.triptracker.view.map

import android.content.Context
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.navigation.AllowLocationPermission
import com.example.triptracker.navigation.checkForLocationPermission
import com.example.triptracker.navigation.getCurrentLocation
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

val lightDark = Color(0xFF1D2022)
val redFox = Color(0xFFD4622B)
val lightGray = Color(0xFFC0C7CD)
val transparentGray = Color(0xC0C0C7CD)

@Composable
fun RecordScreen(
    context: Context,
    viewModel : RecordViewModel = RecordViewModel()
) {    //there will be the viewmodel

    var deviceLocation = LatLng(46.518831258,6.559331096)
    var mapProperties =
        MapProperties(
            mapType = MapType.SATELLITE, isMyLocationEnabled = checkForLocationPermission(context))

    var uiSettings = MapUiSettings(myLocationButtonEnabled = checkForLocationPermission(context))

    getCurrentLocation(context = context, onLocationFetched = { deviceLocation = it })

    when (checkForLocationPermission(context = context)) {
        true -> {
            Map(context, viewModel, deviceLocation, mapProperties, uiSettings)
        }

        false -> {
            AllowLocationPermission(
                onPermissionGranted = {
                    mapProperties =
                        MapProperties(mapType = MapType.SATELLITE, isMyLocationEnabled = true)
                    uiSettings = MapUiSettings(myLocationButtonEnabled = true)
                },
                onPermissionDenied = {
                    mapProperties =
                        MapProperties(mapType = MapType.SATELLITE, isMyLocationEnabled = false)
                    uiSettings = MapUiSettings(myLocationButtonEnabled = false)
                })
        }
    }

}
@Composable
fun Map(
    context: Context,
    viewModel: RecordViewModel,
    startLocation: LatLng,
    mapProperties: MapProperties,
    uiSettings: MapUiSettings
) {
    // Used to display the gradient with the top bar and the changing city location
    val ui by remember { mutableStateOf(uiSettings) }
    val properties by remember { mutableStateOf(mapProperties) }
    var deviceLocation by remember { mutableStateOf(startLocation) }
    val latLngList = remember { mutableStateOf(listOf<LatLng>()) }


    /// TODO change location to the users one with permissions
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(deviceLocation, 17f)
    }

    //update the device location when the location changed

    LaunchedEffect(key1 = viewModel.latLongList) {
        Log.e("lat long list", viewModel.latLongList.toString())
    }


    LaunchedEffect(key1 = deviceLocation) {
        Log.e("device location", deviceLocation.toString())
        //getCurrentLocation(context = context, onLocationFetched = { deviceLocation = it })
        cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLocation, 17f)
        //viewModel.updateLocation(deviceLocation)
    }
    //update the device location every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            getCurrentLocation(context = context, onLocationFetched = { deviceLocation = it })
            delay(5000)
        }
    }

    //if is recoring then add the current location to the list every 5 seconds
    LaunchedEffect(key1 = viewModel.isRecording()) {
        while (viewModel.isRecording() && !viewModel.isPaused.value) {

            viewModel.addLatLng(deviceLocation)
            latLngList.value = viewModel.latLongList
            delay(3000)
        }
    }

    val gradient =
        Brush.verticalGradient(
            colorStops =
            arrayOf(0.0f to Color.White, 0.1f to Color.White, 0.3f to Color.Transparent))

    // Displays the map
    Box {
        Box(modifier = Modifier
            .fillMaxSize()
        ) {
            GoogleMap(
                modifier =
                Modifier
                    .matchParentSize()
                    .background(
                        brush =
                        Brush.verticalGradient(colors = listOf(Color.Transparent, lightDark))
                    ),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(myLocationButtonEnabled = true),
            ) {
                if (viewModel._latLongList.isNotEmpty() && viewModel.isRecording()) {
                    Log.e("polyline", viewModel._latLongList.toString())
                    Polyline(
                        points = viewModel._latLongList,
                        color = Color.Red,
                        width = 5f
                    )
                }

            }
        }
        Box(modifier = Modifier
            .matchParentSize()
            .background(gradient)
            .align(Alignment.TopCenter)) {
            Text(
                text = "Record",
                modifier = Modifier
                    .padding(30.dp)
                    .align(Alignment.TopCenter),
                fontSize = 24.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = lightDark
            )
        }
        StartWindow(context = context, viewModel = viewModel)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(imageVector = Icons.Outlined.MyLocation,
                contentDescription = "Center on device location",
                tint = Color.White,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .background(transparentGray, shape = RoundedCornerShape(16.dp))
                    .padding(10.dp)
                    .align(Alignment.CenterVertically)

                    .clickable {
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(deviceLocation, 17f)
                    }

            )

            FilledTonalButton(
                onClick = {
                    if (viewModel.isRecording()) {
                        viewModel.stopRecording()
                        viewModel.resetRecording()
                    } else {
                        viewModel.startRecording()
                    }

                },
                modifier = Modifier

                    .padding(50.dp)
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(0.1f),
                colors = ButtonDefaults.filledTonalButtonColors(
                    //container color is D4622B
                    containerColor = redFox,
                    contentColor = Color.White
                ),

                ) {
                Text(
                    text = deviceLocation.latitude.toString(),
                    //text = if (viewModel.isRecording()) viewModel._latLongList.size.toString() else "Start",
                    //text = if (viewModel.isRecording()) "Stop" else "Start",
                    fontSize = 24.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(50.dp))
        }


    }
}

/**
 * Function to display the start window
 * @param context the context of the application
 * @param viewModel the viewmodel of the application
 */
@Composable
fun StartWindow(
    context: Context,
    viewModel: RecordViewModel,
    ){

    val timer = remember { mutableLongStateOf(0L) }

    //if is recording then update the timer
    if(viewModel.isRecording()) {
        LaunchedEffect(key1 = viewModel.isRecording()) {
            while (viewModel.isRecording()) {
                // update the timer
                timer.longValue = viewModel.getElapsedTime()
                delay(1000)
            }
        }
    }

    // Display the recording window if the user is recording
    if(viewModel.isRecording()) {
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 100.dp)
            ,

            verticalArrangement = Arrangement.SpaceBetween
        ){
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.35f)
                    .fillMaxWidth()
                    //.padding(top = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.5f)
                        .background(lightDark, shape = RoundedCornerShape(35.dp))
                        .align(Alignment.Center)


                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "TIME",
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            fontSize = 22.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = displayTime(timer.longValue), modifier = Modifier
                                .align(Alignment.CenterVertically),
                            fontSize = 22.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.45f)
                        .background(lightDark, shape = RoundedCornerShape(35.dp))
                        .align(Alignment.Center)

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FilledTonalButton(
                            onClick = {
                                if (viewModel.isPaused.value) {
                                    viewModel.resumeRecording()
                                } else {
                                    viewModel.pauseRecording()
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .fillMaxHeight(0.6f),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color.White,
                                contentColor = lightDark),
                            ) {
                            Text(
                                text = if (viewModel.isPaused.value) "Resume" else "Pause",
                                fontSize = 14.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = lightDark
                            )
                        }
                        Row(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "Add spot", modifier = Modifier
                                    .align(Alignment.CenterVertically),
                                fontSize = 14.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = transparentGray
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            IconButton(
                                onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(50.dp)
                                    .background(Color.White, shape = CircleShape),
                                ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Add spot",
                                    tint = lightDark,
                                    modifier = Modifier.size(30.dp)

                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Function to display the time in a readable format
 * @param time the time in milliseconds
 */
fun displayTime(time: Long): String {
    val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
    val minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60
    val hours = TimeUnit.MILLISECONDS.toHours(time)

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/**
 * Function to preview the RecordScreen
 */
@Preview
@Composable
fun RecordScreenPreview() {
    val context = LocalContext.current
    RecordScreen(
        context,
    )
}