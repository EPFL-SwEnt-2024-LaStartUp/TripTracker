package com.example.triptracker.navigation

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat

fun checkForLocationPermission(context: Context): Boolean {
  return !(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
      android.content.pm.PackageManager.PERMISSION_GRANTED &&
      ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
          android.content.pm.PackageManager.PERMISSION_GRANTED)
}

@Composable
fun AllowLocationPermission(onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {

  val openAlertDialog = remember { mutableStateOf(true) }

  val locationPermissionLauncher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isGranted = true
            permissions.entries.forEach {
              if (!it.value) {
                isGranted = false
                return@forEach
              }
            }

            if (isGranted) {
              onPermissionGranted()
            } else {
              onPermissionDenied()
            }
          }

  when {
    openAlertDialog.value -> {
      AlertDialog(
          icon = { Icons.Filled.LocationOn },
          title = { Text(text = "TripTracker Needs Location Permission") },
          text = {
            Text(
                text =
                    "In order to allow TripTracker to work in an optimal fashion, please allow location permission. This will allow you to record a path and view it on the map.")
          },
          onDismissRequest = {
            onPermissionDenied()
            openAlertDialog.value = false
          },
          confirmButton = {
            TextButton(
                onClick = {
                  locationPermissionLauncher.launch(
                      arrayOf(
                          Manifest.permission.ACCESS_FINE_LOCATION,
                          Manifest.permission.ACCESS_COARSE_LOCATION,
                      ))
                  openAlertDialog.value = false
                  onPermissionGranted()
                }) {
                  Text("Allow")
                }
          },
          dismissButton = {
            TextButton(
                onClick = {
                  onPermissionDenied()
                  openAlertDialog.value = false
                }) {
                  Text("Don't Allow")
                }
          })
    }
  }
}

@Composable
fun LaunchPermissionRequest(context: Context) {
  var hasLocationPermission by remember { mutableStateOf(checkForLocationPermission(context)) }

  if (hasLocationPermission) {
    Log.d("Permission", "Location Permission Granted")
  } else {
    AllowLocationPermission(
        onPermissionGranted = {
          hasLocationPermission = true
          Log.d("Permission", "Location Permission Granted")
        },
        onPermissionDenied = {
          hasLocationPermission = false
          Log.d("Permission", "Location Permission NOT Granted")
        })
  }
}
