package com.example.triptracker.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecordViewModel {

    private val startTime = mutableLongStateOf(0L)
    private val endTime = mutableLongStateOf(0L)
    val isPaused = mutableStateOf(false)

    private var _latLongList = mutableListOf<LatLng>()
    val latLongList: List<LatLng>
        get() = _latLongList

    private var currentLatLng = mutableStateOf(LatLng(0.0, 0.0))

    fun startRecording() {
        startTime.longValue = System.currentTimeMillis()
        Log.d("start time", startTime.longValue.toString())
        //trackLocation()
    }

    fun stopRecording() {
        endTime.longValue = System.currentTimeMillis()
        // TODO do something with the data
        Log.e("DATA", "Data collected: ${_latLongList.size} points, ${getElapsedTime()} ms")
        //clear the list
    }

    fun resetRecording() {
        startTime.longValue = 0L
        endTime.longValue = 0L
        _latLongList.clear()
    }

    fun pauseRecording() {
        endTime.longValue = System.currentTimeMillis()
        isPaused.value = true
    }

    fun resumeRecording() {
        startTime.longValue += System.currentTimeMillis() - endTime.longValue
        isPaused.value = false
    }

    fun isRecording(): Boolean {
        return startTime.longValue != 0L
    }

    fun getElapsedTime(): Long {
        return if (isRecording() && !isPaused.value) {
            val cur = System.currentTimeMillis()
            cur - startTime.longValue
        }
        else if (isRecording() && isPaused.value) {
            endTime.longValue - startTime.longValue
        }
        else {
            0L
        }
    }

    fun getStartTime(): Long {
        return startTime.longValue
    }

    fun addLatLng(latLng: LatLng) {
        _latLongList.add(latLng)
    }

    fun updateLatLng(latLng: LatLng) {
        currentLatLng.value = latLng
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun trackLocation() {
        GlobalScope.launch {
            while(isRecording()) {
                // get current location
                val currentLocation = currentLatLng.value

                // add to arraylist
                _latLongList.add(currentLocation)

                Log.d("pos added", if(_latLongList.isNotEmpty()) _latLongList.last().toString() else "empty")
                Log.d("list size", _latLongList.size.toString())
                Log.d("waiting", "waiting for 1 second)")
                // sleep for n seconds
                delay(10000)
                Log.d("waiting", "done waiting")
            }
        }
    }


}