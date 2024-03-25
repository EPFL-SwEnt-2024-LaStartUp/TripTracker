package com.example.triptracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.example.triptracker.model.geocoder.NominatimApi

class MapViewModel {
  val geocoder = NominatimApi()

  val cityNameState = mutableStateOf("Lausanne")

  fun reverseDecode(lat: Float, lon: Float) {
    geocoder.reverseDecode(lat, lon) { cityName -> cityNameState.value = cityName }
  }
}
