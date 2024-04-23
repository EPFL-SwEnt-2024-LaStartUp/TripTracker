package com.example.triptracker.model.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.util.UUID
import kotlinx.coroutines.tasks.await

class ImageRepository {

  val storage = Firebase.storage

  val PICTURE_FOLDER = "pictures"

  suspend fun addImageToFirebaseStorage(imageUri: Uri): Response<Uri> {
    return try {
      val downloadUrl =
          storage.reference
              .child(PICTURE_FOLDER)
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
