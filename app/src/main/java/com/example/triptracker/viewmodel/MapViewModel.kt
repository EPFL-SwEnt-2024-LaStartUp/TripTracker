package com.example.triptracker.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.geocoder.NominatimApi
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.location.popupState
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


    //ENUM
    var mapPopupState = mutableStateOf(popupState.DISPLAYITINERARY)
    var displayPopUp = mutableStateOf(false)


    // geocoder with Nominatim API that allows to reverse decode the location
  val geocoder = NominatimApi()

  // state for the city name displayed at the top of the screen
  val cityNameState = mutableStateOf("")

  private val repository = ItineraryRepository()

  private val _pathList = MutableLiveData<ItineraryList>()

  val filteredPathList = MutableLiveData<Map<Itinerary, List<LatLng>>>()

  /** Data class describing a selected Polyline */
  data class SelectedPolyline(val itinerary: Itinerary, val startLocation: LatLng)

  // Hold the selected polyline state
  val selectedPolylineState = mutableStateOf<SelectedPolyline?>(null)

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
    _pathList.postValue(ItineraryList(repository.getAllItineraries()))
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
  fun getFilteredPaths(latLngBounds: LatLngBounds?, limit: Int = 10) {
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

  /**
   * This function is used to update all the itineraries in the database with random routes. This is
   * temporary code
   */
  // TODO: REMOVE this function after the database is populated with real data
  /*
  private fun updateAllItineraries() {
    repository.getAllItineraries { itineraries ->
      var i = 0
      for (itinerary in itineraries) {
        val itin =
            Itinerary(
                id = itinerary.id,
                title = itinerary.title,
                username = itinerary.username,
                location =
                    Location(
                        ItineraryPath.itinerary[i][0].latitude,
                        ItineraryPath.itinerary[i][0].longitude,
                        PathName.pathName[i]),
                flameCount = itinerary.flameCount,
                startDateAndTime = itinerary.startDateAndTime,
                endDateAndTime = itinerary.endDateAndTime,
                pinnedPlaces = itinerary.pinnedPlaces,
                description = itinerary.description,
                route = ItineraryPath.itinerary[i])
        repository.updateItinerary(itin)
        Log.d("ITINERARY UPDATE", itin.toString())
        i++
      }
    }
  }

   */

  // TODO: Remove this function after the database is populated with real data
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

  // TODO: Remove this function after the database is populated with real data
  // Usage example to generate a list of random coordinates
  private fun generateRandomItinerary(numPoints: Int): List<LatLng> {
    val itinerary = mutableListOf<LatLng>()
    repeat(numPoints) {
      val coordinates = generateRandomCoordinates()
      itinerary.add(coordinates)
    }
    return itinerary
  }

  // TODO: Remove this function after the database is populated with real data
  object ItineraryPath {
    val itinerary =
        listOf(
            mutableListOf(
                LatLng(46.5249808, 6.5750806),
                LatLng(46.5248391, 6.574235),
                LatLng(46.5239124, 6.5744157),
                LatLng(46.5231392, 6.574878),
                LatLng(46.5225221, 6.5742038),
                LatLng(46.5216737, 6.5730692),
                LatLng(46.5208372, 6.5718573),
                LatLng(46.5194017, 6.5718857),
                LatLng(46.5181048, 6.5721191),
                LatLng(46.517468, 6.5694396),
                LatLng(46.5173975, 6.5685438),
                LatLng(46.5173996, 6.5685021),
                LatLng(46.5173997, 6.568506),
                LatLng(46.5173996, 6.568506),
                LatLng(46.5173996, 6.568506),
                LatLng(46.5173996, 6.568506),
                LatLng(46.5173997, 6.5685202),
                LatLng(46.5173996, 6.5685124),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.5685121),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173996, 6.568512),
                LatLng(46.5173987, 6.5684295)),
            mutableListOf(
                LatLng(46.5196886, 6.6312339),
                LatLng(46.5199785, 6.629541),
                LatLng(46.5204764, 6.6279257),
                LatLng(46.520885, 6.6262697),
                LatLng(46.5212299, 6.6245993),
                LatLng(46.5211999, 6.6228548),
                LatLng(46.5211003, 6.6208035),
                LatLng(46.5209662, 6.6179915),
                LatLng(46.5210907, 6.6156061),
                LatLng(46.52168, 6.6134016),
                LatLng(46.5224988, 6.6112582),
                LatLng(46.5231139, 6.6090377),
                LatLng(46.5235844, 6.6068726),
                LatLng(46.5240462, 6.6045827),
                LatLng(46.5240391, 6.6023018),
                LatLng(46.523632, 6.60118),
                LatLng(46.5232638, 6.5986351),
                LatLng(46.5238225, 6.5965297),
                LatLng(46.5242814, 6.5939838),
                LatLng(46.5238542, 6.5919749),
                LatLng(46.5228327, 6.5894882),
                LatLng(46.521677, 6.586325),
                LatLng(46.5205655, 6.5828869),
                LatLng(46.5200145, 6.5792621),
                LatLng(46.5191338, 6.5758524),
                LatLng(46.5182439, 6.5724327),
                LatLng(46.519351, 6.5716887),
                LatLng(46.5210986, 6.5718501),
                LatLng(46.5217582, 6.5733414),
                LatLng(46.5225773, 6.5744195),
                LatLng(46.523548, 6.5748171),
                LatLng(46.5246866, 6.5739602),
                LatLng(46.5252238, 6.5747292),
                LatLng(46.5250189, 6.5756129),
                LatLng(46.5250247, 6.5756222),
                LatLng(46.5250255, 6.5756204),
                LatLng(46.5250254, 6.5756209),
                LatLng(46.5250254, 6.575621),
                LatLng(46.5250254, 6.5756208),
                LatLng(46.5250255, 6.5756205)),
            mutableListOf(
                LatLng(48.8600309, 2.2917877),
                LatLng(48.8612942, 2.2924132),
                LatLng(48.8627392, 2.2946048),
                LatLng(48.8634928, 2.2972214),
                LatLng(48.8640907, 2.300418),
                LatLng(48.8645627, 2.3036768),
                LatLng(48.8646811, 2.3069451),
                LatLng(48.8647502, 2.3101751),
                LatLng(48.8648215, 2.3133785),
                LatLng(48.86489, 2.3164759),
                LatLng(48.8644219, 2.3194652),
                LatLng(48.8635816, 2.3219099),
                LatLng(48.8628874, 2.3238468),
                LatLng(48.8622945, 2.3256535),
                LatLng(48.8617, 2.3275104),
                LatLng(48.8610437, 2.3295579),
            ),
            mutableListOf(
                LatLng(40.7824499, -73.9635518),
                LatLng(40.7814837, -73.9627307),
                LatLng(40.7807192, -73.9619079),
                LatLng(40.7800183, -73.9615184),
                LatLng(40.7790517, -73.9622312),
                LatLng(40.7783178, -73.9627609),
                LatLng(40.777513, -73.9633532),
                LatLng(40.7766212, -73.9639996),
                LatLng(40.775816, -73.9645953),
                LatLng(40.7750486, -73.9651504),
                LatLng(40.7742413, -73.9657417),
                LatLng(40.7732567, -73.9664603),
                LatLng(40.7724586, -73.9670409),
                LatLng(40.7715904, -73.9676776),
                LatLng(40.7708167, -73.9682359),
                LatLng(40.7699782, -73.9688591),
            ),
            mutableListOf(
                LatLng(40.7650911, -73.9724212),
                LatLng(40.7641489, -73.9731148),
                LatLng(40.763464, -73.9736126),
                LatLng(40.7625021, -73.9743176),
                LatLng(40.7616794, -73.9748997),
                LatLng(40.760856, -73.9755125),
            ),
            mutableListOf(
                LatLng(46.5145065, 6.5719438),
                LatLng(46.5158471, 6.5712785),
                LatLng(46.5168591, 6.570119),
                LatLng(46.517441, 6.5687084),
                LatLng(46.5170871, 6.5655655),
                LatLng(46.5182141, 6.5653919),
                LatLng(46.5193477, 6.5655214),
                LatLng(46.5200905, 6.5659579),
            ))
  }

  // TODO: Remove this function after the database is populated with real data
  object PathName {
    val pathName =
        listOf("EPFL to Vortex", "Lausanne EPFL", "Paris to Eiffel", "NY1", "NY2", "Lac to EPFL")
  }
}
