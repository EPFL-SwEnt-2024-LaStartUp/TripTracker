package com.example.triptracker.map

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordViewModelTest {

  private lateinit var viewModel: RecordViewModel

  private val waiter = CountDownLatch(1)

  @Before
  fun setUp() {
    viewModel = RecordViewModel()
  }

  @Test
  fun testStartRecording() {
    viewModel.startRecording()
    assert(viewModel.startDate.value.isNotEmpty())
  }

  @Test
  fun testStopRecording() {
    viewModel.stopRecording()
    assert(viewModel.endDate.value.isNotEmpty())
  }

  @Test
  fun testStartAndStopRecording() = runTest {
    viewModel.startRecording()
    assert(viewModel.isRecording())
    viewModel.stopRecording()
    assert(!viewModel.isRecording())
  }

  @Test
  fun testAddLatLng() = runTest {
    val latLng = LatLng(0.0, 0.0)
    viewModel.addLatLng(latLng)
    assertEquals(listOf(latLng), viewModel.latLongList)
  }

  @Test
  fun testResetRecording() = runTest {
    viewModel.startRecording()
    // add some latlng
    viewModel.addLatLng(LatLng(0.0, 0.0))
    viewModel.resetRecording()
    assert(!viewModel.isRecording())
    assert(viewModel.startDate.value.isEmpty())
    assert(viewModel.endDate.value.isEmpty())
    assert(viewModel.latLongList.isEmpty())
  }

  @Test
  fun testPauseRecording() = runTest {
    viewModel.startRecording()
    viewModel.pauseRecording()
    assert(viewModel.isPaused.value)
  }

  @Test
  fun testResumeRecording() = runTest {
    viewModel.startRecording()
    viewModel.pauseRecording()
    viewModel.resumeRecording()
    assert(!viewModel.isPaused.value)
  }

  @Test
  fun testStartDescription() = runTest {
    viewModel.startDescription()
    assert(viewModel.isInDescription())
  }

  @Test
  fun testStopDescription() = runTest {
    viewModel.stopDescription()
    assert(!viewModel.isInDescription())
  }

  @Test
  fun testIsInDescription() = runTest {
    viewModel.startDescription()
    assert(viewModel.isInDescription())
  }

  @Test
  fun testGetElapsedTime() = runTest {
    viewModel.startRecording()
    // Here we would ideally wait for some time before stopping the recording
    // to get a non-zero elapsed time. However, in a unit test, we should avoid
    // real waiting. One way to handle this is to use a testing framework that
    // allows controlling time, but that's beyond the scope of this example.
    waiter.await(1000 * 1000, TimeUnit.NANOSECONDS)
    // 1ms
    viewModel.stopRecording()
    assertEquals(0L, viewModel.getElapsedTime())
  }

  @Test
  fun testPrettyPrint() = runTest {
    viewModel.startRecording()
    viewModel.addLatLng(LatLng(0.0, 0.0))
    viewModel.addLatLng(LatLng(1.0, 1.0))
    viewModel.addLatLng(LatLng(2.0, 2.0))

    // prettty print is inside
    viewModel.stopRecording()
  }

  @Test
  fun testStopRecordingPaused() = runTest {
    viewModel.startRecording()
    viewModel.pauseRecording()
    viewModel.stopRecording()
    assert(viewModel.endDate.value.isNotEmpty())
  }
}
