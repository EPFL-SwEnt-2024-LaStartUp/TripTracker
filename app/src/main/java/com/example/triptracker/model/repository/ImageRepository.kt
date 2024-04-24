package com.example.triptracker.model.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.util.UUID
import kotlinx.coroutines.tasks.await

class ImageRepository {

  private val storage = Firebase.storage

  private val PICTURE_FOLDER = "pictures"
  private val PIN_PICTURES = "pin"

  suspend fun addImageToFirebaseStorage(imageUri: Uri): Response<Uri> {
    return try {
      val downloadUrl =
          storage.reference
              .child(PICTURE_FOLDER)
              .child(PIN_PICTURES)
              .child(UUID.randomUUID().toString())
              .putFile(imageUri)
              .await()
              .storage
              .downloadUrl
              .await()
      Log.d("DOWNLOAD URL", downloadUrl.toString())
      Response.Success(downloadUrl)
    } catch (e: Exception) {
      Response.Failure(e)
    }
  }
}
