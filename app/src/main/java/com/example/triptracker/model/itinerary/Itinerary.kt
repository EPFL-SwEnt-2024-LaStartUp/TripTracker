package com.example.triptracker.model.itinerary

import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.google.android.gms.maps.model.LatLng

/**
 * This class represents the Itinerary object which contains the details of the trip
 *
 * @param id : unique identifier of the trip
 * @param title : title of the trip
 * @param userMail : email of the user who created the trip
 * @param location : location of the trip
 * @param flameCount : number of flames of the trip
 * @param saves : number of times the trip has been saved by users
 * @param clicks : number of times the trip has been previewed by users
 * @param numStarts : number of times the trip has been started by users
 * @param startDateAndTime : start date of the trip
 * @param endDateAndTime : end date of the trip
 * @param pinnedPlaces : list of pinned places in the trip
 * @param description : description of the trip (default value is empty)
 * @param route : route taken by the user.
 */
data class Itinerary(
    val id: String,
    val title: String,
    val userMail: String,
    val location: Location,
    val flameCount: Long,
    val saves: Long, // the number of times the trip has been saved by users
    val clicks: Long, // the number of times the trip has been clicked on by users
    val numStarts: Long, // the number of times the trip has been started by users
    val startDateAndTime: String,
    val endDateAndTime: String,
    val pinnedPlaces:
        List<Pin>, // For now implemented as a list of Pins, Pin.kt defined in model/location
    val description: String = "",
    val route: List<LatLng>
)
