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
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.triptracker.view.theme.md_theme_light_surfaceTint
import java.io.File
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun LaunchCamera() {
  // Temporary path to store the picture
  val file = File("/storage/emulated/0/Download/TripTracker/")
  file.mkdirs()

  // Executor to handle the camera operations
  val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
  TakePicture(
      outputDirectory = file,
      executor = cameraExecutor,
  )
}

@Composable
fun TakePicture(
    outputDirectory: File,
    executor: Executor,
) {
  var shouldShowCamera by remember { mutableStateOf(false) }
  var photoUri by remember { mutableStateOf<Uri?>(null) }

  when (shouldShowCamera) {
    true -> {
      CameraView(
          outputDirectory = outputDirectory,
          executor = executor,
          onImageCaptured = {
            photoUri = it
            shouldShowCamera = false
            Log.d("TakePicture", "Photo uri: $photoUri")
          },
          onError = {
            Log.e("TakePicture", "Error taking picture", it)
            shouldShowCamera = false
          })
    }
    false -> {
      Box {
        IconButton(
            onClick = { shouldShowCamera = true },
            content = {
              Icon(
                  imageVector = Icons.Outlined.Camera,
                  contentDescription = "Take picture",
                  tint = md_theme_light_surfaceTint,
                  modifier =
                      Modifier.size(200.dp)
                          .padding(1.dp)
                          .border(1.dp, md_theme_light_surfaceTint, CircleShape))
            })
        if (photoUri != null) {
          Image(
              painter = rememberAsyncImagePainter(photoUri),
              contentDescription = null,
              modifier = Modifier.fillMaxSize())
        }
      }
    }
  }
}

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

  LaunchedEffect(lensFacing) {
    val cameraProvider = context.getCameraProvider()
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)

    preview.setSurfaceProvider(previewView.surfaceProvider)
  }

  Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
    AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

    IconButton(
        modifier = Modifier.padding(bottom = 20.dp),
        onClick = {
          takePhoto(
              context = context,
              imageCapture = imageCapture,
              outputDirectory = outputDirectory,
              executor = executor,
              onImageCaptured = onImageCaptured,
              onError = onError)
        },
        content = {
          Icon(
              imageVector = Icons.Outlined.Camera,
              contentDescription = "Take picture",
              tint = md_theme_light_surfaceTint,
              modifier =
                  Modifier.size(200.dp)
                      .padding(1.dp)
                      .border(1.dp, md_theme_light_surfaceTint, CircleShape))
        })
  }
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
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
          saveImageToGallery(photoFile = photoFile, bitmap = savedBitmap, context = context) {
              savedUri ->
            onImageCaptured(savedUri)
          }

          val savedUri = Uri.fromFile(photoFile)
          onImageCaptured(savedUri)
        }
      })
}

private fun saveImageToGallery(
    photoFile: File,
    bitmap: Bitmap,
    context: Context,
    onImageSaved: (Uri) -> Unit
) {
  val resolver = context.contentResolver

  val contentValues =
      ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, photoFile.name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
      }

  val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

  imageUri?.let { uri ->
    resolver.openOutputStream(uri)?.use { outputStream ->
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
      onImageSaved(uri)
    }
  }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
      ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener(
            { continuation.resume(cameraProvider.get()) }, ContextCompat.getMainExecutor(this))
      }
    }
