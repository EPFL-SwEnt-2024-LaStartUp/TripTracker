package com.example.triptracker.model.itinerary

import com.google.android.gms.maps.model.LatLngBounds

/**
 * Data class for ItineraryList Can get all tasks, get filtered tasks, get task by taskUid, and get
 * size of itineraryList
 *
 * @param itineraryList List of Itinerary
 */
data class ItineraryList(private var itineraryList: List<Itinerary>) {

  /**
   * This function returns all the itineraries.
   *
   * @return List of all itineraries
   */
  fun getAllItineraries(): List<Itinerary> {
    return itineraryList
  }

  /**
   * This function sets the itinerary list.
   *
   * @param itineraryList : List of itineraries
   */
  fun setItineraryList(itineraryList: List<Itinerary>) {
    this.itineraryList = itineraryList
  }

  /**
   * This function adds an itinerary to the list.
   *
   * @param itinerary : itinerary to add
   */
  fun addItinerary(itinerary: Itinerary) {
    itineraryList = itineraryList + itinerary
  }

  /**
   * This function returns all the itineraries that match the query.
   *
   * @param task : String to search for in the itineraries
   * @return List of itineraries that match the task
   */
  fun getFilteredItineraries(task: String): List<Itinerary> {
    return itineraryList.filter { it.title.contains(task, ignoreCase = true) }
  }

  /**
   * This function returns all the itineraries that are within the latLngBounds.
   *
   * @param latLngBounds : LatLngBounds to search for in the itineraries
   * @param limit : Int to limit the number of itineraries
   * @return List of itineraries that are within the latLngBounds
   */
  fun getFilteredItineraries(latLngBounds: LatLngBounds, limit: Int): List<Itinerary> {
    return itineraryList
        .filter { itinerary ->
          itinerary.route.any() { latitudeLongitude -> latLngBounds.contains(latitudeLongitude) }
        }
        .take(limit)
  }

  /**
   * This function returns the itinerary that matches the taskUid.
   *
   * @param taskUid : unique identifier of the itinerary
   * @return Itinerary that matches the taskUid
   */
  fun getItinerary(taskUid: String): Itinerary {
    return itineraryList.find { it.id == taskUid }!!
  }

  /**
   * This function returns the size of the itineraryList.
   *
   * @return Int size of itineraryList
   */
  fun size(): Int {
    return itineraryList.size
  }
}
