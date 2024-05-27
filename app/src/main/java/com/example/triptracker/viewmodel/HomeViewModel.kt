package com.example.triptracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryDownload
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.network.Connection
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.view.home.dummyProfile
import kotlinx.coroutines.launch

/** Enum class for filter types */
enum class FilterType {
  TITLE,
  USERNAME,
  FLAME,
  PIN,
  FAVOURITES
}

enum class IncrementableField {
  FLAME_COUNT,
  SAVES,
  CLICKS,
  NUM_STARTS,
  DESCRIPTION,
  PINNED_PLACES,
  ROUTE,
  TITLE
}

enum class HomeCategory {
  TRENDING,
  FOLLOWING
}

/**
 * ViewModel for the Home Screen. Fetches all itineraries from the repository stores them in a
 * LiveData object
 *
 * @param repository the repository to fetch itineraries from
 * @param connection the connection object to check if the device is connected to the internet
 */
class HomeViewModel(
    private val repository: ItineraryRepository = ItineraryRepository(),
    private val connection: Connection = Connection()
) : ViewModel() {

  private var itineraryInstance = ItineraryList(listOf())
  private var _itineraryList = MutableLiveData<List<Itinerary>>(emptyList())
  val itineraryList: LiveData<List<Itinerary>> = _itineraryList

  private var _trendingList = MutableLiveData<List<Itinerary>>(emptyList())
  val trendingList: LiveData<List<Itinerary>> = _trendingList

  private var _followingList = MutableLiveData<List<Itinerary>>(emptyList())
  val followingList: LiveData<List<Itinerary>> = _followingList

  private val _searchQuery = MutableLiveData<String>("")

  private var currentCategory = HomeCategory.TRENDING
  val searchQuery: LiveData<String>
    get() = _searchQuery

  private val _selectedFilter = MutableLiveData<FilterType>(FilterType.TITLE)
  val selectedFilter: LiveData<FilterType> = _selectedFilter
  private var userProfileList = List(0) { dummyProfile }

  init {
    if (connection.isDeviceConnectedToInternet()) {
      UserProfileViewModel().fetchAllUserProfiles { userProfileList = it }
      viewModelScope.launch { fetchItineraries { updateAllFlameCounts() } }
    } else {
      // If the device is not connected to the internet, fetch itineraries from the internal storage
      _itineraryList.value = ItineraryDownload().loadAllItineraries()
    }
  }

  private val _userProfiles = MutableLiveData<Map<String, UserProfile>>()
  private val userProfiles: LiveData<Map<String, UserProfile>> = _userProfiles

  val filteredItineraryList: LiveData<List<Itinerary>> =
      _searchQuery.switchMap { query ->
        liveData {
          val filteredList =
              when (_selectedFilter.value) {
                FilterType.TITLE -> filterByTitle(query)
                FilterType.USERNAME -> filterByUsername(query) // now filters by user mail
                FilterType.FLAME -> parseFlameQuery(query)
                FilterType.PIN -> filterByPinName(query)
                FilterType.FAVOURITES -> filterByFavorite(query)
                else -> emptyList()
              }

          if (filteredList != null) {

            emit(filteredList)
          }
        }
      }

  /**
   * Fetches all itineraries from the repository and stores them in the itineraryList LiveData
   *
   * @param userEmail the email of the user to fetch itineraries for, used to filter by following
   * @param callback a callback, that can be used in various different ways for now, used for
   *   updating flame counts at launch
   */
  // Updated fetchItineraries to include category as an argument and update itineraries accordingly
  private fun fetchItineraries(callback: () -> Unit = {}) {
    Log.d("HomeViewModel", "Fetching itineraries")
    repository.getAllItineraries { itineraries ->
      itineraryInstance.setItineraryList(itineraries)
      _itineraryList.value = itineraryInstance.getAllItineraries()
      _followingList.value = itineraryInstance.getAllItineraries()
      _trendingList.value = itineraryInstance.getAllItineraries()
      callback()
    }
  }

  /**
   * Filter itineraries by title
   *
   * @param query the query to filter by
   * @return a list of itineraries that match the query
   */
  private fun filterByTitle(query: String) =
      itineraryList.value?.filter { it.title.contains(query, ignoreCase = true) }

  /**
   * Filter itineraries by username
   *
   * @param query the query to filter by
   * @return a list of itineraries that match the query
   */
  private fun filterByUsername(query: String): List<Itinerary> {
    val filteredItineraries = mutableListOf<Itinerary>()
    itineraryList.value?.forEach { itinerary ->
      val profile = userProfileList.firstOrNull { profile -> itinerary.userMail == profile.mail }
      if (profile != null && profile.username.contains(query, ignoreCase = true)) {
        filteredItineraries.add(itinerary)
      }
    }
    return filteredItineraries
  }

  /**
   * Filter itineraries by user mail
   *
   * @param query the query to filter by
   * @return a list of itineraries that match the query
   */
  private fun filterByUserMail(query: String) =
      itineraryList.value?.filter { it.userMail.contains(query, ignoreCase = true) }

  /**
   * Parse itineraries by flame count
   *
   * @param query the query to filter by
   * @return a list of itineraries that match the query
   */
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

  /**
   * Filter itineraries by flame count
   *
   * @param query the query to filter by
   */
  //  private fun filterByFlame(query: String) {
  //    itineraryList.value?.filter {
  //      val regex = """^([<>]=?)(\d+)""".toRegex()
  //      val matchResult = regex.matchEntire(query)
  //      val flameCount = it.flameCount
  //      when (matchResult?.groupValues?.get(1)) {
  //        "<" -> flameCount < matchResult.groupValues[2].toLong()
  //        "<=" -> flameCount <= matchResult.groupValues[2].toLong()
  //        ">" -> flameCount > matchResult.groupValues[2].toLong()
  //        ">=" -> flameCount >= matchResult.groupValues[2].toLong()
  //        "=" -> flameCount == matchResult.groupValues[2].toLong()
  //        else -> false
  //      }
  //    }
  //  }

  /**
   * Filter itineraries by pin name
   *
   * @param query the query to filter by
   * @return a list of itineraries that match the query
   */
  private fun filterByPinName(query: String) =
      itineraryList.value?.filter {
        it.pinnedPlaces.any { pin -> pin.name.contains(query, ignoreCase = true) }
      }

  /**
   * Filter itineraries by favorite
   *
   * @param usermail the usermail to filter by
   * @return a list of itineraries that match the query
   */
  private fun filterByFavorite(usermail: String): List<Itinerary> {
    val filteredItineraries = mutableListOf<Itinerary>()

    /**
     * If the device is not connected to the internet, return the list of itineraries from the
     * internal storage
     */
    if (!connection.isDeviceConnectedToInternet()) return _itineraryList.value ?: emptyList()

    val userProfile = userProfileList.firstOrNull { it.mail == usermail } ?: return emptyList()
    _itineraryList.value?.forEach { itinerary ->
      if (userProfile.favoritesPaths.contains(itinerary.id)) {
        filteredItineraries.add(itinerary)
      }
    }
    return filteredItineraries
  }

  /**
   * Set the search query
   *
   * @param query the query to filter by
   */
  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  /**
   * Set the search filter
   *
   * @param filterType the filter to apply
   */
  fun setSearchFilter(filterType: FilterType) {
    _selectedFilter.value = filterType
  }

  /**
   * Filter the itinerary list by trending itineraries based on the flame count The trending
   * itineraries are sorted in descending order of flame count Filtered list is stored in the
   * itineraryList LiveData
   */
  fun filterByTrending() {
    _trendingList.value =
        _trendingList.value?.sortedByDescending { itinerary -> itinerary.flameCount }
  }

  /**
   * Formula to calculate flame count for each itinerary based on the number of saves, clicks and
   * the number of users that started the itinerary
   */
  private fun calculateFlameCounts(saves: Long, clicks: Long, numStarts: Long): Long {
    return 2 * saves + clicks + 5 * numStarts
  }

  /**
   * Calculate the flame count for each itinerary and update the itineraryList the flame count is
   * calculated as 2 * saves + clicks + 5 * numStarts as number of user that started the itinerary
   * is presumably much smaller than the number of users that saved it and even less than the number
   * of clicks and more valuable. updates the db field flameCount
   *
   * @param itineraryId the id of the itinerary to update
   */
  private fun updateFlameCount(itineraryId: String) {
    repository.getItineraryById(itineraryId) { itinerary ->
      if (itinerary != null) {
        val newFlameCount =
            calculateFlameCounts(itinerary.saves, itinerary.clicks, itinerary.numStarts)
        repository.updateField(itineraryId, IncrementableField.FLAME_COUNT, newFlameCount)
      }
    }
  }

  /**
   * Update flame count for all itineraries can be used when flame counts from all itineraries need
   * to be updated
   */
  private fun updateAllFlameCounts() {
    _itineraryList.value?.forEach { itinerary -> updateFlameCount(itinerary.id) }
  }

  /** Increment the click count of the itinerary with the given id */
  fun incrementClickCount(itineraryId: String) {
    viewModelScope.launch {
      repository.incrementField(itineraryId, IncrementableField.CLICKS)
      // update flame count after incrementing click count
      updateFlameCount(itineraryId)
    }
  }

  /** Increment the save count of the itinerary with the given id */
  fun incrementSaveCount(itineraryId: String) {
    viewModelScope.launch {
      repository.incrementField(itineraryId, IncrementableField.SAVES)
      repository.getItineraryById(itineraryId) { itinerary ->
        if (itinerary != null) {
          val updatedItinerary = itinerary.copy(saves = itinerary.saves + 1)
          updateFlameCount(itinerary.id)
          _itineraryList.value =
              _itineraryList.value?.map { if (it.id == itineraryId) updatedItinerary else it }
        }
      }
    }
  }

  /** Increment the flame count of the itinerary with the given id */
  fun incrementNumStarts(itineraryId: String) {
    viewModelScope.launch {
      repository.incrementField(itineraryId, IncrementableField.NUM_STARTS)
      updateFlameCount(itineraryId)
    }
  }

  /** Filter the itinerary posted by your following users */
  fun filterByFollowing(usermail: String) {
    val userProfile = userProfileList.firstOrNull { it.mail == usermail } ?: return
    _followingList.value =
        _followingList.value?.filter { userProfile.following.contains(it.userMail) }
  }

  /**
   * Filter the itinerary list by trending itineraries in a certain city The trending itineraries
   * are sorted in descending order of flame count
   *
   * @param city the city to filter by
   * @return a list of itineraries that match the query sorted by flame count
   */
  //  fun filterTrendingCity(city: String) {
  //    _itineraryList.value =
  //        itineraryList.value
  //            ?.filter { it.location.name.split(", ").first().equals(city, ignoreCase = true) }
  //            ?.sortedByDescending { it.flameCount }
  //  }

  /**
   * Filter the itinerary list by trending itineraries in a certain country The trending itineraries
   * are sorted in descending order of flame count
   *
   * @param country the country to filter by
   * @return a list of itineraries that match the query sorted by flame count
   */
  //  fun filterTrendingCountry(country: String) {
  //    _itineraryList.value =
  //        itineraryList.value
  //            ?.filter { it.location.name.split(", ").last().equals(country, ignoreCase = true) }
  //            ?.sortedByDescending { it.flameCount }
  //  }

  /**
   * Filter the itinerary list by trending itineraries worldwide The trending itineraries are sorted
   * in descending order of flame count
   *
   * @return a list of itineraries sorted by flame count
   */
  //  fun filterTrendingWorldwide() {
  //    _itineraryList.value = itineraryList.value?.sortedByDescending { it.flameCount }
  //  }

  fun deleteItinerary(itineraryId: String, callback: () -> Unit = {}) {
    viewModelScope.launch {
      repository.removeItinerary(itineraryId) { fetchItineraries() { callback() } }
    }
  }
}
