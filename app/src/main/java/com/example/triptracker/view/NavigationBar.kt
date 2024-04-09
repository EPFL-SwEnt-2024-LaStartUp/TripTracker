package com.example.triptracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
/**
 * @param navigation (Navigation): Navigation object to navigate to other screens
 * @brief NavigationBar composable that displays the bottom navigation bar once logged in
 */
fun NavigationBar(navigation: Navigation) {
  NavigationBar(
      content = {
        val destinations = navigation.getTopLevelDestinations()
        destinations.forEach { destination ->
          NavigationBarItem(
              icon = {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = destination.icon),
                    contentDescription = destination.route,
                )
              },
              label = { Text(destination.textId, color = Color.White) }, // Set text color to white
              selected = navigation.getCurrentDestination().route == destination.route,
              onClick = { navigation.navigateTo(destination) })
        }
      })
}
