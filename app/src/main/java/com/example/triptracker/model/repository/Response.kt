package com.example.triptracker.model.repository

/**
 * Sealed class that represents the different states of a response
 *
 * @param T: Type of the data
 */
sealed class Response<out T> {
  // Success state which returns the data
  data class Success<out T>(val data: T?) : Response<T>()

  // Failure state which returns an exception
  data class Failure(val e: Exception) : Response<Nothing>()
}
