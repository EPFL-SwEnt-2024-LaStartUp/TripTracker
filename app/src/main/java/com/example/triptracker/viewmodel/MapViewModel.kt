package com.example.triptracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.example.triptracker.model.geocoder.NominatimApi

/**
 * ViewModel for the MapOverview composable. It contains the city name state and the geocoder to
 * reverse decode the location.
 */
class MapViewModel {

  val geocoder = NominatimApi()

  val cityNameState = mutableStateOf("")

  fun reverseDecode(lat: Float, lon: Float) {
    geocoder.reverseDecode(lat, lon) { cityName -> cityNameState.value = cityName }
  }
}
