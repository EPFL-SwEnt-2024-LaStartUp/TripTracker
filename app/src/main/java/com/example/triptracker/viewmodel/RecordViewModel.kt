package com.example.triptracker.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.geocoder.NominatimApi
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.repository.ImageRepository
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.model.repository.Response
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

/**
 * ViewModel for recording trips. This ViewModel is responsible for managing the state of a trip
 * recording, including start time, end time, pause status, and the list of LatLng points.
 */
class RecordViewModel(
    private val geocoder: NominatimApi = NominatimApi(),
    private val imageRepository: ImageRepository = ImageRepository(),
    private val _latLongList: MutableList<LatLng> = mutableListOf(),
    private var _pinList: MutableList<Pin> = mutableListOf()
) : ViewModel() {

  // Start time and date of the recording
  private val startTime = mutableLongStateOf(0L)
  val startDate = mutableStateOf("")
  // End time and date of the recording
  private val endTime = mutableLongStateOf(0L)
  val endDate = mutableStateOf("")
  // Boolean state representing whether the recording is paused
  val isPaused = mutableStateOf(false)

  // Boolean state representing whether the description is done or not
  private val inDescription = mutableStateOf(false)
  val description = mutableStateOf("")
  val title = mutableStateOf("")

  // Public immutable list of LatLng points
  val latLongList: List<LatLng>
    get() = _latLongList

  // Public immutable list of LatLng points
  val pinList: List<Pin>
    get() = _pinList

  // Geocoder object to interact with the Nominatim API

  // Point of interest name
  val namePOI = mutableStateOf("")

  // Dropdown menu for POI name
  val displayNameDropDown = mutableStateOf("")

  // AddSpotWindow
  var addSpotClicked = mutableStateOf(false)

  // ImageRepository

  var addImageToStorageResponse by mutableStateOf<List<Response<Uri>>>(emptyList())

  /** Starts the recording. Sets the start time to the current time. */
  fun startRecording() {
    // reset the recording to clear any previous data
    resetRecording()
    startTime.longValue = System.currentTimeMillis()
    // get the current date
    startDate.value = java.time.LocalDate.now().toString()
  }

  /** Stops the recording. Sets the end time to the current time and logs the data collected. */
  fun stopRecording() {
    // if the recording is paused, unpause it and terminate the recording
    if (isPaused.value) {
      isPaused.value = false
    }
    endTime.longValue = System.currentTimeMillis()
    // get the current date
    endDate.value = java.time.LocalDate.now().toString()
    Log.e(
        "DATA",
        "Data collected: ${_latLongList.size} points, ${getElapsedTime()} ms, ${prettyPrint(_latLongList)}")
    // clear the list
  }

  /**
   * Pretty prints the list of LatLng points.
   *
   * @param list The list of LatLng points to pretty print.
   */
  private fun prettyPrint(list: List<LatLng>): String {
    var res = "mutableListOf(\n"
    for (i in list) {
      res += "LatLng(${i.latitude}, ${i.longitude})," + "\n"
    }
    res += ")"
    return res
  }

  /** Resets the recording. Clears the start time, end time, and LatLng list. */
  fun resetRecording() {
    startTime.longValue = 0L
    endTime.longValue = 0L
    startDate.value = ""
    endDate.value = ""
    _latLongList.clear()
  }

  /**
   * Pauses the recording. Sets the end time to the current time and sets the pause status to true.
   */
  fun pauseRecording() {
    endTime.longValue = System.currentTimeMillis()
    isPaused.value = true
  }

  /**
   * Resumes the recording. Adjusts the start time based on the duration of the pause and sets the
   * pause status to false.
   */
  fun resumeRecording() {
    startTime.longValue += System.currentTimeMillis() - endTime.longValue
    isPaused.value = false
  }

  /** Starts the description phase. */
  fun startDescription() {
    inDescription.value = true
  }
  /** Stops the description phase. */
  fun stopDescription() {
    inDescription.value = false
    description.value = ""
    title.value = ""
  }

  /**
   * Checks if the recording is in the description phase.
   *
   * @return Boolean indicating whether the recording is in the description phase.
   */
  fun isInDescription(): Boolean {
    return inDescription.value
  }

  /**
   * Checks if the recording is ongoing.
   *
   * @return Boolean indicating whether the recording is ongoing.
   */
  fun isRecording(): Boolean {
    return startTime.longValue != 0L && endDate.value == "" && startDate.value != ""
  }

  /**
   * Calculates the elapsed time of the recording.
   *
   * @return Long representing the elapsed time in milliseconds.
   */
  fun getElapsedTime(): Long {
    return if (isRecording() && !isPaused.value) {
      val cur = System.currentTimeMillis()
      cur - startTime.longValue
    } else if (isRecording() && isPaused.value) {
      endTime.longValue - startTime.longValue
    } else {
      0L
    }
  }

  /**
   * Adds a LatLng point to the list.
   *
   * @param latLng The LatLng point to add.
   */
  fun addLatLng(latLng: LatLng) {
    _latLongList.add(latLng)
  }

  /**
   * Adds a Pin point to the list.
   *
   * @param pin The LatLng point to add.
   */
  fun addPin(pin: Pin) {
    _pinList.add(pin)
  }

  /**
   * Adds new itinerary to the database.
   *
   * @param itinerary Itinerary object to add to the database
   * @param itineraryRepository ItineraryRepository object to interact with the database
   */
  fun addNewItinerary(itinerary: Itinerary, itineraryRepository: ItineraryRepository) {
    viewModelScope.launch {
      try {
        itineraryRepository.addNewItinerary(itinerary)
      } catch (e: Exception) {
        // Handle the exception, e.g. show a message to the user
      }
    }
  }

  /**
   * Reverse decodes the location to get the city name. On success update the cityNameState at the
   * top of the screen
   *
   * @param lat : latitude of the location
   * @param lon : longitude of the location
   * @param callback : function to call when the city and country names are decoded
   */
  fun getCityAndCountry(lat: Float, lon: Float, callback: (String) -> Unit) {
    geocoder.getCity(lat, lon, callback = callback, true)
  }

  /** Gets the point of interest (POI) name at the given LatLng point. */
  fun getPOI(latLng: LatLng) {
    geocoder.getPOI(latLng.latitude.toFloat(), latLng.longitude.toFloat()) { name ->
      namePOI.value = name
    }
  }

  /**
   * Gets the suggestion name at the given LatLng point and returns the LAT/LNG of the proposed
   * location
   */
  fun getSuggestion(query: String, callback: (LatLng) -> Unit) {
    geocoder.decode(query) { location ->
      displayNameDropDown.value = location.name
      Log.d("API RESPONSE", location.name)
      callback(LatLng(location.latitude, location.longitude))
    }
  }

  /**
   * Adds an image to the storage.
   */
  fun addImageToStorage(imageUri: Uri, callback: (Response<Uri>) -> Unit) {
    viewModelScope.launch {
      val elem = imageRepository.addImageToFirebaseStorage(imageRepository.pinPictures, imageUri)
      callback(elem)
    }
  }
}
