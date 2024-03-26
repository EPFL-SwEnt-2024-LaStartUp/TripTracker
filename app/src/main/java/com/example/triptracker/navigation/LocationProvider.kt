package com.example.triptracker.navigation

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationProvider {

  /**
   * Retrieves the current user location asynchronously.
   *
   * @param onGetCurrentLocationSuccess Callback function invoked when the current location is
   *   successfully retrieved. It provides a Pair representing latitude and longitude.
   * @param onGetCurrentLocationFailed Callback function invoked when an error occurs while
   *   retrieving the current location. It provides the Exception that occurred.
   * @param priority Indicates the desired accuracy of the location retrieval. Default is high
   *   accuracy. If set to false, it uses balanced power accuracy.
   */
  @SuppressLint("MissingPermission")
  fun getCurrentLocation(
      context: Context,
      onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
      onGetCurrentLocationFailed: (Exception) -> Unit,
      priority: Boolean = true
  ) {
    // Determine the accuracy priority based on the 'priority' parameter
    val accuracy =
        if (priority) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    // Check if location permissions are granted
    if (checkForLocationPermission(context)) {
      // Retrieve the current location asynchronously
      fusedLocationProviderClient
          .getCurrentLocation(
              accuracy,
              CancellationTokenSource().token,
          )
          .addOnSuccessListener { location ->
            location?.let {
              // If location is not null, invoke the success callback with latitude and longitude
              onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }
          }
          .addOnFailureListener { exception ->
            // If an error occurs, invoke the failure callback with the exception
            onGetCurrentLocationFailed(exception)
          }
    }
  }
}
