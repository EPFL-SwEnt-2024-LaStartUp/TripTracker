package com.example.triptracker.model.itinerary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.example.triptracker.MainActivity.Companion.applicationContext
import com.example.triptracker.model.location.Pin
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.UUID

/**
 * This class is responsible for saving and loading the itinerary to and from the internal storage
 *
 * @param context the context of the activity
 * @param gson the Gson object to convert the itinerary to JSON
 */
class ItineraryDownload(
    private val context: Context = applicationContext(),
    private val gson: Gson = Gson()
) {

  // Suffix for the itinerary file
  private val itinerarySuffix = "_itinerary"

  /**
   * Save the itinerary to the internal storage
   *
   * @param itinerary the itinerary to be saved
   * @param callback a function to be called after saving the itinerary
   */
  fun saveItineraryToInternalStorage(itinerary: Itinerary, callback: () -> Unit = {}) {
    val updatedPins = mutableListOf<Pin>()
    val pinsToProcess = itinerary.pinnedPlaces.size
    var processedPins = 0

    itinerary.pinnedPlaces.forEach { pin ->
      val updatedImages = mutableListOf<String>()
      val imagesToProcess = pin.imageUrl.size
      var processedImages = 0

      pin.imageUrl.forEach { imageUrl ->
        saveImageToInternalStorage(imageUrl) { imagePath ->
          updatedImages.add(imagePath)
          processedImages++

          // When all images for the pin are saved
          if (processedImages == imagesToProcess) {
            updatedPins.add(pin.copy(imageUrl = updatedImages))
            processedPins++

            // When all pins are saved
            if (processedPins == pinsToProcess) {
              val updatedItinerary = itinerary.copy(pinnedPlaces = updatedPins)
              saveItineraryJsonToFile(updatedItinerary, callback)
            }
          }
        }
      }

      // No images to process
      if (imagesToProcess == 0) {
        updatedPins.add(pin)
        processedPins++

        // When all pins are saved
        if (processedPins == pinsToProcess) {
          val updatedItinerary = itinerary.copy(pinnedPlaces = updatedPins)
          saveItineraryJsonToFile(updatedItinerary, callback)
        }
      }
    }

    // No pins to process
    if (pinsToProcess == 0) {
      saveItineraryJsonToFile(itinerary, callback)
    }
  }

  /**
   * Save the itinerary JSON to a file
   *
   * @param itinerary the itinerary to be saved
   * @param callback a function to be called after saving the itinerary
   */
  private fun saveItineraryJsonToFile(itinerary: Itinerary, callback: () -> Unit) {
    val fileName = "${itinerary.id}$itinerarySuffix.json"
    val itineraryJson = gson.toJson(itinerary)

    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
      outputStream.write(itineraryJson.toByteArray())
    }
    callback()
  }

  /**
   * Save the image to the internal storage asynchronously
   *
   * @param imageUrl the url of the image to be saved
   * @param callback the callback function to be called after the image is saved
   */
  fun saveImageToInternalStorage(imageUrl: String, callback: (String) -> Unit) {
    DownloadImageTask(context, callback).execute(imageUrl)
  }

  /**
   * Delete an image from the internal storage
   *
   * @param imagePath the path of the image to be deleted
   * @return true if the image is deleted successfully, false otherwise
   */
  fun deleteImageFromInternalStorage(imagePath: String): Boolean {
    val file = File(imagePath)
    return file.delete()
  }

  /**
   * Delete the itinerary from the internal storage
   *
   * @param itineraryId the id of the itinerary to be deleted
   * @return true if the itinerary is deleted successfully, false otherwise
   */
  fun deleteItinerary(itineraryId: String): Boolean {
    val fileName = "$itineraryId$itinerarySuffix.json"
    val file = File(context.filesDir, fileName)
    val itinerary = loadItineraryFromInternalStorage(fileName)

    itinerary?.pinnedPlaces?.forEach { pin ->
      pin.imageUrl.forEach { imagePath -> deleteImageFromInternalStorage(imagePath) }
    }

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
  fun listItineraryFiles(): List<String> {
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

  /** AsyncTask to download an image from a URL */
  private class DownloadImageTask(val context: Context, val callback: (String) -> Unit) :
      AsyncTask<String, Void, String?>() {

    override fun doInBackground(vararg params: String?): String? {
      val imageUrl = params[0] ?: return null
      return try {
        val url = URL(imageUrl)
        val bitmap: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
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

    override fun onPostExecute(result: String?) {
      result?.let { callback(it) }
    }
  }
}
