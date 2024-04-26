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
   * Function that add the follower to the user profile
   *
   * @param userProfile : the user profile to which we add a follower
   * @param follower : the user profile of the follower to add
   */
  fun addFollower(userProfile: UserProfile, follower: UserProfile) {
    val updatedUserProfile = userProfile.copy(followers = userProfile.followers + follower.mail)
    val updatedFollower = follower.copy(following = follower.following + userProfile.mail)
    userProfileRepository.updateUserProfile(updatedUserProfile)
    userProfileRepository.updateUserProfile(updatedFollower)
  }

  /**
   * Function that remove the follower from the user profile
   *
   * @param userProfile : the user profile from which we remove a follower
   * @param follower : the user profile of the follower to remove
   */
  fun removeFollower(userProfile: UserProfile, follower: UserProfile) {
    val updatedUserProfile = userProfile.copy(followers = userProfile.followers - follower.mail)
    val updatedFollower = follower.copy(following = follower.following - userProfile.mail)
    userProfileRepository.updateUserProfile(userProfile)
    userProfileRepository.updateUserProfile(follower)
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
