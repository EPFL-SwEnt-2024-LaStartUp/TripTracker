package com.example.triptracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.repository.ItineraryRepository

/**
 * ViewModel for the Home Screen
 * Fetches all itineraries from the repository
 * stores them in a LiveData object
 */
class HomeViewModel : ViewModel() {
    private val repository = ItineraryRepository()

    private var itineraryInstance = ItineraryList(listOf())
    private var _itineraryList = MutableLiveData<List<Itinerary>>()
    val itineraryList: LiveData<List<Itinerary>> = _itineraryList

    init {
        fetchItineraries()
    }

    private fun fetchItineraries() {
        itineraryInstance.itineraryList = repository.getAllItineraries()
        _itineraryList.value = itineraryInstance.itineraryList
    }
}