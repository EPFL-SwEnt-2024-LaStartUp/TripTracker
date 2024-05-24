package com.example.triptracker.model.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.util.UUID
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

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

        val bitmap = BitmapFactory.decodeStream(imageUri.toFile().inputStream())

        // Resize the Bitmap
        val resizedBitmap = resizeBitmap(bitmap)

        // Encode the resized Bitmap to a ByteArray
        val byteArray = ByteArrayOutputStream().apply {
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, this)
        }.toByteArray()
        
        
      val downloadUrl =
          storage.reference
              .child(PICTURE_FOLDER)
              .child(folder)
              .child(UUID.randomUUID().toString())
              .putBytes(byteArray)
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

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int = 800, maxHeight: Int = 800): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val aspectRatio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int

        if (width > height) {
            newWidth = maxWidth
            newHeight = (newWidth / aspectRatio).toInt()
        } else {
            newHeight = maxHeight
            newWidth = (newHeight * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
