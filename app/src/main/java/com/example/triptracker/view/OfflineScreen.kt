package com.example.triptracker.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_orange

/**
 * Screen that is displayed when the device is not connected to the internet. It shows a message
 * that the device is not connected to the internet and a button to retry.
 *
 * @param onRetry: Callback that is called when the user clicks on the retry button.
 */
@Composable
fun OfflineScreen(onRetry: () -> Unit) {
  var blink by remember { mutableStateOf(true) }

  val color by
      animateColorAsState(
          targetValue = if (blink) Color.Black else md_theme_grey,
          animationSpec =
              infiniteRepeatable(
                  animation = tween(durationMillis = 1500), repeatMode = RepeatMode.Reverse),
          label = "")

  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Box(
          modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp),
          contentAlignment = Alignment.Center) {
            Text(
                text = "Whoops! No internet? Try again when you're back in the land of Wi-Fi!",
                fontFamily = Montserrat,
                fontSize = 24.sp,
                color = Color.Black)
          }
      Spacer(modifier = Modifier.height(16.dp))
      Icon(
          imageVector = Icons.Default.AirplanemodeActive,
          contentDescription = "Airplane Mode Active",
          modifier = Modifier.size(100.dp),
          tint = color // Blinking effect
          )
      Spacer(modifier = Modifier.height(100.dp))
      FilledTonalButton(
          onClick = { onRetry() },
          modifier = Modifier.padding(16.dp),
          colors =
              ButtonDefaults.filledTonalButtonColors(
                  containerColor = md_theme_orange, contentColor = Color.White)) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Retry",
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Color.White)
          }
    }
  }

  /** Blinking effect for the airplane icon. */
  LaunchedEffect(blink) { blink = false }
}
