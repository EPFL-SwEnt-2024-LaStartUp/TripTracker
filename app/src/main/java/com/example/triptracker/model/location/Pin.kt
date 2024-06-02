package com.example.triptracker.model.location

/**
 * Data class for Pin
 *
 * @param latitude Latitude of pin
 * @param longitude Longitude of pin
 * @param name Name of pin
 * @param description Description of pin
 * @param imageUrl Image url of pin
 */
data class Pin(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val description: String,
    val imageUrl: List<String>
) {
  /**
   * Function that returns the attribute of the pin as a string
   *
   * @return the pin attributes as a string
   */
  override fun toString(): String {
    return "Pin(latitude=$latitude, longitude=$longitude, name='$name', description='$description', image=$imageUrl)"
  }
}
