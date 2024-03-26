package com.example.triptracker.model.itinerary

import com.example.triptracker.model.location.Location

/**
    * This class represents the Itinerary object which contains the details of the trip
    * @param id : unique identifier of the trip
    * @param title : title of the trip
    * @param location : location of the trip
    * @param startDate : start date of the trip
    * @param endDate : end date of the trip
    * @param description : description of the trip
    * @param path : path followed by the user
    * @param participants : friends who are part of the trip
 */
data class Itinerary(
    val id: String,
    val title: String,
    val location: Location,
    val startDate: String,
    val endDate: String,
    val description: String
    // val path : Path TODO : need to add the path followed by the user as a class Path
    // val participants : List<User> TODO : need to add the friends who are part of the trip as a class Friend
)