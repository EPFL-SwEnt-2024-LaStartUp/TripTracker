package com.example.triptracker.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.repository.ItineraryRepository
import kotlinx.coroutines.launch

/** Enum class for filter types */
enum class FilterType {
  TITLE,
  USERNAME,
  FLAME,
  PIN
}

/**
 * ViewModel for the Home Screen. Fetches all itineraries from the repository stores them in a
 * LiveData object
 */
class HomeViewModel(private val repository: ItineraryRepository = ItineraryRepository()) :
    ViewModel() {

  private var itineraryInstance = ItineraryList(listOf())
  private var _itineraryList = MutableLiveData<List<Itinerary>>()
  val itineraryList: LiveData<List<Itinerary>> = _itineraryList
  var flameRange by mutableStateOf(0..100000)

  private val _selectedFilter = MutableLiveData<FilterType>(FilterType.TITLE)
  val selectedFilter: LiveData<FilterType> = _selectedFilter

  private var _pinNamesMap =
      MutableLiveData<Map<String, List<String>>>() // Map of itinerary ID to list of pin names
  val pinNamesMap: LiveData<Map<String, List<String>>> = _pinNamesMap

  // Search query LiveData
  private val _searchQuery = MutableLiveData<String>("")
  val searchQuery: LiveData<String>
    get() = _searchQuery

  // Fetch all itineraries from the repository on initialization
  init {
    viewModelScope.launch { fetchItineraries() }
  }

  /** Fetches all itineraries from the repository and stores them in the itineraryList LiveData */
  private fun fetchItineraries() {
    Log.d("HomeViewModel", "Fetching itineraries")
    itineraryInstance.setItineraryList(repository.getAllItineraries())
    _itineraryList.value = itineraryInstance.getAllItineraries()
    Log.d("HomeViewModel", repository.getAllItineraries().toString())
  }

  fun setSearchFilter(filterType: FilterType) {
    _selectedFilter.value = filterType
  }
  // Function to update search query
  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  // Enhanced Filtered Itinerary list LiveData based on search query and selected filter
  val filteredItineraryList: LiveData<List<Itinerary>> =
      _searchQuery.switchMap { query ->
        liveData {
          val filteredList =
              when (_selectedFilter.value) {
                FilterType.TITLE ->
                    itineraryList.value?.filter { it.title.contains(query, ignoreCase = true) }
                FilterType.USERNAME -> {
                  emptyList() // TODO change this
                  // itineraryList.value?.filter {
                  // val username = UserProfileViewModel().getUserProfile(it.userMail)?.username
                  // username?.contains(query, ignoreCase = true) ?: false
                }
                FilterType.FLAME -> parseFlameQuery(query, itineraryList.value)
                FilterType.PIN ->
                    itineraryList.value?.filter { itinerary ->
                      itinerary.pinnedPlaces.any { pin ->
                        pin.name.contains(query, ignoreCase = true)
                      }
                    }
                else -> emptyList()
              } ?: emptyList()
          emit(filteredList)
        }
      }

  /**
   * Helper function to parse flame count query
   *
   * @param query: search query
   * @param itineraries: list of itineraries
   * @return List of itineraries that match the flame count query
   */
  private fun parseFlameQuery(query: String, itineraries: List<Itinerary>?): List<Itinerary>? {
    val operatorRegex = """([<>]=?)(\d+)""".toRegex()
    val matchResult = operatorRegex.find(query)
    return matchResult?.let {
      val (operator, valueStr) = it.groups[1]?.value to it.groups[2]?.value
      val value = valueStr?.toIntOrNull()
      when {
        operator == "<" && value != null ->
            itineraries?.filter { itinerary -> itinerary.flameCount < value }
        operator == "<=" && value != null ->
            itineraries?.filter { itinerary -> itinerary.flameCount <= value }
        operator == ">" && value != null ->
            itineraries?.filter { itinerary -> itinerary.flameCount > value }
        operator == ">=" && value != null ->
            itineraries?.filter { itinerary -> itinerary.flameCount >= value }
        operator == "=" && value != null ->
            itineraries?.filter { itinerary -> itinerary.flameCount.toInt() == value }
        else -> null
      }
    } ?: itineraries // return all itineraries if the query is not matched
  }
}
