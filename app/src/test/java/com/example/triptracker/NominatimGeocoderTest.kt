package com.example.triptracker

import com.example.triptracker.model.geocoder.NominatimApi
import org.junit.Test

class NominatimGeocoderTest {

  @Test
  fun testDecode() {
    val geocoder = NominatimApi()
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
    val geocoder = NominatimApi()
    geocoder.getCity(46.519053.toFloat(), 6.568287.toFloat()) { city -> assert(city == "Lausanne") }

    geocoder.getCity(0.0.toFloat(), 0.0.toFloat()) { city -> assert(city == "Unknown") }
  }

  @Test
  fun getPOITest() {
    val geocoder = NominatimApi()
    geocoder.getPOI(46.519053.toFloat(), 6.568287.toFloat()) { poi ->
      assert(poi.contains("École Polytechnique Fédérale de Lausanne"))
    }

    geocoder.getPOI(0.0.toFloat(), 0.0.toFloat()) { poi -> assert(poi == "Unknown") }
  }
}
