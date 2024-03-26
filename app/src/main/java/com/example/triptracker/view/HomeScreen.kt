package com.example.triptracker.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navigation: Navigation) {
    val homeViewModel = HomeViewModel()
    Log.d("HomeScreen", "Rendering HomeScreen")

    Scaffold(
        topBar = {
            // Assuming a SearchBar composable is defined elsewhere
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearch = { /* handle search */ }
            )
        },
        bottomBar = {
            // Bottom bar content goes here, if any
        },
        modifier = Modifier.testTag("HomeScreen")
    ) { innerPadding ->
        when (val itineraries = homeViewModel.itineraryList.value) {
            null -> {
                Text(text = "You do not have any itineraries yet.", modifier = Modifier.padding(16.dp))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .testTag("ItineraryList"),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(itineraries) { itinerary ->
                        Log.d("HomeScreen", "Displaying itinerary: $itinerary")
                        DisplayItinerary(itinerary = itinerary, navigation = navigation)
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayItinerary(itinerary: Itinerary, navigation: Navigation) {
    val numNotDisplayed = 3 // Number of additional itineraries not displayed
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            Color.Black,
            Color.White,
            Color.Gray,
            Color.Gray,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://img.freepik.com/free-photo/portrait-beautiful-young-woman-standing-grey-wall_231208-10760.jpg?w=2000&t=st=1711454089~exp=1711454689~hmac=6f14370e52705014b746e505ad5eaa349d39cb10da32e08df52fdeb9dbf9ad9f",
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = itinerary.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = itinerary.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${itinerary.description}, and $numNotDisplayed more.",
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More Actions"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${itinerary.flameCount}🔥",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

// Replace 'R.drawable.ic_profile' with actual drawable resource identifier
@Composable
fun SearchBar(modifier: Modifier = Modifier, onSearch: (String) -> Unit) {
    TextField(
        value = "",
        onValueChange = {it -> onSearch(it)},
        modifier = modifier,
        placeholder = { Text("Search for an itinerary") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedLeadingIconColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
            focusedTextColor = Color.Black
        ),
        shape = MaterialTheme.shapes.small.copy(CornerSize(50))
    )
}
