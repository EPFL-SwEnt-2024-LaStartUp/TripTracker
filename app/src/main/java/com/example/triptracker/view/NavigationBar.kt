package com.example.triptracker.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

@Composable
/**
 * @param navigation: Navigation object to navigate to other screens
 */
fun NavigationBar(navigation: Navigation) {
    var selectedItem = 0
    NavigationBar {
        navigation.getTopLevelDestinations().forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = item.route) },
                label = { Text(item.route) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}