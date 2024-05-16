package com.example.triptracker.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.profile.UserProfileList
import com.example.triptracker.model.repository.ImageRepository
import com.example.triptracker.model.repository.Response
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.view.Navigation
import kotlinx.coroutines.launch

/**
 * ViewModel for the UserProfile class This class is responsible for handling the data operations
 * for the UserProfile class
 */
class UserProfileViewModel(
    private val userProfileRepository: UserProfileRepository = UserProfileRepository(),
    private val imageRepository: ImageRepository = ImageRepository(),
) : ViewModel() {

  private var userProfileInstance = UserProfileList(listOf())
  private var _userProfileList = MutableLiveData<List<UserProfile>>()
  val userProfileList: LiveData<List<UserProfile>> = _userProfileList

  private val _listToFilter = MutableLiveData<List<UserProfile>>()
  private val listToFilter: LiveData<List<UserProfile>> = _listToFilter

  private val _searchQuery = MutableLiveData<String>("")
  val searchQuery: LiveData<String>
    get() = _searchQuery

  init {
    viewModelScope.launch { fetchAllUserProfiles() }
  }

  /**
   * Fetches all user profiles from the repository and stores them in the userProfileList LiveData
   * could be used to display all user profiles in the UI not used in the current implementation
   */
  private fun fetchAllUserProfiles() {
    userProfileInstance.setUserProfileList(userProfileRepository.getAllUserProfiles())
    _userProfileList.value = userProfileInstance.getAllUserProfiles()
  }

  /**
   * This function returns the list of user's profiles.
   *
   * @param callback : callback function to handle the response
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
   * Function that add following to userProfile following list
   *
   * @param userProfile : the user profile to which we add a following
   * @param following : the user profile of the following to add
   */
  fun addFollowing(userProfile: MutableUserProfile, following: UserProfile) {
    val user = userProfile.userProfile.value
    // we only add follower to userProfile's follower list if he is not already present
    if (!user.following.contains(following.mail)) {
      val updatedUserProfile = user.copy(following = user.following + following.mail)
      userProfileRepository.updateUserProfile(updatedUserProfile)
      userProfile.userProfile.value = updatedUserProfile
    }
    // we only add userProfile to follower's following list if he is not already present
    if (!following.followers.contains(user.mail)) {
      val updatedFollower = following.copy(followers = following.followers + user.mail)
      userProfileRepository.updateUserProfile(updatedFollower)
    }
  }

  /**
   * Function that add follower to userProfile followers list
   *
   * @param userProfile : the user profile to which we add a follower
   * @param follower : the user profile of the follower to add
   */
  fun addFollower(userProfile: MutableUserProfile, follower: UserProfile) {
    val user = userProfile.userProfile.value
    // we only add follower to userProfile's follower list if he is not already present
    if (!user.followers.contains(follower.mail)) {
      val updatedUserProfile = user.copy(followers = user.followers + follower.mail)
      userProfileRepository.updateUserProfile(updatedUserProfile)
      userProfile.userProfile.value = updatedUserProfile
    }
    // we only add userProfile to follower's following list if he is not already present
    if (!follower.following.contains(user.mail)) {
      val updatedFollower = follower.copy(following = follower.following + user.mail)
      userProfileRepository.updateUserProfile(updatedFollower)
    }
  }

  /**
   * Function that remove following from userProfile following list
   *
   * @param userProfile : the user profile from which we remove a following
   * @param following : the user profile of the following to remove
   */
  fun removeFollowing(userProfile: MutableUserProfile, following: UserProfile) {
    val user = userProfile.userProfile.value
    val updatedUserProfile = user.copy(following = user.following - following.mail)
    val updatedFollower = following.copy(followers = following.followers - user.mail)
    userProfileRepository.updateUserProfile(updatedUserProfile)
    userProfileRepository.updateUserProfile(updatedFollower)
    userProfile.userProfile.value = updatedUserProfile
  }

  /**
   * Function that remove follower from userProfile followers list
   *
   * @param userProfile : the user profile from which we remove a follower
   * @param follower : the user profile of the follower to remove
   */
  fun removeFollower(userProfile: MutableUserProfile, follower: UserProfile) {
    val user = userProfile.userProfile.value
    val updatedUserProfile = user.copy(followers = user.followers - follower.mail)
    val updatedFollower = follower.copy(following = follower.following - user.mail)
    userProfileRepository.updateUserProfile(updatedUserProfile)
    userProfileRepository.updateUserProfile(updatedFollower)
    userProfile.userProfile.value = updatedUserProfile
  }

  /**
   * This function removes the user profile matching the id from the database.
   *
   * @param mail : mail of the user profile to remove
   */
  fun removeUserProfileInDb(mail: String) {
    userProfileRepository.removeUserProfile(mail)
  }

  /**
   * This function sets the search query to the specified query.
   *
   * @param query : search query to set
   */
  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  /**
   * This function returns the filtered user profile list based on the search query.
   *
   * @param aware : parameter that indicates if the filtered list should contain the private profiles or no
   * @return filtered user profile list based on the search query
   */
  fun filteredUserProfileList(aware: Boolean): LiveData<List<UserProfile>> =
      _searchQuery.switchMap { query ->
        liveData {
          val filteredList =
              listToFilter.value?.filter {
                (it.username.contains(query, ignoreCase = true) ||
                    it.surname.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true)) && (it.profilePrivacy == 0 || aware)
              } ?: emptyList()
          emit(filteredList)
        }
      }

  /**
   * Function used to set the list of user profiles that we want to filter using the search query.
   *
   * @param list : list of user profiles to filter
   */
  fun setListToFilter(list: List<UserProfile>) {
    _listToFilter.value = list
  }

  /**
   * This function adds a profile picture to the user profile.
   *
   * @param imageUri : Uri of the image to add
   * @param callback : callback function to handle the response
   */
  fun addProfilePictureToStorage(imageUri: Uri, callback: (Response<Uri>) -> Unit) {
    viewModelScope.launch {
      val elem =
          imageRepository.addImageToFirebaseStorage(imageRepository.profilePictures, imageUri)
      callback(elem)
    }
  }

  /**
   * This function removes the given favorite path from the user profile.
   *
   * @param profile : user profile to update
   * @param id : id of the favorite to remove
   */
  fun removeFavorite(profile: MutableUserProfile, id: String) {
    val favorites = profile.userProfile.value.favoritesPaths.toMutableList()
    favorites.remove(id)
    profile.userProfile.value = profile.userProfile.value.copy(favoritesPaths = favorites)
    updateUserProfileInDb(profile.userProfile.value)
  }

  /**
   * This function adds the given favorite path to the user profile.
   *
   * @param profile : user profile to update
   * @param id : id of the favorite to add
   */
  fun addFavorite(profile: MutableUserProfile, id: String) {
    val favorites = profile.userProfile.value.favoritesPaths.toMutableList()
    favorites.add(id)
    profile.userProfile.value = profile.userProfile.value.copy(favoritesPaths = favorites)
    updateUserProfileInDb(profile.userProfile.value)
  }

  /**
   * This function updates the user profile in the database on save.
   *
   * @param navigation : Navigation object that manages the navigation in the application.
   * @param isCreated : Boolean indicating if the user profile is created. Navigation needs to be
   *   used after the callback else the view will be destroyed and resulting in a crash of the
   *   upload of the picture.
   * @param onLoadingChange : Function to execute when the loading status changes.
   * @param selectedPicture : Uri of the selected picture to update the user profile (can be null).
   * @param profile : User profile to update.
   * @return the updated user profile potentially with the new picture.
   */
  fun updateProfile(
      navigation: Navigation,
      isCreated: Boolean,
      onLoadingChange: () -> Unit,
      selectedPicture: Uri?,
      profile: MutableUserProfile
  ) {
    onLoadingChange()
    if (selectedPicture != null) {
      addProfilePictureToStorage(selectedPicture!!) { resp ->
        val imageUrl =
            if (resp is Response.Success) {
              resp.data!!.toString()
            } else {
              profile.userProfile.value
                  .profileImageUrl // Keep the old image if the new one could not be uploaded
            }
        val newProfile = profile.userProfile.value.copy(profileImageUrl = imageUrl)
        Log.d("Profile picture updated", newProfile.toString())
        updateUserProfileInDb(newProfile)
        profile.userProfile.value = newProfile
        if (!isCreated) {
          navigation.goBack()
        } else {
          navigation.navigateTo(navigation.getStartingDestination())
        }
        onLoadingChange()
      }
    } else {
      updateUserProfileInDb(profile.userProfile.value)
      if (!isCreated) {
        navigation.goBack()
      } else {
        navigation.navigateTo(navigation.getStartingDestination())
      }
      onLoadingChange()
    }
  }
}
