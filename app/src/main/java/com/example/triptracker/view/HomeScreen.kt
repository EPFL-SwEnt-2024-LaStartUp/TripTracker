package com.example.triptracker.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.view.theme.md_theme_gray
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navigation: Navigation, homeViewModel: HomeViewModel = viewModel()) {
  Log.d("HomeScreen", "Rendering HomeScreen")
  Scaffold(
      topBar = {
        // Assuming a SearchBar composable is defined elsewhere
        SearchBar(
            modifier = Modifier.fillMaxWidth().padding(16.dp), onSearch = { /* handle search */})
      },
      bottomBar = {
        // Bottom bar content goes here, if any
      },
      modifier = Modifier.testTag("HomeScreen")) { innerPadding ->
        when (val itineraries = homeViewModel.itineraryList.value) {
          null -> {
            Text(
                text = "You do not have any itineraries yet.",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp)
          }
          else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).testTag("ItineraryList"),
                contentPadding = PaddingValues(16.dp)) {
                  items(itineraries) { itinerary ->
                    Log.d("ItineraryToDisplay", "Displaying itinerary: $itinerary")
                    DisplayItinerary(
                        itinerary = itinerary,
                        navigation = navigation,
                        homeViewModel.pinNamesMap.value ?: emptyMap())
                  }
                }
          }
        }
      }
}

@Composable
fun DisplayItinerary(
    itinerary: Itinerary,
    navigation: Navigation,
    pinNamesMap: Map<String, List<String>>
) {
  // Number of additional itineraries not displayed
  val pinNames = pinNamesMap[itinerary.id]
  val numNotDisplayed = pinNames?.size?.minus(3)
  // pinNamesMap[itinerary.id]?.let { Log.d("PININHOME", "Pinned places: $it") }
  val pinListString = pinNames?.let { convertPinListToString(it) }
  // Log.d("AHHH", "$pinListString")
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .padding(vertical = 10.dp)
              .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))
              .clickable { navigation.navController.navigate(Route.LOGIN) }) {
        Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {
          Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model =
                    "https://img.freepik.com/free-photo/portrait-beautiful-young-woman-standing-grey-wall_231208-10760.jpg?w=2000&t=st=1711454089~exp=1711454689~hmac=6f14370e52705014b746e505ad5eaa349d39cb10da32e08df52fdeb9dbf9ad9f",
                contentDescription = "User Avatar",
                modifier = Modifier.size(20.dp).clip(CircleShape))
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = itinerary.username,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = md_theme_gray)
            Spacer(modifier = Modifier.width(120.dp))
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Star",
                Modifier.size(20.dp)) // it's here but yo
          }
          Spacer(modifier = Modifier.height(10.dp))

          Text(
              text = itinerary.title,
              fontFamily = FontFamily(Font(R.font.montserrat_regular)),
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp,
              color = md_theme_light_onPrimary)
          Text(
              text = "${itinerary.flameCount}🔥",
              color = md_theme_orange, // This is the orange color
              fontFamily = FontFamily(Font(R.font.montserrat_regular)),
              fontSize = 14.sp)
          Spacer(modifier = Modifier.height(20.dp))
          Spacer(modifier = Modifier.heightIn(6.dp).weight(1f))
          Text(
              text = "$pinListString",
              fontSize = 14.sp,
              fontFamily = FontFamily(Font(R.font.montserrat_medium)),
              color = md_theme_gray)
        }
      }
}

fun convertPinListToString(pinList: List<String>): String {
  return if (pinList.size <= 3) {
    pinList.joinToString(", ")
  } else {
    val displayedPins = pinList.take(3).joinToString(", ")
    val remainingCount = pinList.size - 3
    "$displayedPins, and $remainingCount more"
  }
}

// Replace 'R.drawable.ic_profile' with actual drawable resource identifier
@Composable
fun SearchBar(modifier: Modifier = Modifier, onSearch: (String) -> Unit) {
  TextField(
      value = "",
      onValueChange = { it -> onSearch(it) },
      modifier = modifier,
      placeholder = { Text("Search for an itinerary") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
      colors =
          TextFieldDefaults.colors(
              focusedContainerColor = Color.White,
              focusedLeadingIconColor = Color.Gray,
              focusedPlaceholderColor = Color.Gray,
              focusedTextColor = Color.Black),
      shape = MaterialTheme.shapes.small.copy(CornerSize(50)))
}
