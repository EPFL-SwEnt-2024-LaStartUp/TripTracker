package com.example.triptracker.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.geocoder.NominatimApi
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.repository.ItineraryRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlin.random.Random
import kotlinx.coroutines.launch

/**
 * ViewModel for the MapOverview composable. It contains the city name state and the geocoder to
 * reverse decode the location.
 */
class MapViewModel : ViewModel() {

  // geocoder with Nominatim API that allows to reverse decode the location
  val geocoder = NominatimApi()

  // state for the city name displayed at the top of the screen
  val cityNameState = mutableStateOf("")

  private val repository = ItineraryRepository()

  private val _pathList = MutableLiveData<ItineraryList>()

  val filteredPathList = MutableLiveData<Map<String, List<LatLng>>>()

  init {
    viewModelScope.launch { getAllItineraries() }
  }

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

  /** Get all itineraries from the database and update the pathList */
  private fun getAllItineraries() {
    repository.getAllItineraries { itineraries -> _pathList.postValue(ItineraryList(itineraries)) }
  }

  /**
   * Get all paths from the pathList in the format of the title of the itinerary and the route
   *
   * @return a map of the title of the itinerary and the route
   */
  fun getAllPaths(): Map<String, List<LatLng>> {
    return _pathList.value?.getAllItineraries()?.map { it.title to it.route }?.toMap() ?: emptyMap()
  }

  /**
   * Get the filtered paths based on the visible region of the map
   *
   * @param latLngBounds : the visible region of the map
   */
  fun getFilteredPaths(latLngBounds: LatLngBounds?) {
    if (latLngBounds == null) {
      filteredPathList.value = emptyMap()
    } else {
      filteredPathList.postValue(
          _pathList.value
              ?.getFilteredItineraries(latLngBounds)
              ?.map { it.title to it.route }
              ?.toMap() ?: emptyMap())
    }
  }

  /**
   * This function is used to update all the itineraries in the database with random routes. This is
   * temporary code
   */
  private fun updateAllItineraries() {
    repository.getAllItineraries { itineraries ->
      for (itinerary in itineraries) {
        val itin =
            Itinerary(
                id = itinerary.id,
                title = itinerary.title,
                username = itinerary.username,
                location = itinerary.location,
                flameCount = itinerary.flameCount,
                startDateAndTime = itinerary.startDateAndTime,
                endDateAndTime = itinerary.endDateAndTime,
                pinnedPlaces = itinerary.pinnedPlaces,
                description = itinerary.description,
                route = generateRandomItinerary(Random.nextInt(5, 10)))
        repository.updateItinerary(itin)
        Log.d("ITINERARY UPDATE", itin.toString())
      }
    }
  }

  private fun generateRandomCoordinates(): LatLng {
    // Define the latitude and longitude range (adjust as needed)
    val minLat = 46.51
    val maxLat = 46.53
    val minLon = 6.62
    val maxLon = 6.63

    // Generate random latitude and longitude values within the specified range
    val randomLat = Random.nextDouble(minLat, maxLat)
    val randomLon = Random.nextDouble(minLon, maxLon)

    return LatLng(randomLat, randomLon)
  }

  // Usage example to generate a list of random coordinates
  private fun generateRandomItinerary(numPoints: Int): List<LatLng> {
    val itinerary = mutableListOf<LatLng>()
    repeat(numPoints) {
      val coordinates = generateRandomCoordinates()
      itinerary.add(coordinates)
    }
    return itinerary
  }
}
