package com.example.triptracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.triptracker.model.geocoder.NominatimApi
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.repository.ItineraryRepository
import com.google.android.gms.maps.model.LatLng

/**
 * ViewModel for the MapOverview composable. It contains the city name state and the geocoder to
 * reverse decode the location.
 */
class MapViewModel {

  // geocoder with Nominatim API that allows to reverse decode the location
  val geocoder = NominatimApi()

  // state for the city name displayed at the top of the screen
  val cityNameState = mutableStateOf("")

  private val repository = ItineraryRepository()

  private val _pathList = MutableLiveData<Map<Location, List<LatLng>>>(emptyMap())
  val itineraryList: LiveData<Map<Location, List<LatLng>>> = _pathList
  /**
   * Reverse decodes the location to get the city name. On success update the cityNameState at the
   * top of the screen
   *
   * @param lat : latitude of the location
   * @param lon : longitude of the location
   */
  fun reverseDecode(lat: Float, lon: Float) {
    geocoder.reverseDecode(lat, lon) { cityName -> cityNameState.value = cityName }
  }

  fun getAllPaths() {
    repository.getAllItineraries { itineraries ->
      for (itinerary in itineraries) {
        //        _itineraryList.value += //TODO add the list of LatLng points of the itinerary
      }
    }
  }
}
