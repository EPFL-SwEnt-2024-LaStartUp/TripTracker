package com.example.triptracker.authentication

sealed class AuthResponse<T> {

    data class Success<T>(val data: T) : AuthResponse<T>()

    data class Error<T>(val errorMessage: String) : AuthResponse<T>()

    data class Loading<T>(val data: T? = null) : AuthResponse<T>()
}
