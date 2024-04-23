package com.example.triptracker.viewmodel

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
import kotlinx.coroutines.launch

/**
 * ViewModel for the MapOverview composable. It contains the city name state and the geocoder to
 * reverse decode the location.
 *
 * @param geocoder: Geocoder with Nominatim API that allows to reverse decode the location
 *   (optional)
 * @param pathList: Mutable list of that contains all the itineraries (optional)
 * @param repository: Repository containing the itineraries for Firebase interactions (optional)
 * @param filteredPathList: Mutable map of Itinerary linked to a list of coordinates for filtering
 *   purposes (optional)
 */
class MapViewModel(
    val geocoder: NominatimApi = NominatimApi(),
    private val pathList: MutableLiveData<ItineraryList> = MutableLiveData<ItineraryList>(),
    private val repository: ItineraryRepository = ItineraryRepository(),
    val filteredPathList: MutableLiveData<Map<Itinerary, List<LatLng>>> =
        MutableLiveData<Map<Itinerary, List<LatLng>>>()
) : ViewModel() {

  // state for the city name displayed at the top of the screen
  val cityNameState = mutableStateOf("")

  /** Data class describing a selected Polyline */
  data class SelectedPolyline(val itinerary: Itinerary, val startLocation: LatLng)

  // Hold the selected polyline state
  var selectedPolylineState = mutableStateOf<SelectedPolyline?>(null)

  // Expose the selected polyline state as immutable

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
  fun reverseDecode(lat: Float, lon: Float, _geocoder: NominatimApi = geocoder) {
    _geocoder.getCity(lat, lon) { cityName -> cityNameState.value = cityName }
  }

  /** Get all itineraries from the database and update the pathList */
  private fun getAllItineraries() {
    pathList.postValue(ItineraryList(repository.getAllItineraries()))
  }

  /**
   * Get all paths from the pathList in the format of the title of the itinerary and the route
   *
   * @return a map of the title of the itinerary and the route
   */
  fun getAllPaths(_pathList: MutableLiveData<ItineraryList> = pathList): Map<String, List<LatLng>> {
    return _pathList.value?.getAllItineraries()?.map { it.title to it.route }?.toMap() ?: emptyMap()
  }

  /**
   * Get the filtered paths based on the visible region of the map
   *
   * @param latLngBounds : the visible region of the map
   */
  fun getFilteredPaths(
      latLngBounds: LatLngBounds?,
      limit: Int = 10,
      _pathList: MutableLiveData<ItineraryList> = pathList
  ) {
    if (latLngBounds == null) {
      filteredPathList.value = emptyMap()
    } else {
      filteredPathList.postValue(
          _pathList.value
              ?.getFilteredItineraries(latLngBounds, limit)
              ?.map { it to it.route }
              ?.toMap() ?: emptyMap())
    }
  }
}
