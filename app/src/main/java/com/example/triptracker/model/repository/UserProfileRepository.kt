package com.example.triptracker.model.repository

import android.util.Log
import com.example.triptracker.model.profile.UserProfile
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Date

/**
 * Repository for the UserProfile class This class is responsible for handling the data operations
 * for the UserProfile class It interacts with the Firebase Firestore to save, update, delete and
 * retrieve the user's profiles data
 */
open class UserProfileRepository(private val db: FirebaseFirestore = Firebase.firestore) {

  // Initialise the Firebase Firestore
  // val db = Firebase.firestore

  // Reference to the collection of user's profiles
  private val userProfileDb = db.collection("user_profiles")

  // List of user's profiles
  private var _userProfileList = mutableListOf<UserProfile>()

  // A tag for logging
  private val TAG = "FirebaseConnection - UserProfileRepository"

  /**
   * This function returns a unique identifier.
   *
   * @return Unique identifier for the user's profile
   */
  fun getUID(): String {
    return userProfileDb.document().id // check this
  }

  /**
   * This function returns all the user's profiles.
   *
   * @return List of all user's profiles
   */
  open fun getAllUserProfiles(): List<UserProfile> {
    userProfileDb
        .get()
        .addOnSuccessListener { result -> userProfileList(result) }
        .addOnFailureListener { e -> Log.e(TAG, "Error getting all user's profiles", e) }
    Log.d("UserProfileRepository", _userProfileList.toString())
    return _userProfileList
  }

  /**
   * This function returns the user profile corresponding to the mail
   *
   * @param mail : mail of the user profile to return
   * @return user profile corresponding to the mail
   */
  fun getUserProfile(mail: String): UserProfile? {
    var userProfile: UserProfile? = null
    userProfileDb
        .document(mail)
        .get()
        .addOnSuccessListener { document ->
          if (document != null) {
            userProfile =
                UserProfile(
                    document.id,
                    document.data?.get("name") as String,
                    document.data?.get("surname") as String,
                    document.data?.get("birthdate") as Date,
                    document.data?.get("pseudo") as String,
                    document.data?.get("profileImageUrl") as String)
          } else {
            Log.d(TAG, "No such document")
          }
        }
        .addOnFailureListener { e -> Log.e(TAG, "Error getting user profile", e) }
    return userProfile
  }

  /**
   * This function converts the QuerySnapshot to a list of user's profiles.
   *
   * @param taskSnapshot : QuerySnapshot to convert to a list of user's profiles
   * @return List of user's profiles
   */
  private fun userProfileList(taskSnapshot: QuerySnapshot) {
    for (document in taskSnapshot) {
      val userProfile =
          UserProfile(
              document.id,
              document.data["name"] as String,
              document.data["surname"] as String,
              document.data["birthdate"] as Date,
              document.data["pseudo"] as String,
              document.data["profileImageUrl"] as String)

      _userProfileList.add(userProfile)
    }
  }

  /**
   * This function updates the user's profile passed as a parameter.
   *
   * @param userProfile : UserProfile to update
   */
  fun updateUserProfile(userProfile: UserProfile) {
    userProfileDb
        .document(userProfile.mail)
        .set(userProfile)
        .addOnSuccessListener { Log.d(TAG, "User profile updated successfully") }
        .addOnFailureListener { e -> Log.e(TAG, "Error updating user profile", e) }
  }

  /**
   * This function adds a new user's profile to the database.
   *
   * @param userProfile : UserProfile to add
   */
  fun addNewUserProfile(userProfile: UserProfile) {
    userProfileDb
        .document(userProfile.mail)
        .set(userProfile)
        .addOnSuccessListener { Log.d(TAG, "User profile added successfully") }
        .addOnFailureListener { e -> Log.e(TAG, "Error adding new user profile", e) }
  }

  /**
   * This function removes the user's profile with the specified id.
   *
   * @param id : Unique identifier of the user's profile to remove
   */
  fun removeUserProfile(id: String) {
    userProfileDb
        .document(id)
        .delete()
        .addOnSuccessListener { Log.d(TAG, "User profile removed successfully") }
        .addOnFailureListener { e -> Log.e(TAG, "Error removing user profile", e) }
  }
}
