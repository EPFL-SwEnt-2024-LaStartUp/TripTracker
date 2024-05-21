package com.example.triptracker.model.repository

import android.util.Log
import com.example.triptracker.model.profile.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore

/**
 * Repository for the UserProfile class This class is responsible for handling the data operations
 * for the UserProfile class It interacts with the Firebase Firestore to save, update, delete and
 * retrieve the user's profiles data
 */
open class UserProfileRepository {

  // Initialise the Firebase Firestore
  private val db = Firebase.firestore

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
   * This function returns all the user's profiles.
   *
   * @return List of all user's profiles
   */
  open fun getAllUserProfiles(callback: (List<UserProfile>) -> Unit) {
    userProfileDb
        .get(Source.SERVER)
        .addOnSuccessListener { result ->
          userProfileList(result)
          callback(_userProfileList)
        }
        .addOnFailureListener { e -> Log.e(TAG, "Error getting all user's profiles", e) }
  }

  /**
   * This function returns the user profile corresponding to the mail
   *
   * @param email : mail of the user profile to return
   * @param onResult : callback function to return the user profile
   * @return user profile corresponding to the mail
   */
  fun getUserProfileByEmail(email: String, onResult: (UserProfile?) -> Unit) {
    userProfileDb
        .document(email)
        .get()
        .addOnSuccessListener { document ->
          if (document.exists()) {
            val userProfile = userProfile(document)
            Log.d(TAG, "User profile found for email: $email")
            onResult(userProfile)
          } else {
            Log.d(TAG, "No user profile found for email: $email")
            onResult(null)
          }
        }
        .addOnFailureListener { e ->
          Log.e(TAG, "Error getting user profile for email: $email", e)
          onResult(null)
        }
  }

  private fun userProfile(document: DocumentSnapshot): UserProfile {
    val name =
        document.data?.get("name") as? String ?: throw IllegalStateException("Name is missing")
    val surname =
        document.data?.get("surname") as? String
            ?: throw IllegalStateException("Surname is missing")
    val username =
        document.data?.get("username") as? String
            ?: throw IllegalStateException("Username is missing")
    val profileImageUrl =
        document.data?.get("profileImageUrl") as? String
            ?: throw IllegalStateException("Profile image URL is missing")
    val birthdate =
        document.data?.get("birthdate") as? String
            ?: throw IllegalStateException("Birthdate is missing")
    val follower =
        document.data?.get("followers") as? List<String>
            ?: throw IllegalStateException("Followers is missing")
    val following =
        document.data?.get("following") as? List<String>
            ?: throw IllegalStateException("Following is missing")
    // if favoritesPaths doesn't exist create the fiald in the database
    val favoritesPaths =
        document.data?.get("favoritesPaths") as? List<String> ?: createFavoritesPaths(document.id)
    val profilePrivacy = document.getLong("profilePrivacy") ?: createProfilePrivacy(document.id)
    val itineraryPrivacy =
        document.getLong("itineraryPrivacy") ?: createItineraryPrivacy(document.id)
    val interest =
        document.data?.get("interests") as? List<String>
            ?: throw IllegalStateException("Interests is missing")
    val travelStyle =
        document.data?.get("travelStyle") as? List<String>
            ?: throw IllegalStateException("Travel style is missing")
    val languages =
        document.data?.get("languages") as? List<String>
            ?: throw IllegalStateException("Languages is missing")

    return UserProfile(
        document.id,
        name,
        surname,
        birthdate,
        username,
        profileImageUrl,
        follower,
        following,
        favoritesPaths,
        profilePrivacy.toInt(),
        itineraryPrivacy.toInt(),
        interest,
        travelStyle,
        languages)
  }

  private fun createFavoritesPaths(id: String): List<String> {
    val favoritesPaths = mutableListOf<String>()
    userProfileDb
        .document(id)
        .update("favoritesPaths", favoritesPaths)
        .addOnSuccessListener { Log.d(TAG, "FavoritesPaths created successfully") }
        .addOnFailureListener { e -> Log.e(TAG, "Error creating FavoritesPaths", e) }
    return favoritesPaths
  }

  private fun createProfilePrivacy(id: String): Int {
    val profilePrivacy = 0
    userProfileDb
        .document(id)
        .update("profilePrivacy", profilePrivacy)
        .addOnSuccessListener { Log.d(TAG, "Profile Privacy created successfully $id") }
        .addOnFailureListener { e -> Log.e(TAG, "Error creating Profile Privacy", e) }
    return profilePrivacy
  }

  private fun createItineraryPrivacy(id: String): Int {
    val itineraryPrivacy = 0
    userProfileDb
        .document(id)
        .update("itineraryPrivacy", itineraryPrivacy)
        .addOnSuccessListener { Log.d(TAG, "Itinerary Privacy created successfully") }
        .addOnFailureListener { e -> Log.e(TAG, "Error creating Itinerary Privacy", e) }
    return itineraryPrivacy
  }

  /**
   * This function converts the QuerySnapshot to a list of user's profiles.
   *
   * @param taskSnapshot : QuerySnapshot to convert to a list of user's profiles
   * @return List of user's profiles
   */
  private fun userProfileList(taskSnapshot: QuerySnapshot) {
    for (document in taskSnapshot) {
      val name = document.data["name"] as? String ?: throw IllegalStateException("Name is missing")
      val surname =
          document.data["surname"] as? String ?: throw IllegalStateException("Surname is missing")
      val username =
          document.data["username"] as? String ?: throw IllegalStateException("Username is missing")
      val profileImageUrl =
          document.data["profileImageUrl"] as? String
              ?: throw IllegalStateException("Profile image URL is missing")

      val birthdate =
          document.data["birthdate"] as? String
              ?: throw IllegalStateException("Birthdate is missing")
      val followers =
          document.data["followers"] as? List<String>
              ?: throw IllegalStateException("Followers is missing")
      val following =
          document.data["following"] as? List<String>
              ?: throw IllegalStateException("Following is missing")
      val favoritesPaths =
          document.data["favoritesPaths"] as? List<String> ?: createFavoritesPaths(document.id)
      val profilePrivacy = document.getLong("profilePrivacy") ?: createProfilePrivacy(document.id)
      val itineraryPrivacy =
          document.getLong("itineraryPrivacy") ?: createItineraryPrivacy(document.id)
      val interest =
          document.data["interests"] as? List<String>
              ?: throw IllegalStateException("Interests is missing")
      val travelStyle =
          document.data["travelStyle"] as? List<String>
              ?: throw IllegalStateException("Travel style is missing")
      val languages =
          document.data["languages"] as? List<String>
              ?: throw IllegalStateException("Languages is missing")

      val userProfile =
          UserProfile(
              document.id,
              name,
              surname,
              birthdate,
              username,
              profileImageUrl,
              followers,
              following,
              favoritesPaths,
              profilePrivacy.toInt(),
              itineraryPrivacy.toInt(),
              interest,
              travelStyle,
              languages)
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
