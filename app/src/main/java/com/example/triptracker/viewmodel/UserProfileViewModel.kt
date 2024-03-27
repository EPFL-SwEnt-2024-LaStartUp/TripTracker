package com.example.triptracker.viewmodel

import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.profile.UserProfileList
import com.example.triptracker.model.repository.UserProfileRepository

/**
 * ViewModel for the UserProfile class
 * This class is responsible for handling the data operations for the UserProfile class
 */
class UserProfileViewModel {
    val userProfileRepository = UserProfileRepository()
    val userProfileList = UserProfileList(mutableListOf())

    /**
     * This function gets all the user's profiles from the database.
     */
    fun getAllUserProfilesFromDb() {
        userProfileList.userProfileList = userProfileRepository.getAllUserProfiles()
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
     * This function removes the user profile matching the id from the database.
     *
     * @param id : id of the user profile to remove
     */
    fun removeUserProfileInDb(id: String) {
        userProfileRepository.removeUserProfile(id)
    }
}