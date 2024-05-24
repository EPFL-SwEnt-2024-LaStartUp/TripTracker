package com.example.triptracker.view.map.com.example.triptracker

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import androidx.test.core.app.ApplicationProvider
import com.example.triptracker.view.map.copyFile
import com.example.triptracker.view.map.getFileExtension
import com.example.triptracker.view.map.getFileName
import com.example.triptracker.view.map.getFileNameFromCursor
import com.example.triptracker.view.map.getFilePathFromUri
import com.example.triptracker.view.map.rotateImage
import java.io.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PictureUtilsTest {

  private val context: Context = ApplicationProvider.getApplicationContext()

  @Test
  fun testGetFilePathFromUri() {
    val uri = Uri.parse("content://com.example.provider/test")
    val contentResolver = mock(ContentResolver::class.java)
    val inputStream = ByteArrayInputStream("test data".toByteArray())
    val context = mock(Context::class.java)

    `when`(context.contentResolver).thenReturn(contentResolver)
    `when`(contentResolver.openInputStream(uri)).thenReturn(inputStream)

    val result = getFilePathFromUri(uri, context)
    assertNotNull(result)
  }

  @Test
  fun testCopyFile() {
    val inputStream = ByteArrayInputStream("test data".toByteArray())
    val outputStream = ByteArrayOutputStream()

    copyFile(inputStream, outputStream)
    assertEquals("test data", outputStream.toString())
  }

  @Test
  fun testGetFileName() {
    val uri = Uri.parse("content://com.example.provider/test")
    val contentResolver = mock(ContentResolver::class.java)
    val cursor = mock(Cursor::class.java)
    val context = mock(Context::class.java)

    `when`(context.contentResolver).thenReturn(contentResolver)
    `when`(contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null))
        .thenReturn(cursor)
    `when`(cursor.moveToFirst()).thenReturn(true)
    `when`(cursor.getString(anyInt())).thenReturn("testFileName")

    val result = getFileName(uri, context)
    assertEquals("testFileName.null", result)
  }

  @Test
  fun testGetFileExtension() {
    val uri = Uri.parse("content://com.example.provider/test")
    val contentResolver = mock(ContentResolver::class.java)
    val context = mock(Context::class.java)

    `when`(context.contentResolver).thenReturn(contentResolver)
    `when`(contentResolver.getType(uri)).thenReturn("image/png")

    val result = getFileExtension(uri, context)
    assertEquals(null, result)
  }

  @Test
  fun testGetFileNameFromCursor() {
    val uri = Uri.parse("content://com.example.provider/test")
    val contentResolver = mock(ContentResolver::class.java)
    val cursor = mock(Cursor::class.java)
    val context = mock(Context::class.java)

    `when`(context.contentResolver).thenReturn(contentResolver)
    `when`(contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null))
        .thenReturn(cursor)
    `when`(cursor.moveToFirst()).thenReturn(true)
    `when`(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).thenReturn(0)
    `when`(cursor.getString(0)).thenReturn("testFileName")

    val result = getFileNameFromCursor(uri, context)
    assertEquals("testFileName", result)
  }

  //    @Test
  //    fun testAdjustBitmapOrientation() {
  //        val bitmap = mock(Bitmap::class.java)
  //        val exifInterface = mock(ExifInterface::class.java)
  //
  //        `when`(exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
  // ExifInterface.ORIENTATION_NORMAL))
  //            .thenReturn(ExifInterface.ORIENTATION_ROTATE_90)
  //
  //        val adjustedBitmap = adjustBitmapOrientation("testPath", bitmap)
  //        assertNotNull(adjustedBitmap)
  //    }

  @Test
  fun testRotateImage() {
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    val rotatedBitmap = rotateImage(bitmap, 90f)
    assertNotNull(rotatedBitmap)
    assertEquals(bitmap.width, rotatedBitmap.height)
    assertEquals(bitmap.height, rotatedBitmap.width)
  }

  //    @Test
  //    fun testGetCameraProvider() = runBlocking {
  //        val processCameraProvider = mock(ProcessCameraProvider::class.java)
  //        val context = mock(Context::class.java)
  //        `when`(ProcessCameraProvider.getInstance(context)).thenReturn(processCameraProvider)
  //
  //        val cameraProvider = context.getCameraProvider()
  //        assertNotNull(cameraProvider)
  //    }
}
