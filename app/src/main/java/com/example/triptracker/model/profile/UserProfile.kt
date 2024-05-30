package com.example.triptracker.model.profile

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

/**
 * This data class represents a user's profile information.
 *
 * @property mail : unique identifier of the user.
 * @property name : first name of the user. (Defaults: empty string)
 * @property surname : last name of the user. (Defaults: empty string)
 * @property birthdate : user's date of birth.
 * @property username : nickname or pseudonym chosen by the user.
 * @property profileImageUrl : optional URL to the user's profile image. (Defaults: null)
 * @property followers : list of user's followers' email. (Defaults: empty list)
 * @property following : list of user's following's email. (Defaults: empty list)
 * @property profilePrivacy : privacy of the user's profile 0 = public and 1 = private
 * @property itineraryPrivacy : privacy of the user's itineraries 0 = public, 1 = friends, 2 =
 *   private
 * @property interests : list of user's interests. (Defaults: empty list)
 * @property travelStyle : list of user's travel style. (Defaults: empty list)
 * @property languages : list of user's spoken languages. (Defaults: empty list)
 */
data class UserProfile(
    val mail: String,
    val name: String = "",
    val surname: String = "",
    val birthdate: String = "",
    val username: String = "",
    val profileImageUrl: String? = null,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val favoritesPaths: List<String> = emptyList(),
    val profilePrivacy: Int = 0,
    val itineraryPrivacy: Int = 0,
    val interests: List<String> = emptyList(),
    val travelStyle: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val flowerMode: Int = 0,
)

/** This data class represents a mutable user's profile information. */
data class MutableUserProfile(
    var userProfile: MutableState<UserProfile> = mutableStateOf(EMPTY_PROFILE)
)

/**
 * CompositionLocal for providing the user's profile information. This will be global information
 * that can be accessed by any composable function.
 */
@SuppressLint("CompositionLocalNaming")
var AmbientUserProfile = compositionLocalOf { MutableUserProfile() }

/** Composable function to provide the user's profile information. */
@Composable
fun ProvideUserProfile(userProfileState: MutableUserProfile, content: @Composable () -> Unit) {
  CompositionLocalProvider(AmbientUserProfile provides userProfileState) { content() }
}

/** Empty profile object to be used as a default value. */
val EMPTY_PROFILE =
    UserProfile(
        "surname.name@gmail.com",
        "Name",
        "Surname",
        "00/00/0000",
        "Username",
        "your-profile-pic.url",
        emptyList(),
        emptyList(),
        emptyList(),
        0,
        0)
