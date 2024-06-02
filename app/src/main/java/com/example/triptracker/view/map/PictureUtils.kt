package com.example.triptracker.view.map

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Function that gets the real file path from the generic photo picker URI.
 *
 * @param uri The URI of the file.
 * @param context The context of the application.
 * @return The real file path.
 */
@Throws(IOException::class)
fun getFilePathFromUri(uri: Uri?, context: Context?): Uri? {
  val fileName: String? = getFileName(uri, context)
  val file = fileName?.let { File(context?.externalCacheDir, it) }
  file?.createNewFile()
  FileOutputStream(file).use { outputStream ->
    if (uri != null) {
      context?.contentResolver?.openInputStream(uri).use { inputStream ->
        copyFile(inputStream, outputStream)
        outputStream.flush()
      }
    }
  }
  return Uri.fromFile(file)
}

/**
 * Function that copies the file from the input stream to the output stream.
 *
 * @param `in` The input stream.
 * @param out The output stream.
 */
@Throws(IOException::class)
fun copyFile(`in`: InputStream?, out: OutputStream) {
  val buffer = ByteArray(1024)
  var read: Int? = null
  while (`in`?.read(buffer).also { read = it!! } != -1) {
    read?.let { out.write(buffer, 0, it) }
  }
}

/**
 * Function that gets the file name from the URI.
 *
 * @param uri The URI of the file.
 * @param context The context of the application.
 * @return The file name.
 */
fun getFileName(uri: Uri?, context: Context?): String? {
  var fileName: String? = getFileNameFromCursor(uri, context)
  if (fileName == null) {
    val fileExtension: String? = getFileExtension(uri, context)
    fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""
  } else if (!fileName.contains(".")) {
    val fileExtension: String? = getFileExtension(uri, context)
    fileName = "$fileName.$fileExtension"
  }
  return fileName
}

/**
 * Function that gets the file extension from the URI.
 *
 * @param uri The URI of the file.
 * @param context The context of the application.
 * @return The file extension.
 */
fun getFileExtension(uri: Uri?, context: Context?): String? {
  val fileType: String? = uri?.let { context?.contentResolver?.getType(it) }
  return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

/**
 * Function that gets the file name from the cursor.
 *
 * @param uri The URI of the file.
 * @param context The context of the application.
 * @return The file name.
 */
@SuppressLint("Recycle")
fun getFileNameFromCursor(uri: Uri?, context: Context?): String? {
  val fileCursor: Cursor? =
      uri?.let {
        context
            ?.contentResolver
            ?.query(it, arrayOf<String>(OpenableColumns.DISPLAY_NAME), null, null, null)
      }
  var fileName: String? = null
  if (fileCursor != null && fileCursor.moveToFirst()) {
    val cIndex: Int = fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    if (cIndex != -1) {
      fileName = fileCursor.getString(cIndex)
    }
  }
  return fileName
}

/**
 * Function that adjusts the orientation of the bitmap.
 *
 * @param filePath The path of the file.
 * @param bitmap The bitmap that will be adjusted.
 * @return The adjusted bitmap.
 */
fun adjustBitmapOrientation(filePath: String, bitmap: Bitmap): Bitmap {
  val ei = ExifInterface(filePath)
  val orientation =
      ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
  return when (orientation) {
    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
    else -> bitmap
  }
}

/**
 * Function that rotates the image.
 *
 * @param source The source bitmap.
 * @param angle The angle of rotation.
 * @return The rotated bitmap.
 */
fun rotateImage(source: Bitmap, angle: Float): Bitmap {
  val matrix = Matrix()
  matrix.postRotate(angle)
  return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

/**
 * Function that gets the camera provider.
 *
 * @return The camera provider.
 */
suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
  ProcessCameraProvider.getInstance(this).also { cameraProvider ->
    cameraProvider.addListener(
        { continuation.resume(cameraProvider.get()) }, ContextCompat.getMainExecutor(this))
  }
}
