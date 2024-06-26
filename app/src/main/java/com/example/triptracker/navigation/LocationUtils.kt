package com.example.triptracker.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.ktx.utils.sphericalDistance

/**
 * Utils function that checks if the location permission is granted. Need both fine and coarse
 * location permissions.
 */
fun checkForLocationPermission(context: Context): Boolean {
  return !(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
      android.content.pm.PackageManager.PERMISSION_GRANTED &&
      ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
          android.content.pm.PackageManager.PERMISSION_GRANTED)
}

@Composable
/**
 * Composable that displays an AlertDialog asking the user to allow location permission. If the user
 * allows the permission, the onPermissionGranted lambda is called. If the user denies the
 * permission, the onPermissionDenied lambda is called.
 */
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

/**
 * Composable that launches the permission request for location and sets the result to a mutable
 * state only if the permission was not already granted
 *
 * @param context The context of the activity (needed to check for location permission)
 */
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

@SuppressLint("MissingPermission")
/**
 * Function that fetches the real time current location of the device. If the priority is set to
 * true, the function will try to get the most accurate location. If the priority is set to false,
 * the function will try to get the location with the least battery consumption.
 *
 * @param context The context of the activity (needed to check for location permission)
 * @param onLocationFetched The lambda that is called when the location is fetched
 * @param priority The priority of the location request
 */
fun getCurrentLocation(
    context: Context,
    onLocationFetched: (LatLng) -> Unit,
    priority: Boolean = true
) {
  // Determine the accuracy priority based on the 'priority' parameter
  if (checkForLocationPermission(context)) {
    val accuracy =
        if (priority) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    var loc: LatLng
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationProviderClient
        .getCurrentLocation(
            accuracy,
            CancellationTokenSource().token,
        )
        .addOnSuccessListener { location: Location? ->
          if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            loc = LatLng(latitude, longitude)
            onLocationFetched(loc)
            Log.d("MAP-LOCATION", loc.toString())
          }
        }
        .addOnFailureListener { exception: Exception ->
          // Handle failure to get location
          Log.d("MAP-EXCEPTION", exception.message.toString())
        }
  }
}

/**
 * Function that compares the distance between two LatLng points and returns true if the distance is
 * less than or equal to the given distance in meters.
 *
 * @param latLng1 The first LatLng point
 * @param latLng2 The second LatLng point
 * @param distance The distance to compare
 * @return True if the distance between the two points is less than or equal to the given distance
 */
fun compareDistance(latLng1: LatLng, latLng2: LatLng, distance: Double): Boolean {
  Log.d("MAP-DISTANCE", latLng1.sphericalDistance(latLng2).toString())
  return latLng1.sphericalDistance(latLng2) <= distance
}

/**
 * Function that calculates the mean location of a list of LatLng points.
 *
 * @param list The list of LatLng points
 * @return The mean location of the list
 */
fun meanLocation(list: List<LatLng>): LatLng {
  var sumLat = 0.0
  var sumLng = 0.0
  for (i in list) {
    sumLat += i.latitude
    sumLng += i.longitude
  }
  return LatLng(sumLat / list.size, sumLng / list.size)
}
