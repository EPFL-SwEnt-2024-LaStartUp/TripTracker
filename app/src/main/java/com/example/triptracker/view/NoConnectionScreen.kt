package com.example.triptracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoConnectionScreen(onRetry: () -> Unit) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = "No connection", fontSize = 24.sp, color = Color.Black)
      Spacer(modifier = Modifier.height(16.dp))
      Icon(
          imageVector = Icons.Default.AirplanemodeActive,
          contentDescription = "Airplane Mode Active",
          modifier = Modifier.size(72.dp),
          tint = Color.Black)
      Spacer(modifier = Modifier.height(16.dp))
      Button(onClick = { onRetry() }, modifier = Modifier.padding(16.dp)) {
        Icon(
            imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Retry", color = Color.White)
      }
    }
  }
}
