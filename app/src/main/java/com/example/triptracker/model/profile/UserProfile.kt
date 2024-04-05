package com.example.triptracker.model.profile

import java.util.Date

/**
 * This data class represents a user's profile information.
 *
 * @property uid : unique identifier of the user. (Defaults: empty string)
 * @property name : first name of the user. (Defaults: empty string)
 * @property surname : last name of the user. (Defaults: empty string)
 * @property birthdate : user's date of birth. (Defaults: current date)
 * @property pseudo : nickname or pseudonym chosen by the user. (Defaults: empty string)
 * @property profileImageUrl : optional URL to the user's profile image. (Defaults: null)
 */
data class UserProfile(
    var id: String,
    var name: String = "",
    var surname: String = "",
    var birthdate: Date = Date(),
    var pseudo: String = "",
    var profileImageUrl: String? = null
)
