package com.example.triptracker.itinerary

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryDownload
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import java.io.File
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItineraryDownloadTest {

  private lateinit var appContext: Context
  private lateinit var itineraryDownload: ItineraryDownload
  private lateinit var mockItineraries: List<Itinerary>
  private lateinit var mockItineraryWithPicture: Itinerary
  @RelaxedMockK private lateinit var mockItineraryDownload: ItineraryDownload

  @Before
  fun setUp() {
    appContext = InstrumentationRegistry.getInstrumentation().targetContext
    itineraryDownload = ItineraryDownload(context = appContext, gson = Gson())
    mockItineraries = MockItineraryList().getItineraries()
    mockItineraryDownload = mockk(relaxed = true)
    mockItineraryWithPicture =
        Itinerary(
            "eiffel",
            "Trip to Paris",
            "User1",
            Location(0.0, 0.0, "Paris"),
            200,
            saves = 50,
            clicks = 20,
            numStarts = 16, // 2 * 50 + 20 + 5 * 16 = 200
            "10-03-2024",
            "20-03-2024",
            listOf(
                Pin(
                    0.2,
                    0.1,
                    "Eiffel Tower",
                    "yes",
                    listOf(
                        "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.britannica.com%2Ftopic%2FEiffel-Tower-Paris-France&psig=AOvVaw0RG2d4WA7d_XrOp2IIjkjz&ust=1716899733992000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCNj74qLsrYYDFQAAAAAdAAAAABAH"))),
            "super cool",
            listOf(LatLng(0.1, 0.4)))
  }

  @Test
  fun testSaveItineraryJsonToFile() {
    itineraryDownload.saveItineraryToInternalStorage(mockItineraries[0]) {
      val file = File(appContext.filesDir, "1_itinerary.json")
      assertTrue(file.exists())
    }

    itineraryDownload.saveItineraryToInternalStorage(mockItineraryWithPicture) {
      val file = File(appContext.filesDir, "eiffel_itinerary.json")
      assertTrue(file.exists())
    }
  }

  @Test
  fun testLoadItineraryFromInternalStorage() {
    itineraryDownload.saveItineraryToInternalStorage(mockItineraries[1]) {
      val loadedItinerary = itineraryDownload.loadAllItineraries()
      assertNotNull(loadedItinerary)
      assert(loadedItinerary.size == 1)
      assertEquals(mockItineraries[1].id, loadedItinerary[0].id)
      itineraryDownload.saveItineraryToInternalStorage(mockItineraryWithPicture) {
        val loadedItinerary2 = itineraryDownload.loadAllItineraries()
        assertNotNull(loadedItinerary2)
        assert(loadedItinerary2.size == 2)
        assertEquals(mockItineraryWithPicture.id, loadedItinerary2[1].id)
      }
    }
  }

  @Test
  fun testDeleteItinerary() {
    itineraryDownload.saveItineraryToInternalStorage(mockItineraries[0]) {
      val isDeleted = itineraryDownload.deleteItinerary(mockItineraries[0].id)
      assertTrue(isDeleted)
      val file = File(appContext.filesDir, "3_itinerary.json")
      assertFalse(file.exists())
    }

    itineraryDownload.saveItineraryToInternalStorage(mockItineraryWithPicture) {
      val isDeleted = itineraryDownload.deleteImageFromInternalStorage("eiffel_itinerary.json")
      assertTrue(isDeleted)
      val file = File(appContext.filesDir, "eiffel_itinerary.json")
      assertFalse(file.exists())
    }
  }

  @Test
  fun testSaveImageToInternalStorage() {
    val imageUrl =
        "https://www.google.com/url?sa=i&url=https%3A%2F%2Fparisjetaime.com%2Fculture%2Fla-tour-eiffel-p3486&psig=AOvVaw0RG2d4WA7d_XrOp2IIjkjz&ust=1716899733992000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCNj74qLsrYYDFQAAAAAdAAAAABAf"
    itineraryDownload.saveImageToInternalStorage(imageUrl) { imagePath ->
      val file = File(imagePath)
      assertTrue(file.exists())
    }
  }

  @Test
  fun testLoadAllItineraries() {
    var allItineraries = itineraryDownload.loadAllItineraries()
    assertNotNull(allItineraries)
    assertTrue(allItineraries.isEmpty())

    itineraryDownload.saveItineraryToInternalStorage(mockItineraries[0]) {
      itineraryDownload.saveItineraryToInternalStorage(mockItineraries[1]) {
        allItineraries = itineraryDownload.loadAllItineraries()
        assertEquals(2, allItineraries.size)
      }
    }
  }

  @Test
  fun testListItineraryFiles() {
    itineraryDownload.saveItineraryToInternalStorage(mockItineraries[0]) {
      val fileNames = itineraryDownload.listItineraryFiles()
      assertNotNull(fileNames)
      assertTrue(fileNames.size == 1)
      assertEquals(fileNames[0], "1_itinerary.json")
    }
  }
}
