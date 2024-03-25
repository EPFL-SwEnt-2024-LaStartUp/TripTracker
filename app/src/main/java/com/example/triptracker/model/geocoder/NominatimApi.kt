package com.example.triptracker.model.geocoder

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class NominatimApi {
  private val searchURL = "https://nominatim.openstreetmap.org/search?q="
  private val reverseURL = "https://nominatim.openstreetmap.org/reverse?"
  private val format = "&format=json"

  private val httpClient = OkHttpClient()

  private fun getSearchUrl(query: String): String {
    return "$searchURL$query$format"
  }

  private fun getReverseUrl(lat: Float, lon: Float): String {
    return "${reverseURL}lat=$lat&lon=$lon$format"
  }

  fun reverseDecode(lat: Float, lon: Float, callback: (String) -> Unit) {
    val url = getReverseUrl(lat, lon)
    val request = Request.Builder().url(url).build()
    val call = httpClient.newCall(request)
    call.enqueue(
        object : Callback {
          override fun onFailure(call: Call, e: IOException) {
            Log.d("API RESPONSE", "Failed to execute request")
            callback("Unknown")
          }

          override fun onResponse(call: Call, response: Response) {
            val result = response.body?.string()
            if (result == "{\"error\":\"Unable to geocode\"}" || result == "") {
              callback("Unknown")
              return
            }
            val json = result?.let { JSONObject(it) }

            val address = json?.get("address")

            if (address == null) {
              callback("Unknown")
              return
            }

            val addressJson = address as JSONObject
            if (addressJson.has("city")) {
              val cityName = addressJson.get("city") as? String ?: "Unknown City"
              callback(cityName)
            } else if (addressJson.has("village")) {
              val village = addressJson.get("village") as? String ?: "Unknown Village"
              callback(village)
            } else if (addressJson.has("town")) {
              val town = addressJson.get("town") as? String ?: "Unknown Town"
              callback(town)
            } else {
              callback("Unknown")
            }
          }
        })
  }
}
