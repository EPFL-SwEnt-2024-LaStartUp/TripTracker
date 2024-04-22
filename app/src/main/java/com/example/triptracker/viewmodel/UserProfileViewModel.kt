package com.example.triptracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.profile.UserProfileList
import com.example.triptracker.model.repository.UserProfileRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the UserProfile class This class is responsible for handling the data operations
 * for the UserProfile class
 */
class UserProfileViewModel(private val userProfileRepository: UserProfileRepository) : ViewModel() {
  private var userProfileInstance = UserProfileList(listOf())
  private var _userProfileList = MutableLiveData<List<UserProfile>>()
  val userProfileList: LiveData<List<UserProfile>> = _userProfileList

  // Search query LiveData
  private val _searchQuery = MutableLiveData<String>("")
  val searchQuery: LiveData<String>
    get() = _searchQuery

  init {
    viewModelScope.launch { fetchUserProfiles() }
  }

  /** This function gets all the user's profiles from the database. */
  fun fetchUserProfiles() {
    userProfileInstance.setUserProfileList(userProfileRepository.getAllUserProfiles())
    _userProfileList.value = userProfileInstance.getAllUserProfiles()
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
    val updatedProfile =
        UserProfile(
            userProfile.mail,
            userProfile.name,
            userProfile.surname,
            userProfile.birthdate,
            userProfile.pseudo,
            userProfile.profileImageUrl,
            userProfile.followers + follower,
            userProfile.following)
    userProfileRepository.updateUserProfile(updatedProfile)
    val updatedFollowerProfile =
        UserProfile(
            follower.mail,
            follower.name,
            follower.surname,
            follower.birthdate,
            follower.pseudo,
            follower.profileImageUrl,
            follower.followers,
            follower.following + userProfile)
    userProfileRepository.updateUserProfile(updatedFollowerProfile)
  }

  /**
   * This function adds new following to the user profile in the database.
   *
   * @param userProfile : user profile to update
   * @param following : following to add
   */
  fun addFollowingInDb(userProfile: UserProfile, following: UserProfile) {
    val updatedProfile =
        UserProfile(
            userProfile.mail,
            userProfile.name,
            userProfile.surname,
            userProfile.birthdate,
            userProfile.pseudo,
            userProfile.profileImageUrl,
            userProfile.followers,
            userProfile.following + following)
    userProfileRepository.updateUserProfile(updatedProfile)
    val updatedFollowingProfile =
        UserProfile(
            following.mail,
            following.name,
            following.surname,
            following.birthdate,
            following.pseudo,
            following.profileImageUrl,
            following.followers + userProfile,
            following.following)
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
        UserProfile(
            userProfile.mail,
            userProfile.name,
            userProfile.surname,
            userProfile.birthdate,
            userProfile.pseudo,
            userProfile.profileImageUrl,
            userProfile.followers.filter { it.mail != follower.mail },
            userProfile.following)
    userProfileRepository.updateUserProfile(updatedProfile)
    val updatedFollowerProfile =
        UserProfile(
            follower.mail,
            follower.name,
            follower.surname,
            follower.birthdate,
            follower.pseudo,
            follower.profileImageUrl,
            follower.followers,
            follower.following.filter { it.mail != userProfile.mail })
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
        UserProfile(
            userProfile.mail,
            userProfile.name,
            userProfile.surname,
            userProfile.birthdate,
            userProfile.pseudo,
            userProfile.profileImageUrl,
            userProfile.followers,
            userProfile.following.filter { it.mail != following.mail })
    userProfileRepository.updateUserProfile(updatedProfile)
    val updatedFollowingProfile =
        UserProfile(
            following.mail,
            following.name,
            following.surname,
            following.birthdate,
            following.pseudo,
            following.profileImageUrl,
            following.followers.filter { it.mail != userProfile.mail },
            following.following)
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

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }
}
