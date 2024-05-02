package com.example.triptracker.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.profile.UserProfile
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
  private var _itineraryList = MutableLiveData<List<Itinerary>>(emptyList())
  val itineraryList: LiveData<List<Itinerary>> = _itineraryList
  private val _searchQuery = MutableLiveData<String>("")

  val searchQuery: LiveData<String>
    get() = _searchQuery

  private val _selectedFilter = MutableLiveData<FilterType>(FilterType.TITLE)
  val selectedFilter: LiveData<FilterType> = _selectedFilter

  private val userProfileViewModel: UserProfileViewModel = UserProfileViewModel()

  val filteredItineraryList: LiveData<List<Itinerary>> =
      _searchQuery.switchMap { query ->
        liveData {
          val filteredList =
              when (_selectedFilter.value) {
                FilterType.TITLE -> filterByTitle(query)
                FilterType.USERNAME -> filterByUsername(query)
                FilterType.FLAME -> parseFlameQuery(query)
                FilterType.PIN -> filterByPinName(query)
                else -> emptyList()
              }

          if (filteredList != null) {
            emit(filteredList)
          }
        }
      }

  init {
    viewModelScope.launch { fetchItineraries() }
  }

  /** Fetches all itineraries from the repository and stores them in the itineraryList LiveData */
  private fun fetchItineraries() {
    itineraryInstance.setItineraryList(repository.getAllItineraries())
    _itineraryList.value = itineraryInstance.getAllItineraries()
  }

  private fun filterByTitle(query: String) =
      itineraryList.value?.filter { it.title.contains(query, ignoreCase = true) }

  private fun filterByUsername(query: String) =
      itineraryList.value?.filter {
        var readyToDisplay = false
        var profile = UserProfile("")
        userProfileViewModel.getUserProfile(it.userMail) { itin ->
          if (itin != null) {
            profile = itin
            readyToDisplay = true
          }
        }

        Log.d("PROFILE", profile.toString())
        when (readyToDisplay) {
          false -> {
            Log.d("UserProfile", "User profile is null")
            profile.name.contains("", ignoreCase = true)
          }
          else -> {
            profile.name.contains(query, ignoreCase = true)
          }
        }
      }

  private fun parseFlameQuery(query: String): List<Itinerary> {
    val regex = """^([<>]=?|=)(\d+)""".toRegex()
    val matchResult = regex.matchEntire(query)

    return matchResult?.let {
      val (operator, valueStr) = it.destructured
      val value = valueStr.toLongOrNull() ?: return emptyList()
      itineraryList.value?.filter {
        when (operator) {
          "<" -> it.flameCount < value
          "<=" -> it.flameCount <= value
          ">" -> it.flameCount > value
          ">=" -> it.flameCount >= value
          "=" -> it.flameCount == value
          else -> false
        }
      }
    } ?: emptyList()
  }

  private fun filterByFlame(query: String) {
    itineraryList.value?.filter {
      val regex = """^([<>]=?)(\d+)""".toRegex()
      val matchResult = regex.matchEntire(query)
      val flameCount = it.flameCount
      when (matchResult?.groupValues?.get(1)) {
        "<" -> flameCount < matchResult.groupValues[2].toLong()
        "<=" -> flameCount <= matchResult.groupValues[2].toLong()
        ">" -> flameCount > matchResult.groupValues[2].toLong()
        ">=" -> flameCount >= matchResult.groupValues[2].toLong()
        "=" -> flameCount == matchResult.groupValues[2].toLong()
        else -> false
      }
    }
  }

  private fun filterByPinName(query: String) =
      itineraryList.value?.filter {
        it.pinnedPlaces.any { pin -> pin.name.contains(query, ignoreCase = true) }
      }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSearchFilter(filterType: FilterType) {
    _selectedFilter.value = filterType
  }
}
