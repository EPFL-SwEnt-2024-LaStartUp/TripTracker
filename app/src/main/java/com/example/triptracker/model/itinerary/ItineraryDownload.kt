package com.example.triptracker.model.itinerary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

/**
 * This class is responsible for saving and loading the itinerary to and from the internal storage
 *
 * @param context the context of the activity
 */
class ItineraryDownload(private val context: Context) {

  private val gson = Gson()
  private val itinerarySuffix = "_itinerary"

  /**
   * Save the itinerary to the internal storage
   *
   * @param itinerary the itinerary to be saved
   */
  fun saveItineraryToInternalStorage(itinerary: Itinerary) {
    val updatedPins =
        itinerary.pinnedPlaces.map { pin ->
          val updatedImages =
              pin.image_url.mapNotNull { imageUrl -> saveImageToInternalStorage(imageUrl) }
          pin.copy(image_url = updatedImages)
        }
    val updatedItinerary = itinerary.copy(pinnedPlaces = updatedPins)
    val fileName = "${updatedItinerary.id}$itinerarySuffix.json"
    val itineraryJson = gson.toJson(updatedItinerary)

    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
      outputStream.write(itineraryJson.toByteArray())
    }
  }

  /**
   * Save the image to the internal storage
   *
   * @param imageUrl the url of the image to be saved
   */
  private fun saveImageToInternalStorage(imageUrl: String): String? {
    return try {
      val url = URL(imageUrl)
      val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
      connection.doInput = true
      connection.connect()
      val inputStream: InputStream = connection.inputStream
      val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

      val fileName = "image_${UUID.randomUUID()}.jpg"
      val file = File(context.filesDir, fileName)
      FileOutputStream(file).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
      }

      file.absolutePath
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  /**
   * Delete the itinerary from the internal storage
   *
   * @param itineraryId the id of the itinerary to be deleted
   */
  fun deleteItinerary(itineraryId: String): Boolean {
    val fileName = "$itineraryId$itinerarySuffix.json"
    val file = File(context.filesDir, fileName)
    return file.delete()
  }

  /**
   * Load the itinerary from the internal storage
   *
   * @param fileName the name of the file to be loaded
   */
  private fun loadItineraryFromInternalStorage(fileName: String): Itinerary? {
    return try {
      context.openFileInput(fileName).use { inputStream ->
        val itineraryJson = inputStream.bufferedReader().use { it.readText() }
        val itineraryType = object : TypeToken<Itinerary>() {}.type
        gson.fromJson(itineraryJson, itineraryType)
      }
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  /** List all the itinerary files in the internal storage */
  private fun listItineraryFiles(): List<String> {
    return context.filesDir
        .listFiles()
        ?.filter { it.name.endsWith("$itinerarySuffix.json") }
        ?.map { it.name } ?: emptyList()
  }

  /** Load all the itineraries from the internal storage */
  fun loadAllItineraries(): List<Itinerary> {
    val itineraryFiles = listItineraryFiles()
    val itineraries = mutableListOf<Itinerary>()

    for (fileName in itineraryFiles) {
      val itinerary = loadItineraryFromInternalStorage(fileName)
      itinerary?.let { itineraries.add(it) }
    }

    return itineraries
  }

  /**
   * Load the image from the internal storage
   *
   * @param imagePath the path of the image to be loaded
   */
  fun loadImageFromInternalStorage(imagePath: String): Bitmap? {
    return try {
      BitmapFactory.decodeFile(imagePath)
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }
}
