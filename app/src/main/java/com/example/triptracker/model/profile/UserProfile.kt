package com.example.triptracker.model.profile

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
 */
data class UserProfile(
    val mail: String,
    val name: String = "",
    val surname: String = "",
    val birthdate: String = "",
    val username: String = "",
    val profileImageUrl: String? = null,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList()
)
