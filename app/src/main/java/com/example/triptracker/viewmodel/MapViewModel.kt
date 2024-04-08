package com.example.triptracker.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.triptracker.model.geocoder.NominatimApi
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

  private val _pathList = MutableLiveData<Map<String, List<LatLng>>>(emptyMap())
  val pathList: LiveData<Map<String, List<LatLng>>> = _pathList
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
    //    repository.getAllItineraries { itineraries ->
    //      for (itinerary in itineraries) {
    //        //        _pathList.value += //TODO add the list of LatLng points of the itinerary
    //      }

    _pathList.value =
        mapOf(
            "Lausanne" to
                listOf(
                    LatLng(46.5236427, 6.5746056),
                    LatLng(46.5234576, 6.5747337),
                    LatLng(46.522897, 6.5748407),
                    LatLng(46.5226874, 6.5747289),
                    LatLng(46.5216868, 6.5731126),
                    LatLng(46.5216313, 6.5722793)),
            "Ecublens" to
                listOf(
                    LatLng(46.5199086, 6.6297869),
                    LatLng(46.5200088, 6.6294613),
                    LatLng(46.5201087, 6.6291317),
                    LatLng(46.520209, 6.6288057),
                    LatLng(46.520309, 6.6284807),
                    LatLng(46.5205159, 6.6277878),
                ))
  }
}
