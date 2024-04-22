package com.example.triptracker.model.repository

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class ImageRepository {

  val storage = Firebase.storage

  val PICTURE_FOLDER = "pictures"

  suspend fun addImageToFirebaseStorage(imageUri: Uri): Response<Uri> {
    return try {
      val downloadUrl =
          storage.reference
              .child(PICTURE_FOLDER)
              .child("test_id")
              .putFile(imageUri)
              .await()
              .storage
              .downloadUrl
              .await()
      Response.Success(downloadUrl)
    } catch (e: Exception) {
      Response.Failure(e)
    }
  }

  //    suspend fun addImageUrlToFirestore(downloadUrl: Uri): Response<Boolean> {
  //        return try {
  //            db.collection(IMAGES).document(UID).set(mapOf(
  //                URL to downloadUrl,
  //                CREATED_AT to FieldValue.serverTimestamp()
  //            )).await()
  //            Success(true)
  //        } catch (e: Exception) {
  //            Failure(e)
  //        }
  //    }
  //
  //    suspend fun getImageUrlFromFirestore(): Response<String> {
  //        return try {
  //            val imageUrl = db.collection(IMAGES).document(UID).get().await().getString(URL)
  //            Success(imageUrl)
  //        } catch (e: Exception) {
  //            Failure(e)
  //        }
  //    }
}
