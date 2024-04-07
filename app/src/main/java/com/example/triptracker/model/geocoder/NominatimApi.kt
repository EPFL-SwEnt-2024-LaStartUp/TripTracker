package com.example.triptracker.model.geocoder

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * Class containing all the nominatim API variants that describe a city to reverse decode the
 * location.
 */
object LocationStrings {
  const val ADDRESS = "address"
  const val CITY = "city"
  const val VILLAGE = "village"
  const val TOWN = "town"
}

/**
 * Class containing all the error messages that can be returned by the API. Unknown: when nothing
 * was decoded callback with this Error: when the API returns an error json object Fail: when the
 * request fails
 */
object LocationErrors {
  const val UNKNOWN = "Unknown"
  const val ERROR = "{\"error\":\"Unable to geocode\"}"
  const val FAIL = "Failed to execute request"
}

/** Class to interact with the Nominatim API to reverse decode the location. */
class NominatimApi {

  // URL for the search query (address to lat and lon)
  private val searchURL = "https://nominatim.openstreetmap.org/search?q="

  // URL for the reverse query (lat and lon to address)
  private val reverseURL = "https://nominatim.openstreetmap.org/reverse?"

  // format of the response
  private val format = "&format=json"

  // HTTP client
  private val httpClient = OkHttpClient()

  /**
   * Function to get the search URL given a query.
   *
   * @param query : address to search for
   * @return the search URL
   */
  private fun getSearchUrl(query: String): String {
    return "$searchURL$query$format"
  }

  /**
   * Function to get the reverse search URL.
   *
   * @param lat : latitude of the location
   * @param lon : longitude of the location
   * @return the reverse search URL
   */
  private fun getReverseUrl(lat: Float, lon: Float): String {
    return "${reverseURL}lat=$lat&lon=$lon$format"
  }

  /**
   * Function to reverse decode the location.
   *
   * @param lat : latitude of the location
   * @param lon : longitude of the location
   * @param callback : function to call when the location is decoded into a city
   */
  fun reverseDecode(lat: Float, lon: Float, callback: (String) -> Unit) {

    // generate the URL
    val url = getReverseUrl(lat, lon)
    val request = Request.Builder().url(url).build()
    val call = httpClient.newCall(request)

    // execute the request
    call.enqueue(
        object : Callback {

          // if the request fails log the error and call the callback with the error
          override fun onFailure(call: Call, e: IOException) {
            Log.d("API RESPONSE", LocationErrors.FAIL)
            callback(LocationErrors.UNKNOWN)
          }

          // if the request is successful, parse the response and call the callback with the city
          override fun onResponse(call: Call, response: Response) {
            val result = response.body?.string()
            if (result == LocationErrors.ERROR || result == "") {
              callback(LocationErrors.UNKNOWN)
              return
            }
            // parse the response in json
            val json = result?.let { JSONObject(it) }

            // get the address from the json (which is a new json object)
            val address = json?.get(LocationStrings.ADDRESS)

            if (address == null) {
              callback(LocationErrors.UNKNOWN)
              return
            }

            // get the city, village or town from the address json object since there are multiple
            // ways to describe a "city"
            val addressJson = address as JSONObject
            if (addressJson.has(LocationStrings.CITY)) {
              val cityName =
                  addressJson.get(LocationStrings.CITY) as? String ?: LocationErrors.UNKNOWN
              callback(cityName)
            } else if (addressJson.has(LocationStrings.VILLAGE)) {
              val village =
                  addressJson.get(LocationStrings.VILLAGE) as? String ?: LocationErrors.UNKNOWN
              callback(village)
            } else if (addressJson.has(LocationStrings.TOWN)) {
              val town = addressJson.get(LocationStrings.TOWN) as? String ?: LocationErrors.UNKNOWN
              callback(town)
            } else {
              callback(LocationErrors.UNKNOWN)
            }
          }
        })
  }
}
