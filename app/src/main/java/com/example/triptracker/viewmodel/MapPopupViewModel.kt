package com.example.triptracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.triptracker.model.geocoder.NominatimApi

/**
 * ViewModel for the MapPopup composable.
 * It contains the address state and the geocoder to reverse decode the location.
 */
open class MapPopupViewModel : ViewModel() {

  private val _address = MutableLiveData<String>()
  val address: LiveData<String> = _address

  private val geocoder = NominatimApi()

  /**
   * Fetches the address for the given latitude and longitude.
   *
   * @param latitude: Latitude of the location
   * @param longitude: Longitude of the location
   */
  fun fetchAddressForPin(latitude: Float, longitude: Float) {
    geocoder.reverseDecodeAddress(latitude, longitude) { result -> _address.postValue(result) }
  }
}
