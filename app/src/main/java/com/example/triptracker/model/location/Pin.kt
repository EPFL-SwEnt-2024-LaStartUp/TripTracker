package com.example.triptracker.model.location

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
}
