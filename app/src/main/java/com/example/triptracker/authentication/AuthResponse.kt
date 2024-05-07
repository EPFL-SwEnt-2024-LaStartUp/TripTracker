package com.example.triptracker.authentication

/** Sealed class that represents the result of an authentication operation. */
sealed class AuthResponse<T> {

  data class Success<T>(val data: T? = null) : AuthResponse<T>()

  data class Error<T>(val errorMessage: String) : AuthResponse<T>()

  data class Loading<T>(val data: T? = null) : AuthResponse<T>()
}
