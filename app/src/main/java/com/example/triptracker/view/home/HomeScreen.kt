package com.example.triptracker.view.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.viewmodel.HomeViewModel

/**
 * HomeScreen composable that displays the list of itineraries
 *
 * @param navigation: Navigation object to use for navigation
 * @param homeViewModel: HomeViewModel to use for fetching itineraries
 */
@Composable
fun HomeScreen(navigation: Navigation, homeViewModel: HomeViewModel = viewModel()) {
  Log.d("HomeScreen", "Rendering HomeScreen")

  // Filtered list of itineraries based on search query
  val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())
  var isSearchActive by remember { mutableStateOf(false) }
  val isNoResultFound =
      remember(filteredList, isSearchActive) {
        isSearchActive && filteredList.isEmpty() && homeViewModel.searchQuery.value!!.isNotEmpty()
      }

  Scaffold(
      topBar = {
        // Assuming a SearchBar composable is defined elsewhere
        SearchBar(
            onBackClicked = {
              // Navigate back to the overview
              val home = navigation.getTopLevelDestinations()[0].route
              navigation.navController.navigate(home)
            },
            onSearchActivated = { isActive -> isSearchActive = isActive },
            navigation = navigation,
            isNoResultFound = isNoResultFound)
      },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.testTag("HomeScreen")) { innerPadding ->
        when (val itineraries = homeViewModel.itineraryList.value ?: emptyList()) {
          null -> {
            Text(
                text = "You do not have any itineraries yet.",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp)
          }
          else -> {
            // will display the list of itineraries
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).testTag("ItineraryList"),
                contentPadding = PaddingValues(16.dp)) {
                  items(itineraries) { itinerary ->
                    Log.d("ItineraryToDisplay", "Displaying itinerary: $itinerary")
                    DisplayItinerary(itinerary = itinerary, navigation = navigation)
                  }
                }
          }
        }
      }
}

/**
 * Displays the search bar at the top of the screen
 *
 * @param onBackClicked: Function to call when the back button is clicked
 * @param onSearchActivated: Function to call when the search bar is activated
 * @param viewModel: HomeViewModel to use for searching itineraries
 * @param isNoResultFound: Boolean to indicate if no results were found
 * @param navigation: Navigation object to use for navigation, i.e when an itinerary is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onBackClicked: () -> Unit = {},
    onSearchActivated: (Boolean) -> Unit,
    viewModel: HomeViewModel = viewModel(),
    isNoResultFound: Boolean = false,
    navigation: Navigation
) {

  var searchText by remember { mutableStateOf("") }
  val items = viewModel.filteredItineraryList.value ?: listOf()
  val focusManager = LocalFocusManager.current // Get access to the focus manager

  // If the search bar is active (in focus or contains text), we'll consider it active.
  var isActive by remember(searchText) { mutableStateOf(searchText.isNotEmpty()) }

  androidx.compose.material3.SearchBar(
      modifier = Modifier.fillMaxWidth().padding(14.dp).testTag("SearchItinerary"),
      query = searchText,
      onQueryChange = { newText ->
        searchText = newText
        viewModel.setSearchQuery(newText)
        onSearchActivated(isActive)
        isActive =
            newText.isNotEmpty() ||
                focusManager.equals(true) // Properly update the search query in the ViewModel
      },
      onSearch = { viewModel.setSearchQuery(searchText) },
      active = isActive,
      onActiveChange = { activeState ->
        isActive = activeState
        if (!activeState) { // When deactivating, clear the search text.
          searchText = ""
          viewModel.setSearchQuery("") // Reset search query
        }
      },
      placeholder = {
        Text(
            "Search for an itinerary",
            modifier = Modifier.width(200.dp),
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp,
            color = md_theme_grey)
      },
      leadingIcon = {
        if (isActive) {
          androidx.compose.material.Icon(
              modifier = Modifier.clickable { onBackClicked() },
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back")
        } else {
          androidx.compose.material.Icon(
              imageVector = Icons.Default.Search, contentDescription = "Menu")
        }
      },
      shape = MaterialTheme.shapes.small.copy(CornerSize(50)),
      trailingIcon = {
        if (isActive) {
          androidx.compose.material.Icon(
              modifier =
                  Modifier.clickable {
                    // Clear the text field
                    searchText = ""
                    viewModel.setSearchQuery("")
                    // TODO: For now when clearing the text, it loses focus and exits the
                    // "search mode"
                    // we need to find a way to keep the search bar active even when the text is
                    // cleared
                  },
              imageVector = Icons.Default.Close,
              contentDescription = "Clear text field")
        }
      },
  ) {
    LaunchedEffect(searchText) { onSearchActivated(isActive) }
    if (isNoResultFound) {
      Text(
          "No results found",
          modifier = Modifier.padding(16.dp),
          fontFamily = FontFamily(Font(R.font.montserrat_regular)),
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.15.sp,
          color = Color.Red)
    }
    // This displays the list of itineraries in the search results
    LazyColumn {
      val listToShow = if (isActive) items else viewModel.itineraryList.value ?: listOf()
      items(listToShow) { itinerary ->
        ItineraryItem(
            itinerary = itinerary,
            onItineraryClick = { id ->
              // Hard coded for the moment
              // TODO: Navigate to the itinerary details screen (2nd screen Figma)
              val map = navigation.getTopLevelDestinations()[1]
              navigation.navigateTo(map)
            })
      }
    }
  }
}

/**
 * Displays an itinerary item when searching for itineraries
 *
 * @param itinerary: Itinerary object to display
 * @param onItineraryClick: Function to call when the itinerary is clicked
 */
@Composable
fun ItineraryItem(itinerary: Itinerary, onItineraryClick: (String) -> Unit) {
  Row(
      modifier =
          Modifier.fillMaxWidth().clickable { onItineraryClick(itinerary.id) }.padding(16.dp)) {
        Text(text = itinerary.title, style = MaterialTheme.typography.bodyMedium)
      }
}
