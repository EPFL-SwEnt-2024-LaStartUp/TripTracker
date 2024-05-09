package com.example.triptracker.view

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
/**
 * @param navigation (Navigation): Navigation object to navigate to other screens
 * @brief NavigationBar composable that displays the bottom navigation bar once logged in
 */
fun NavigationBar(navigation: Navigation) {
  NavigationBar(
      modifier = Modifier.height(73.dp),
      content = {
        val destinations = navigation.getTopLevelDestinations()
        destinations.forEach { destination ->
          NavigationBarItem(
              icon = { Icon(destination.icon, contentDescription = destination.textId) },
              label = { Text(destination.textId) },
              selected = navigation.getCurrentDestination().route == destination.route,
              onClick = { navigation.navigateTo(destination) })
        }
      })
}
