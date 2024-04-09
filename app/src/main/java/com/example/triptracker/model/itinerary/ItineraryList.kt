package com.example.triptracker.model.itinerary

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

/**
 * Data class for ItineraryList Can get all tasks, get filtered tasks, get task by taskUid, and get
 * size of itineraryList
 *
 * @param itineraryList List of Itinerary
 */
data class ItineraryList(var itineraryList: List<Itinerary>) {
  // Returns all tasks
  fun getAllItineraries(): List<Itinerary> {
    return itineraryList
  }

  // Returns filtered itinerary based on search query
  fun getFilteredItineraries(task: String): List<Itinerary> {
    return itineraryList.filter { it.title.contains(task, ignoreCase = true) }
  }

  // Returns filtered itinerary based on LatLngBounds of the screen
  fun getFilteredItineraries(latLngBounds: LatLngBounds, limit: Int): List<Itinerary> {
    return itineraryList
        .filter { latLngBounds.contains(LatLng(it.location.latitude, it.location.longitude)) }
        .take(limit)
  }

  // Returns task based on taskid
  fun getItinerary(taskUid: String): Itinerary {
    return itineraryList.find { it.id == taskUid }!!
  }

  fun size(): Int {
    return itineraryList.size
  }
}
