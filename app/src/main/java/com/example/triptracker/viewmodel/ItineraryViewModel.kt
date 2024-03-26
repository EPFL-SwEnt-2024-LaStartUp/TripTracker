package com.example.triptracker.viewmodel

import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.repository.ItineraryRepository

/**
 * ViewModel for the Itinerary
 */
class ItineraryViewModel {
    val itineraryRepository = ItineraryRepository()
    val itineraryList = ItineraryList(mutableListOf())


    // Would be useful for when editing an itinerary
    fun getAllItinerariesFromDb() {
        itineraryList.itineraryList = itineraryRepository.getAllItineraries()
    }
}