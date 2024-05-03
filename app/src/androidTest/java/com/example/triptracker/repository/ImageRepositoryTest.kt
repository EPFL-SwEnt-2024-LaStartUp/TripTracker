package com.example.triptracker.repository

import android.net.Uri
import com.example.triptracker.model.repository.ImageRepository
import com.example.triptracker.model.repository.Response
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class ImageRepositoryTest {

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK private lateinit var mockImageRepository: ImageRepository

  @Test
  fun testAddImageToFirebaseStorage(): Unit = runBlocking {
    val imageUri = Uri.parse("app/src/main/res/drawable/triptrackerlogo.png")

    // Mock the behavior of the addImageToFirebaseStorage method
    coEvery { mockImageRepository.addImageToFirebaseStorage(imageUri) } returns
        Response.Success(imageUri)

    // Call the method on the mock object
    mockImageRepository.addImageToFirebaseStorage(imageUri)

    // Verify that the method was called
    coVerify { mockImageRepository.addImageToFirebaseStorage(imageUri) }
  }
}
