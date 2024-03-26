package com.example.triptracker.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.triptracker.viewmodel.HomeViewModel

@Composable
/**
 * @param navigation: Navigation object to navigate to other screens
 * @return: Composable function to display the home screen
 */
fun HomeScreen(navigation: Navigation) {
    val homeViewModel = HomeViewModel()
    androidx.compose.material.Scaffold(
        topBar = {
                 //TODO done by Jeremy C
        },
        bottomBar = {
            //TODO done by Jeremy C
        },
        modifier = Modifier.testTag("HomeScreen")
    ) { innerPadding ->
        when (homeViewModel.itineraryList.value) {
            null -> {
                Text(text = "You do not have any ToDos yet.")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).testTag("ItineraryList"),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(homeViewModel.itineraryList.value!!) {
                        itinerary ->
                        // non null assert call
                        //TODO Implement the Display Itinerary composable
                        //DisplayItinerary(itinerary = itinerary, navigationActions = navigationActions)
                    }
                }
            }
        }
    }
}