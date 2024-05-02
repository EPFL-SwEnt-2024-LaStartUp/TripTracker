package com.example.triptracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.UserProfileRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the UserProfile class This class is responsible for handling the data operations
 * for the UserProfile class
 */
class UserProfileViewModel(
    private val userProfileRepository: UserProfileRepository = UserProfileRepository()
) : ViewModel() {

  private var _userProfileList = MutableLiveData<List<UserProfile>>()
  private val userProfileList: LiveData<List<UserProfile>> = _userProfileList

  /**
   * Fetches all user profiles from the repository and stores them in the userProfileList LiveData
   * could be used to display all user profiles in the UI not used in the current implementation
   */
  fun fetchAllUserProfiles(callback: (List<UserProfile>) -> Unit) {
    viewModelScope.launch { userProfileRepository.getAllUserProfiles() { callback(it) } }
  }

  /** This function returns the list of user's profiles. */
  fun getUserProfileList(): List<UserProfile> {
    return userProfileList.value ?: emptyList()
  }

  fun getUserProfile(email: String, callback: (UserProfile?) -> Unit) {
    viewModelScope.launch {
      userProfileRepository.getUserProfileByEmail(email) { userProfile -> callback(userProfile) }
    }
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
