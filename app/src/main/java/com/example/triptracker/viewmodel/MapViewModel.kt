package com.example.triptracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.example.triptracker.model.geocoder.NominatimApi

/**
 * ViewModel for the MapOverview composable. It contains the city name state and the geocoder to
 * reverse decode the location.
 */
class MapViewModel {

  // geocoder with Nominatim API that allows to reverse decode the location
  val geocoder = NominatimApi()

  // state for the city name displayed at the top of the screen
  val cityNameState = mutableStateOf("")

  /**
   * Reverse decodes the location to get the city name.
   *
   * @param lat latitude of the location
   * @param lon longitude of the location On success update the cityNameState on the top of the
   *   screen
   */
  fun reverseDecode(lat: Float, lon: Float) {
    geocoder.reverseDecode(lat, lon) { cityName -> cityNameState.value = cityName }
  }
}
