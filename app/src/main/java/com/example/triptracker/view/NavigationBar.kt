package com.example.triptracker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
/**
 * @param navigation (Navigation): Navigation object to navigate to other screens
 * @brief NavigationBar composable that displays the bottom navigation bar once logged in
 */
fun NavigationBar(navigation: Navigation, selectedItem: TopLevelDestination) {
  NavigationBar(
      containerColor = Color.Black, // Set the background color to black
      content = {
        val destinations = navigation.getTopLevelDestinations()
        destinations.forEachIndexed { index, item ->
          NavigationBarItem(
              icon = {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = item.icon),
                    contentDescription = item.route,
                )
              },
              label = { Text(item.textId, color = Color.White) }, // Set text color to white
              selected = selectedItem.route == item.route,
              onClick = {
                navigation.navigateTo(destinations[index])
              })
        }
      },
      contentColor =
          Color.White, // When another tab is clicked, a small transition animation in white appears
  )
}
