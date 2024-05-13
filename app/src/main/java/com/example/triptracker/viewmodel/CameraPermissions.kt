package com.example.triptracker.viewmodel

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat

fun checkForCameraPermission(context: Context): Boolean {
  return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
      android.content.pm.PackageManager.PERMISSION_GRANTED
}

@Composable
fun LaunchCameraPermissionRequest(context: Context) {
  var hasCameraPermission by remember { mutableStateOf(checkForCameraPermission(context)) }

  if (hasCameraPermission) {
    Log.d("Permission", "Camera Permission Granted")
  } else {
    AllowCameraPermission(
        onPermissionGranted = {
          hasCameraPermission = true
          Log.d("Permission", "Camera Permission Granted")
        },
        onPermissionDenied = {
          hasCameraPermission = false
          Log.d("Permission", "Camera Permission NOT Granted")
        })
  }
}

@Composable
fun AllowCameraPermission(onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {

  val openAlertDialog = remember { mutableStateOf(true) }

  val cameraPermissionLauncher =
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
          icon = { Icons.Filled.CameraAlt },
          title = { Text(text = "TripTracker Needs Camera Permissions") },
          text = {
            Text(
                text =
                    "In order to allow TripTracker to work in an optimal fashion, please allow Camera permission. This will allow you to take pictures for the spots when a path is recorded.")
          },
          onDismissRequest = {
            onPermissionDenied()
            openAlertDialog.value = false
          },
          confirmButton = {
            TextButton(
                onClick = {
                  cameraPermissionLauncher.launch(
                      arrayOf(
                          Manifest.permission.CAMERA,
                      ))
                  openAlertDialog.value = false
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
