package com.example.triptracker.model.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.triptracker.MainActivity.Companion.applicationContext

/** Class in charge of the network status */
class Connection() {
  private var context: Context? = null

  init {
    try {
      context = applicationContext()
    } catch (e: Exception) { // If the context is not available because of testing
      e.printStackTrace()
    }
  }

  /**
   * Function to check if the device is connected to the internet. If the function is called when
   * testing, always return true
   *
   * @return Boolean: True if the device is connected to the internet, false otherwise
   */
  fun isDeviceConnectedToInternet(): Boolean {
    // Context can be null when testing because no application context
    if (context == null) {
      return true
    }

    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
  }
}
