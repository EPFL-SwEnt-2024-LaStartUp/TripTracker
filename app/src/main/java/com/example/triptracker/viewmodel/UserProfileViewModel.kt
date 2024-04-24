package com.example.triptracker.viewmodel

import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.UserProfileRepository

/**
 * ViewModel for the UserProfile class This class is responsible for handling the data operations
 * for the UserProfile class
 */
class UserProfileViewModel(
    private val userProfileRepository: UserProfileRepository = UserProfileRepository()
) {

  private var userProfileList: List<UserProfile> = mutableListOf()

  /** This function returns the list of user's profiles. */
  fun getUserProfileList(): List<UserProfile> {
    getAllUserProfilesFromDb()
    return userProfileList
  }

  /** This function gets all the user's profiles from the database. */
  fun getAllUserProfilesFromDb() {
    userProfileList = userProfileRepository.getAllUserProfiles()
  }

  /**
   * This function returns the user profile corresponding to the mail.
   *
   * @param mail : mail of the user profile to return
   * @return user profile corresponding to the mail
   */
  fun getUserProfileFromDb(mail: String): UserProfile? {
    return userProfileRepository.getUserProfile(mail)
  }

  /**
   * This function adds a new user profile to the database.
   *
   * @param userProfile : user profile to add
   */
  fun addNewUserProfileToDb(userProfile: UserProfile) {
    userProfileRepository.addNewUserProfile(userProfile)
  }

  /**
   * This function updates a user profile from the database.
   *
   * @param userProfile : user profile to update
   */
  fun updateUserProfileInDb(userProfile: UserProfile) {
    userProfileRepository.updateUserProfile(userProfile)
  }

  /**
   * This function adds new followers to the user profile in the database.
   *
   * @param userProfile : user profile to update
   * @param follower : follower to add
   */
  fun addFollowersInDb(userProfile: UserProfile, follower: UserProfile) {
    val updatedProfile = userProfile.copy(followers = userProfile.followers + follower.mail)
    userProfileRepository.updateUserProfile(updatedProfile)

    val updatedFollowerProfile = follower.copy(following = follower.following + userProfile.mail)
    userProfileRepository.updateUserProfile(updatedFollowerProfile)
  }

  /**
   * This function adds new following to the user profile in the database.
   *
   * @param userProfile : user profile to update
   * @param following : following to add
   */
  fun addFollowingInDb(userProfile: UserProfile, following: UserProfile) {
    val updatedProfile = userProfile.copy(following = userProfile.following + following.mail)
    userProfileRepository.updateUserProfile(updatedProfile)

    val updatedFollowingProfile = following.copy(followers = following.followers + userProfile.mail)
    userProfileRepository.updateUserProfile(updatedFollowingProfile)
  }

  /**
   * This function removes a follower from the user profile in the database.
   *
   * @param userProfile : user profile to update
   * @param follower : follower to remove
   */
  fun removeFollowerInDb(userProfile: UserProfile, follower: UserProfile) {
    val updatedProfile =
        userProfile.copy(followers = userProfile.followers.filter { it != follower.mail })
    userProfileRepository.updateUserProfile(updatedProfile)

    val updatedFollowerProfile =
        follower.copy(following = follower.following.filter { it != userProfile.mail })
    userProfileRepository.updateUserProfile(updatedFollowerProfile)
  }

  /**
   * This function removes a following from the user profile in the database.
   *
   * @param userProfile : user profile to update
   * @param following : following to remove
   */
  fun removeFollowingInDb(userProfile: UserProfile, following: UserProfile) {
    val updatedProfile =
        userProfile.copy(following = userProfile.following.filter { it != following.mail })
    userProfileRepository.updateUserProfile(updatedProfile)

    val updatedFollowingProfile =
        following.copy(followers = following.followers.filter { it != userProfile.mail })
    userProfileRepository.updateUserProfile(updatedFollowingProfile)
  }

  /**
   * This function removes the user profile matching the id from the database.
   *
   * @param mail : mail of the user profile to remove
   */
  fun removeUserProfileInDb(mail: String) {
    userProfileRepository.removeUserProfile(mail)
  }
}
