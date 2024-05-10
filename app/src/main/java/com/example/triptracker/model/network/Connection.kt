package com.example.triptracker.model.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.triptracker.MainActivity.Companion.applicationContext

/** Class in charge of the network status */
class Connection(private val context: Context = applicationContext()) {

  /**
   * @return Boolean: True if the device is connected to the internet, false otherwise
   * @brief Function to check if the device is connected to the internet
   */
  fun isDeviceConnectedToInternet(): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
  }
}
