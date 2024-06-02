package com.example.triptracker.authentication

/** Sealed class that represents the result of an authentication operation. */
sealed class AuthResponse<T> {

  /* Data class for successful authentication */
  data class Success<T>(val data: T? = null) : AuthResponse<T>()

  /* Data class for failed authentication */
  data class Error<T>(val errorMessage: String) : AuthResponse<T>()

  /* Data class for loading authentication */
  data class Loading<T>(val data: T? = null) : AuthResponse<T>()
}
