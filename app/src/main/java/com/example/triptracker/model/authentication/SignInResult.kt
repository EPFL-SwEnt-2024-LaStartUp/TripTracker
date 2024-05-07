package com.example.triptracker.model.authentication

import com.example.triptracker.model.profile.UserProfile

/** Data class that captures user information for logged in users retrieved from LoginRepository */
data class SignInResult(val userProfile: UserProfile)
