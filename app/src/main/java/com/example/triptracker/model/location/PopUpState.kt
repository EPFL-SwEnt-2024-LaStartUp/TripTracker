package com.example.triptracker.model.location

/**
 * Enum class that represents the state of the popup
 */
enum class PopUpState {
  DISPLAY_ITINERARY, // intermediary display with details
  DISPLAY_PIN, // firstDisplay it displays just the main overview of the path
  PATH_OVERLAY // display once the path is selected
}
