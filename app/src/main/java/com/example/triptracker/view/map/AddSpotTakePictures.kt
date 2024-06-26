package com.example.triptracker.view.map

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.triptracker.view.AllowCameraPermission
import com.example.triptracker.view.checkForCameraPermission
import com.example.triptracker.view.theme.md_theme_dark_black
import com.example.triptracker.view.theme.md_theme_light_black
import java.io.File
import java.util.Locale
import java.util.concurrent.Executor

/**
 * Composable that displays the camera view and allows the user to take a picture and saves it to
 * the gallery.
 *
 * @param outputDirectory The directory where the picture will be saved.
 * @param executor The executor that will be used to take the picture.
 * @param onCaptureClosedSuccess Callback that will be called when the user closes the camera after
 *   taking a picture.
 * @param onCaptureClosedError Callback that will be called when an error occurs while taking a
 *   picture.
 */
@Composable
fun TakePicture(
    outputDirectory: File,
    executor: Executor,
    context: Context,
    onCaptureClosedSuccess: () -> Unit,
    onCaptureClosedError: () -> Unit
) {
  var shouldShowCamera by remember { mutableStateOf(checkForCameraPermission(context)) }
  var photoUri by remember { mutableStateOf<Uri?>(null) }

  when (shouldShowCamera) {
    true -> {
      CameraView(
          outputDirectory = outputDirectory,
          executor = executor,
          onImageCaptured = {
            photoUri = it
            shouldShowCamera = false
            onCaptureClosedSuccess()
            Log.d("TakePicture", "Photo uri: $photoUri")
          },
          onError = {
            Log.e("TakePicture", "Error taking picture", it)
            shouldShowCamera = false
            onCaptureClosedError()
          })
    }
    false -> {
      if (!checkForCameraPermission(context)) {
        AllowCameraPermission(
            onPermissionGranted = { shouldShowCamera = true },
            onPermissionDenied = { shouldShowCamera = false })
      }
    }
  }
}

/**
 * Composable that displays the camera view and allows the user to take a picture and saves it to
 * the gallery.
 *
 * @param outputDirectory The directory where the picture will be saved.
 * @param executor The executor that will be used to take the picture.
 * @param onImageCaptured Callback that will be called when the picture is taken and gives the link
 *   to the saved picture.
 * @param onError Callback that will be called when an error occurs while taking a picture.
 */
@Composable
fun CameraView(
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
  val lensFacing = CameraSelector.LENS_FACING_BACK
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  val preview = Preview.Builder().build()
  val previewView = remember { PreviewView(context) }
  val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
  val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
  val configuration = LocalConfiguration.current
  val orientation = configuration.orientation

  LaunchedEffect(lensFacing) {
    val cameraProvider = context.getCameraProvider()
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
    preview.setSurfaceProvider(previewView.surfaceProvider)
  }

  Box(
      contentAlignment = Alignment.Center,
      modifier =
          Modifier.fillMaxWidth()
              .fillMaxHeight(0.85f)
              .padding(top = 80.dp, start = 15.dp, end = 15.dp, bottom = 20.dp)
              .border(1.dp, md_theme_light_black, shape = RoundedCornerShape(35.dp))
              .clip(RoundedCornerShape(35.dp))
              .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))
              .testTag("CameraView")) {
        AndroidView(
            { previewView },
        ) { view ->
          view.layoutParams =
              ViewGroup.LayoutParams(
                  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
          view.clipToOutline = true
        }

        IconButton(
            modifier =
                Modifier.padding(bottom = 20.dp)
                    .align(Alignment.TopEnd)
                    .size(60.dp)
                    .testTag("CloseCameraScreen"),
            onClick = {
              onError(
                  ImageCaptureException(ImageCapture.ERROR_CAMERA_CLOSED, "Camera closed", null))
            },
        ) {
          Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close Camera",
              tint = md_theme_dark_black,
              modifier = Modifier.size(30.dp).padding(1.dp))
        }

        IconButton(
            modifier =
                Modifier.padding(bottom = 20.dp)
                    .align(Alignment.BottomCenter)
                    .size(60.dp)
                    .testTag("TakePictureButton"),
            onClick = {
              takePhoto(
                  context = context,
                  imageCapture = imageCapture,
                  outputDirectory = outputDirectory,
                  executor = executor,
                  onImageCaptured = onImageCaptured,
                  onError = onError,
                  orientation = orientation)
            },
        ) {
          Icon(
              imageVector = Icons.Default.Camera,
              contentDescription = "Take picture",
              tint = md_theme_dark_black,
              modifier =
                  Modifier.size(500.dp)
                      .padding(1.dp)
                      .border(1.dp, md_theme_dark_black, CircleShape))
        }
      }
}

/**
 * Function that takes a picture and saves it to the gallery.
 *
 * @param context The context of the application.
 * @param imageCapture The image capture object that will be used to take the picture.
 * @param outputDirectory The directory where the picture will be saved.
 * @param executor The executor that will be used to take the picture.
 * @param orientation The orientation of the device.
 * @param onImageCaptured Callback that will be called when the picture is taken and gives the link
 *   to the saved picture.
 * @param onError Callback that will be called when an error occurs while taking a picture.
 */
private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    orientation: Int,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

  val photoFile =
      File(
          outputDirectory,
          SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.FRANCE)
              .format(System.currentTimeMillis()) + ".jpg")

  val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

  imageCapture.takePicture(
      outputOptions,
      executor,
      object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
          onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

          val savedBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

          // Save the image to the gallery
          saveImageToGallery(
              photoFile = photoFile,
              bitmap = savedBitmap,
              context = context,
              orientation = orientation) { savedUri ->
                onImageCaptured(savedUri)
              }

          val savedUri = Uri.fromFile(photoFile)
          onImageCaptured(savedUri)
        }
      })
}

/**
 * Function that saves the image to the gallery.
 *
 * @param photoFile The file where the image will be saved.
 * @param bitmap The bitmap that will be saved.
 * @param context The context of the application.
 * @param orientation The orientation of the device.
 * @param onImageSaved Callback that will be called when the image is saved.
 */
private fun saveImageToGallery(
    photoFile: File,
    bitmap: Bitmap,
    context: Context,
    orientation: Int,
    onImageSaved: (Uri) -> Unit
) {
  val resolver = context.contentResolver

  val contentValues =
      ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, photoFile.name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.MediaColumns.ORIENTATION, orientation)
      }

  val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

  imageUri?.let { uri ->
    resolver.openOutputStream(uri)?.use { outputStream ->
      val adjustedBitmap = adjustBitmapOrientation(photoFile.absolutePath, bitmap)
      adjustedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
      onImageSaved(uri)
    }
  }
}
