package com.example.triptracker.model.itinerary

import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin

/**
    * This class represents the Itinerary object which contains the details of the trip
    * @param id : unique identifier of the trip
    * @param title : title of the trip
    * @param location : location of the trip
    * @param startDateAndTime : start date of the trip
    * @param endDateAndTime : end date of the trip
    * @param description : description of the trip
    * @param path : path followed by the user
    * @param participants : friends who are part of the trip
 */
data class Itinerary(
    val id: String,
    val title: String,
    val username: String,
    val location: Location,
    val flameCount : Long,
    val startDateAndTime: String,
    val endDateAndTime: String,
    val pinnedPlaces : List<Pin>, // For now implemented as a list of Pins, Pin.kt defined in model/location
    val description: String
    // val route : Route TODO : need to add the path followed by the user as a class Path
    // val participants : List<User> TODO : need to add the friends who are part of the trip as a class Friend
)