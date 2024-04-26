package com.example.triptracker.model.profile

/**
 * This Data class represents a list of user's profiles.
 *
 * @property userProfileList : List of user's profiles
 */
data class UserProfileList(private var userProfileList: List<UserProfile>) {
  /**
   * This function returns all the user's profiles.
   *
   * @return List of all user's profiles
   */
  fun getAllUserProfiles(): List<UserProfile> {
    return userProfileList
  }

  /**
   * This function sets the user's profiles list.
   *
   * @param userProfileList : List of user's profiles
   */
  fun setUserProfileList(userProfileList: List<UserProfile>) {
    this.userProfileList = userProfileList
  }

  /**
   * This function adds a user's profile to the list.
   *
   * @param userProfile : user's profile to add
   */
  fun addUserProfile(userProfile: UserProfile) {
    userProfileList = userProfileList + userProfile
  }

  /**
   * This function returns all the user's profiles that match the query.
   *
   * @param task : String to search for in the user's profiles
   * @return List of user's profiles that match the task
   */
  fun getFilteredUserProfile(task: String): List<UserProfile> {
    return userProfileList.filter {
      it.name.contains(task, ignoreCase = true) ||
          it.surname.contains(task, ignoreCase = true) ||
          it.username.contains(task, ignoreCase = true)
    }
  }

  /**
   * This function returns the user's profile that matches the Uid.
   *
   * @param Uid : unique identifier of the user's profile
   * @return User's profile that matches the Uid
   */
  fun getUserProfile(taskUid: String): UserProfile {
    return userProfileList.find { it.mail == taskUid }!!
  }

  /**
   * This function adds a user's profile to the list.
   *
   * @return List of user's profiles
   */
  fun size(): Int {
    return userProfileList.size
  }
}
