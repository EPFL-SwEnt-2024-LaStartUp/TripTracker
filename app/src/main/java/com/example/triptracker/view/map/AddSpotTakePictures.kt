package com.example.triptracker.view.map

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.triptracker.view.theme.md_theme_dark_black
import com.example.triptracker.view.theme.md_theme_light_black
import java.io.File
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun TakePicture(
    outputDirectory: File,
    executor: Executor,
    onCaptureClosedSuccess: () -> Unit,
    onCaptureClosedError: () -> Unit
) {
  var shouldShowCamera by remember { mutableStateOf(true) }
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
    false -> {}
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
  val configuration = LocalConfiguration.current
  val orientation = configuration.orientation
  Log.d("LALALLALAALAL", "Orientation: $orientation")

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
              .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))) {
        AndroidView(
            { previewView },
        ) { view ->
          view.layoutParams =
              ViewGroup.LayoutParams(
                  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
          view.clipToOutline = true
        }

        IconButton(
            modifier = Modifier.padding(bottom = 20.dp).align(Alignment.BottomCenter).size(60.dp),
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

private fun adjustBitmapOrientation(filePath: String, bitmap: Bitmap): Bitmap {
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

private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
  val matrix = Matrix()
  matrix.postRotate(angle)
  return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

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

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
      ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener(
            { continuation.resume(cameraProvider.get()) }, ContextCompat.getMainExecutor(this))
      }
    }
