package com.example.triptracker.model.authentication

/** Data class that captures user information for logged in users retrieved from LoginRepository */
data class SignInResult(val name: String?, val email: String?, val imageUrl: String?)
