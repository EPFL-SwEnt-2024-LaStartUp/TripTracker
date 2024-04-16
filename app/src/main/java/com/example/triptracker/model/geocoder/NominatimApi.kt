package com.example.triptracker.model.geocoder

import android.util.Log
import com.example.triptracker.model.location.Location
import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

/**
 * Class containing all the nominatim API variants that describe a city to reverse decode the
 * location.
 */
object LocationStrings {
  const val ADDRESS = "address"
  const val CITY = "city"
  const val VILLAGE = "village"
  const val TOWN = "town"
  const val NAME = "name"
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

  fun decode(query: String, callback: (Location) -> Unit) {
    val url = getSearchUrl(query)
    val request = Request.Builder().url(url).build()
    val call = httpClient.newCall(request)

    call.enqueue(
        object : Callback {
          override fun onFailure(call: Call, e: IOException) {
            callback(Location(0.0, 0.0, ""))
          }

          override fun onResponse(call: Call, response: Response) {
            val result = response.body?.string()
            val cleaned = result?.drop(1)?.dropLast(1)
            if (cleaned == "") {
              callback(Location(0.0, 0.0, ""))
              return
            }
            val json = cleaned?.let { JSONObject(it) }

            val lat = json?.get("lat").toString().toDoubleOrNull()
            val lon = json?.get("lon").toString().toDoubleOrNull()
            val display = json?.get("display_name").toString()
            if (display == "" || lat == null || lon == null) {
              callback(Location(0.0, 0.0, ""))
              return
            }
            Log.d("API RESPONSE", result ?: "")
            val location = Location(name = display, latitude = lat, longitude = lon)
            callback(location)
          }
        })
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
   * @param callback : function to call when the location is decoded into a json object
   */
  private fun reverseDecode(lat: Float, lon: Float, callback: (JSONObject?) -> Unit) {

    // generate the URL
    val url = getReverseUrl(lat, lon)
    val request = Request.Builder().url(url).build()
    val call = httpClient.newCall(request)

    // execute the request
    call.enqueue(
        object : Callback {

          // if the request fails log the error and call the callback with the error
          override fun onFailure(call: Call, e: IOException) {
            callback(JSONObject())
          }

          // if the request is successful, parse the response and call the callback with the city
          override fun onResponse(call: Call, response: Response) {
            val result = response.body?.string()
            if (result == LocationErrors.ERROR || result == "") {
              callback(JSONObject(LocationErrors.ERROR))
              return
            }
            // parse the response in json
            val json = result?.let { JSONObject(it) }

            callback(json)
          }
        })
  }

  /**
   * Function to get the city from the location.
   *
   * @param lat : latitude of the location
   * @param lon : longitude of the location
   * @param callback : function to call when the city is decoded
   */
  fun getCity(lat: Float, lon: Float, callback: (String) -> Unit) {
    reverseDecode(lat, lon) { json ->
      if (json.toString() == LocationErrors.ERROR) {
        callback(LocationErrors.UNKNOWN)
        return@reverseDecode
      }
      // get the address from the json (which is a new json object)
      val address = json?.get(LocationStrings.ADDRESS)

      if (address == null) {
        callback(LocationErrors.UNKNOWN)
        return@reverseDecode
      }

      // get the city, village or town from the address json object since there are multiple
      // ways to describe a "city"
      val addressJson = address as JSONObject
      if (addressJson.has(LocationStrings.CITY)) {
        val cityName = addressJson.get(LocationStrings.CITY) as? String ?: LocationErrors.UNKNOWN
        callback(cityName)
      } else if (addressJson.has(LocationStrings.VILLAGE)) {
        val village = addressJson.get(LocationStrings.VILLAGE) as? String ?: LocationErrors.UNKNOWN
        callback(village)
      } else if (addressJson.has(LocationStrings.TOWN)) {
        val town = addressJson.get(LocationStrings.TOWN) as? String ?: LocationErrors.UNKNOWN
        callback(town)
      } else {
        callback(LocationErrors.UNKNOWN)
      }
    }
  }

  /**
   * Function to get the point of interest from the location.
   *
   * @param lat : latitude of the location
   * @param lon : longitude of the location
   * @param callback : function to call when the point of interest is decoded
   */
  fun getPOI(lat: Float, lon: Float, callback: (String) -> Unit) {
    reverseDecode(lat, lon) { json ->
      if (json.toString() == LocationErrors.ERROR) {
        callback(LocationErrors.UNKNOWN)
        return@reverseDecode
      }

      Log.d("API RESPONSE", json.toString())

      val name = json?.get(LocationStrings.NAME)

      if (name == null) {
        callback(LocationErrors.UNKNOWN)
        return@reverseDecode
      }

      callback(name as String)
    }
  }
}
