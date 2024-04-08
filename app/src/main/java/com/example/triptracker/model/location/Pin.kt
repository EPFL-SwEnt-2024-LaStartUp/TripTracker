package com.example.triptracker.model.location

/**
 * Data class for Pin
 *
 * @param latitude Latitude of pin
 * @param longitude Longitude of pin
 * @param name Name of pin
 * @param description Description of pin
 * @param image_url Image url of pin
 */
data class Pin(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val description: String,
    val image_url:
        String // will use this to store the image url and fetch the image from the url on the
    // Firestore
) {
  override fun toString(): String {
    return "Pin(latitude=$latitude, longitude=$longitude, name='$name', description='$description', image=$image_url)"
  }

  fun getPinName(): String {
    return name
  }
}
