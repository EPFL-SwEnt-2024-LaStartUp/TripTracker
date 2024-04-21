package com.example.triptracker.map

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.itinerary.MockItineraryList
import com.example.triptracker.model.geocoder.NominatimApi
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.itinerary.ItineraryList
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.viewmodel.MapViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapViewModelTest {

  private lateinit var viewModel: MapViewModel
  @RelaxedMockK private lateinit var geocoder: NominatimApi
  @RelaxedMockK private lateinit var pathList: MutableLiveData<ItineraryList>
  @RelaxedMockK private lateinit var repo: ItineraryRepository
  @RelaxedMockK private lateinit var filteredPathList: MutableLiveData<Map<Itinerary, List<LatLng>>>

  @Before
  fun setUp() {
    geocoder = mockk(relaxed = true)
    pathList = mockk(relaxed = true)
    repo = mockk(relaxed = true)
    filteredPathList = mockk(relaxed = true)
    viewModel = MapViewModel(geocoder, pathList, repo, filteredPathList)
  }

  @Test
  fun testConstructor() {
    // Create an instance of MapViewModel without mocked dependencies but the one using the DB
    val newViewModel = MapViewModel(repository = repo)

    // Verify that the dependencies are properly initialized
    assertNotNull(newViewModel)
  }

  @Test
  fun viewModelInitialization() {
    // Verify that the ViewModel initializes with the correct dependencies
    assertNotNull(viewModel.geocoder)
    assertNotNull(viewModel.filteredPathList)
    assertNotNull(viewModel.selectedPolylineState)
    assertNotNull(viewModel.cityNameState)
  }

  @Test
  fun testSelectedPolyline() {
    val itineraryList = MockItineraryList()
    val polyline =
        MapViewModel.SelectedPolyline(itineraryList.getItineraries()[0], LatLng(0.0, 0.0))
    assertNotNull(polyline)
    assertEquals(polyline.itinerary, itineraryList.getItineraries()[0])
    assertEquals(polyline.startLocation, LatLng(0.0, 0.0))
  }

  @Test
  fun testReverseDecode() {

    val cityName = "Lyon"

    val callbackSlot = slot<(String) -> Unit>()
    every { geocoder.getCity(any(), any(), capture(callbackSlot)) } answers
        {
          callbackSlot.captured(cityName)
        }

    // At the beginning empty
    assertEquals("", viewModel.cityNameState.value)
    viewModel.reverseDecode(45.75777F, 4.831964F)
    verify { geocoder.getCity(45.75777F, 4.831964F, any()) }

    // After the callback, contains "Lyon"
    assertEquals(cityName, viewModel.cityNameState.value)
  }

  @Test
  fun testGetAllPaths() {
    val mockItineraryList = MockItineraryList()
    val expectedResult = mockItineraryList.getItineraries().associate { it.title to it.route }

    // Mock pathList.value to return null, so we can test the fallback to emptyMap()
    every { pathList.value } returns null
    val res1 = viewModel.getAllPaths()
    verify { pathList.value }
    assertEquals(emptyMap<String, List<LatLng>>(), res1)

    every { pathList.value } returns ItineraryList(mockItineraryList.getItineraries())
    val res2 = viewModel.getAllPaths()
    verify { pathList.value }
    assertEquals(expectedResult, res2)
  }

  @Test
  fun testGetFilteredPaths() {
    viewModel.getFilteredPaths(null)
    every { pathList.value } returns ItineraryList(listOf<Itinerary>())
    viewModel.getFilteredPaths(LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0)))
    verify { filteredPathList.postValue(allAny()) }
    verify { pathList.value }
  }
}
