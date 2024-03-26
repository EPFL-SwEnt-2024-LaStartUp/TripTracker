package com.example.triptracker.model.itinerary

/**
 * Data class for ItineraryList
 * Can get all tasks, get filtered tasks, get task by taskUid, and get size of itineraryList
 * @param itineraryList List of Itinerary
 */
data class ItineraryList(var itineraryList: List<Itinerary>) {
    fun getAllTask(): List<Itinerary> {
        return itineraryList
    }

    fun getFilteredTask(task: String): List<Itinerary> {
        return itineraryList.filter { it.title.contains(task, ignoreCase = true) }
    }

    fun getTask(taskUid: String): Itinerary {
        return itineraryList.find { it.id == taskUid }!!
    }

    fun size(): Int {
        return itineraryList.size
    }
}