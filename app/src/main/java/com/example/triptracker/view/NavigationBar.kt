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
fun NavigationBar(navigation: Navigation) {
  val selectedItem = remember { mutableIntStateOf(0) } // By default, the home page is displayed

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
              selected = selectedItem.intValue == index,
              onClick = {
                selectedItem.intValue = index

                /** TODO uncomment this once the navigation and the other tabs are implemented */
                navigation.navigateTo(destinations[selectedItem.intValue])
              })
        }
      },
      contentColor =
          Color.White, // When another tab is clicked, a small transition animation in white appears
  )
}
