package com.example.triptracker.model.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.util.UUID
import kotlinx.coroutines.tasks.await

/** Repository for interacting with Firebase Storage to upload and download images. */
class ImageRepository {

  private val storage = Firebase.storage

  /** The folder where the pictures are stored */
  private val PICTURE_FOLDER = "pictures"
  private val PIN_PICTURES = "pin"
  private val PROFILE_PICTURES = "profile"

  /** Get the path to the pin pictures folder */
  val pinPictures: String
    get() = PIN_PICTURES

  /** Get the path to the profile pictures folder */
  val profilePictures: String
    get() = PROFILE_PICTURES

  suspend fun addImageToFirebaseStorage(folder: String, imageUri: Uri): Response<Uri> {
    return try {
      val downloadUrl =
          storage.reference
              .child(PICTURE_FOLDER)
              .child(folder)
              .child(UUID.randomUUID().toString())
              .putFile(imageUri)
              .await()
              .storage
              .downloadUrl
              .await()
      Log.d("DOWNLOAD URL", downloadUrl.toString())
      Response.Success(downloadUrl)
    } catch (e: Exception) {
      Log.d("DOWNLOAD URL", e.toString())
      Response.Failure(e)
    }
  }
}
