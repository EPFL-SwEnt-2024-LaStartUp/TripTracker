package com.example.triptracker.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng

/**
 * ViewModel for recording trips.
 * This ViewModel is responsible for managing the state of a trip recording, including start time, end time, pause status, and the list of LatLng points.
 */
class RecordViewModel {

    // Start time of the recording
    private val startTime = mutableLongStateOf(0L)
    // End time of the recording
    private val endTime = mutableLongStateOf(0L)
    // Boolean state representing whether the recording is paused
    val isPaused = mutableStateOf(false)

    // Private mutable list of LatLng points
    private var _latLongList = mutableListOf<LatLng>()
    // Public immutable list of LatLng points
    val latLongList: List<LatLng>
        get() = _latLongList

    /**
     * Starts the recording.
     * Sets the start time to the current time.
     */
    fun startRecording() {
        startTime.longValue = System.currentTimeMillis()
    }

    /**
     * Stops the recording.
     * Sets the end time to the current time and logs the data collected.
     */
    fun stopRecording() {
        endTime.longValue = System.currentTimeMillis()
        // TODO do something with the data
        Log.e("DATA", "Data collected: ${_latLongList.size} points, ${getElapsedTime()} ms")
        //clear the list
    }

    /**
     * Resets the recording.
     * Clears the start time, end time, and LatLng list.
     */
    fun resetRecording() {
        startTime.longValue = 0L
        endTime.longValue = 0L
        _latLongList.clear()
    }

    /**
     * Pauses the recording.
     * Sets the end time to the current time and sets the pause status to true.
     */
    fun pauseRecording() {
        endTime.longValue = System.currentTimeMillis()
        isPaused.value = true
    }

    /**
     * Resumes the recording.
     * Adjusts the start time based on the duration of the pause and sets the pause status to false.
     */
    fun resumeRecording() {
        startTime.longValue += System.currentTimeMillis() - endTime.longValue
        isPaused.value = false
    }

    /**
     * Checks if the recording is ongoing.
     * @return Boolean indicating whether the recording is ongoing.
     */
    fun isRecording(): Boolean {
        return startTime.longValue != 0L
    }

    /**
     * Calculates the elapsed time of the recording.
     * @return Long representing the elapsed time in milliseconds.
     */
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

    /**
     * Adds a LatLng point to the list.
     * @param latLng The LatLng point to add.
     */
    fun addLatLng(latLng: LatLng) {
        _latLongList.add(latLng)
    }

}