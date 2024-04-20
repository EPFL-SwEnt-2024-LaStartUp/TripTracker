package com.example.triptracker.map

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.geocoder.NominatimApi
import com.example.triptracker.viewmodel.MapViewModel
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapViewModelTest {

  private lateinit var viewModel: MapViewModel

  @Before
  fun setUp() {
    viewModel = MapViewModel()
  }

  @Test
  fun testReverseDecode() {
    val geodecoder = mockk<NominatimApi>(relaxed = true)
    every { geodecoder.getCity(any(), any(), callback=any()) } returns Unit
    every { geodecoder.decode(any()) {} } returns Unit
    every { geodecoder.getPOI(any(), any()) {} } returns Unit
    every { geodecoder. reverseDecodeAddress(any(), any()) {} } returns Unit
    assert(viewModel.cityNameState.value == "")
    viewModel.reverseDecode(45.75777F, 4.831964F, geodecoder)
    verify {geodecoder.getCity(any(), any(), any()) }
    //assert(viewModel.cityNameState.value == "Lyon")

  }

  // @Test
  // fun testReverseDecode2() {
  //    val geodecoder = mockk<NominatimApi>()
  //    every { geodecoder.getCity(any(), any()) { _ -> viewModel.cityNameState.value = "Lyon" } }
  // returns Unit
  //    every { geodecoder.decode(any()) {} } returns Unit
  //    every { geodecoder.getPOI(any(), any()) {} } returns Unit
  //    every { geodecoder. reverseDecodeAddress(any(), any()) {} } returns Unit
  //    assert(viewModel.cityNameState.value == "")
  //    viewModel.reverseDecode(45.75777F, 4.831964F, geodecoder)
  //    assert(viewModel.cityNameState.value == "Lyon")
  //
  // }
}
