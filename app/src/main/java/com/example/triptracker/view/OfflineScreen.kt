package com.example.triptracker.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalConfiguration
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
fun OfflineScreen(navigation: Navigation, onRetry: () -> Unit) {
  var blink by remember { mutableStateOf(true) }

  val color by
      animateColorAsState(
          targetValue = if (blink) MaterialTheme.colorScheme.inverseSurface else md_theme_grey,
          animationSpec =
              infiniteRepeatable(
                  animation = tween(durationMillis = 1500), repeatMode = RepeatMode.Reverse),
          label = "")

  /** Blinking effect for the wifi off icon. */
  LaunchedEffect(blink) { blink = false }

  Scaffold(bottomBar = { NavigationBar(navigation = navigation) }) { innerPadding ->
    Box(
        modifier = Modifier.padding(innerPadding).fillMaxSize(),
        contentAlignment = Alignment.Center) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(
                modifier = Modifier.height((LocalConfiguration.current.screenHeightDp * 0.17f).dp))
            Text(
                text = "Wooops !",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = (LocalConfiguration.current.screenHeightDp * 0.030f).sp,
                color = MaterialTheme.colorScheme.inverseSurface)
            Text(
                text = "No internet ?",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = (LocalConfiguration.current.screenHeightDp * 0.030f).sp,
                color = MaterialTheme.colorScheme.inverseSurface)
            Spacer(
                modifier = Modifier.height((LocalConfiguration.current.screenHeightDp * 0.075f).dp))
            Icon(
                imageVector = Icons.Default.SignalWifiOff,
                contentDescription = "Airplane Mode Active",
                modifier = Modifier.size((LocalConfiguration.current.screenHeightDp * 0.090f).dp),
                tint = color // Blinking effect
                )
            Spacer(
                modifier = Modifier.height((LocalConfiguration.current.screenHeightDp * 0.025f).dp))
            Text(
                text = "Try again when you're back",
                fontFamily = Montserrat,
                fontSize = (LocalConfiguration.current.screenHeightDp * 0.018f).sp,
                color = MaterialTheme.colorScheme.inverseSurface)
            Text(
                text = "in the land of Wi-Fi!",
                fontFamily = Montserrat,
                fontSize = (LocalConfiguration.current.screenHeightDp * 0.018f).sp,
                color = MaterialTheme.colorScheme.inverseSurface)
            Spacer(
                modifier = Modifier.height((LocalConfiguration.current.screenHeightDp * 0.15f).dp))
            FilledTonalButton(
                onClick = { onRetry() },
                modifier =
                    Modifier.width((LocalConfiguration.current.screenHeightDp * 0.18f).dp)
                        .height((LocalConfiguration.current.screenHeightDp * 0.07f).dp),
                colors =
                    ButtonDefaults.filledTonalButtonColors(
                        containerColor = md_theme_orange, contentColor = Color.White)) {
                  Icon(
                      imageVector = Icons.Default.Refresh,
                      contentDescription = "Refresh",
                      modifier =
                          Modifier.size((LocalConfiguration.current.screenHeightDp * 0.03f).dp),
                      tint = Color.White)
                  Spacer(
                      modifier =
                          Modifier.width((LocalConfiguration.current.screenHeightDp * 0.010f).dp))
                  Text(
                      text = "Retry",
                      fontFamily = Montserrat,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = (LocalConfiguration.current.screenHeightDp * 0.025f).sp,
                      color = Color.White)
                }
          }
        }
  }
}
