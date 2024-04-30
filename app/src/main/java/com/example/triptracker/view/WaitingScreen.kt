package com.example.triptracker.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.triptracker.R

/**
 * This function is used to display the waiting screen. It's the screen that shows a loading
 * animation when the app is waiting for data to load. Not used in the current version of the app
 * but could be very useful in the future.
 */
@Composable
fun WaitingScreen() {
  Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    Text(
        text = "Loading...",
        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp)
  }
}
