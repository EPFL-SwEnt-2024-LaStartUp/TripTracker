package com.example.triptracker

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.geocoder.NominatimApi
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NominatimGeocoderTest {
  val geocoder = NominatimApi()

  @Test
  fun testDecode() {
    geocoder.decode("EPFL") { location ->
      assert(location.latitude == 46.519879)
      assert(location.longitude == 6.560632)
    }

    geocoder.decode("unknown") { location ->
      assert(location.latitude == 0.0)
      assert(location.longitude == 0.0)
    }
  }

  @Test
  fun getCityTest() {
    geocoder.getCity(46.519053.toFloat(), 6.568287.toFloat()) { city -> assert(city == "Lausanne") }

    geocoder.getCity(0.0.toFloat(), 0.0.toFloat()) { city -> assert(city == "Unknown") }
  }

  @Test
  fun getPOITest() {
    geocoder.getPOI(46.519053.toFloat(), 6.568287.toFloat()) { poi ->
      assert(poi.contains("École Polytechnique Fédérale de Lausanne"))
    }

    geocoder.getPOI(0.0.toFloat(), 0.0.toFloat()) { poi -> assert(poi == "Unknown") }
  }
}
