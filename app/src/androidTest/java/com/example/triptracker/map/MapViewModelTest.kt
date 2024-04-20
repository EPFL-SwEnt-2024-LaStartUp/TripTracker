package com.example.triptracker.map

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import io.mockk.verify
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
  fun viewModelCreationWorks() {
    geocoder = mockk(relaxed = true)
    assertNotNull(geocoder)
    pathList = mockk(relaxed = true)
    assertNotNull(pathList)
    repo = mockk(relaxed = true)
    assertNotNull(repo)
    filteredPathList = mockk(relaxed = true)
    assertNotNull(filteredPathList)
    viewModel = MapViewModel(geocoder, pathList, repo, filteredPathList)
    assertNotNull(viewModel)
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
  fun testReverseDecode() {
    assert(viewModel.cityNameState.value == "")
    viewModel.reverseDecode(45.75777F, 4.831964F)
    verify { geocoder.getCity(any(), any(), any()) }
  }

  @Test
  fun testGetAllPaths() {
    every { pathList.value?.getAllItineraries() } returns null
    viewModel.getAllPaths()
    verify { pathList.value?.getAllItineraries() }
  }

  @Test
  fun testGetFilteredPaths() {
    viewModel.getFilteredPaths(null)
    every { pathList.value } returns null
    viewModel.getFilteredPaths(LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0)))
    verify { filteredPathList.postValue(allAny()) }
  }
}
