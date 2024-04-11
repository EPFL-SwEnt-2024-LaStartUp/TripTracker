package com.example.triptracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.triptracker.model.geocoder.NominatimApi

class MapPopupViewModel : ViewModel() {

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val geocoder = NominatimApi()

    fun fetchAddressForPin(latitude: Float, longitude: Float) {
        geocoder.reverseDecodeAddress(latitude, longitude) { result ->
            _address.postValue(result)
        }
    }
}