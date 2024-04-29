package com.example.triptracker.view.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.viewmodel.FilterType
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
  val selectedFilterType by homeViewModel.selectedFilter.observeAsState(FilterType.TITLE)

  // Filtered list of itineraries based on search query
  val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())
  var showFilterDropdown by remember { mutableStateOf(false) }
  var isSearchActive by remember { mutableStateOf(false) }
  val isNoResultFound =
      remember(filteredList, isSearchActive) {
        isSearchActive && filteredList.isEmpty() && homeViewModel.searchQuery.value!!.isNotEmpty()
      }

  Scaffold(
      topBar = {
        // Assuming a SearchBar composable is defined elsewhere
        SearchBarImplementation(
            onBackClicked = {
              // Navigate back to the home
              navigation.goBack()
            },
            viewModel = homeViewModel,
            onSearchActivated = { isActive -> isSearchActive = isActive },
            navigation = navigation,
            selectedFilterType = selectedFilterType,
            isNoResultFound = isNoResultFound)
        if (isSearchActive) {
          Box(
              modifier =
                  Modifier.padding(270.dp, 27.dp, 30.dp, 220.dp)
                      .width(200.dp)
                      .testTag("DropDownBox")) {
                DropdownMenu(
                    expanded = showFilterDropdown,
                    onDismissRequest = { showFilterDropdown = false },
                    modifier = Modifier.testTag("DropDownFilter")) {
                      FilterType.entries.forEach { filterType ->
                        DropdownMenuItem(
                            text = {
                              Text(
                                  text = filterType.name.replace('_', ' '),
                                  modifier = Modifier.testTag("FilterText"))
                            },
                            onClick = {
                              homeViewModel.setSearchFilter(filterType)
                              showFilterDropdown = false
                            })
                      }
                    }
                Text(
                    text = selectedFilterType.name,
                    modifier =
                        Modifier.clickable { showFilterDropdown = true }
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.medium)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    fontSize = 12.sp,
                )
              }
        }
      },
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.fillMaxWidth().testTag("HomeScreen")) { innerPadding ->
        when (val itineraries = homeViewModel.itineraryList.value ?: emptyList()) {
          emptyList<Itinerary>() -> {
            Text(
                text = "You do not have any itineraries yet.",
                modifier = Modifier.padding(10.dp).testTag("NoItinerariesText"),
                fontSize = 1.sp)
          }
          else -> {
            val listState = rememberLazyListState()
            // will display the list of itineraries
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).testTag("ItineraryList"),
                contentPadding = PaddingValues(16.dp),
                state = listState) {
                  items(itineraries) { itinerary ->
                    Log.d("ItineraryToDisplay", "Displaying itinerary: $itinerary")
                    DisplayItinerary(itinerary = itinerary, navigation = navigation)
                  }
                }
            /*
            TODO can be used to scroll to the top of the list

            val showButton = listState.firstVisibleItemIndex > 0
            AnimatedVisibility(visible = showButton) {
                ScrollToTopButton()
            }
             */
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
fun SearchBarImplementation(
    onBackClicked: () -> Unit = {},
    onSearchActivated: (Boolean) -> Unit,
    viewModel: HomeViewModel = viewModel(),
    selectedFilterType: FilterType,
    isNoResultFound: Boolean = false,
    navigation: Navigation
) {
  var searchText by remember { mutableStateOf("") }
  val items = viewModel.filteredItineraryList.value ?: listOf()
  val focusManager = LocalFocusManager.current
  // If the search bar is active (in focus or contains text), we'll consider it active.
  var isActive by remember { mutableStateOf(false) }

  Box(modifier = Modifier.fillMaxWidth()) {
    SearchBar(
        modifier =
            Modifier.fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 5.dp)
                .testTag("searchBar"),
        query = searchText,
        onQueryChange = { newText ->
          searchText = newText
          viewModel.setSearchQuery(newText)
          onSearchActivated(isActive)
          isActive = newText.isNotEmpty() || focusManager.equals(true)
        },
        onSearch = {
          viewModel.setSearchQuery(searchText)
          isActive = false
        },
        leadingIcon = {
          if (isActive) {
            androidx.compose.material.Icon(
                modifier = Modifier.clickable { onBackClicked() }.testTag("BackButton"),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back")
          } else {
            androidx.compose.material.Icon(
                imageVector = Icons.Default.Search, contentDescription = "Menu")
          }
        },
        trailingIcon = {
          if (isActive) {
            Icon(
                modifier =
                    Modifier.clickable {
                          if (searchText.isEmpty()) {
                            isActive = false // Deactivate the search bar if text is empty
                          } else {
                            searchText = "" // Clear the text but keep the search bar active
                            viewModel.setSearchQuery(searchText) // Reset search query
                          }
                          onSearchActivated(isActive)
                        }
                        .testTag("ClearButton"),
                imageVector = Icons.Default.Close,
                contentDescription = "Clear text field")
          }
        },
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
              "Find Itineraries",
              modifier = Modifier.padding(start = 10.dp).testTag("searchBarText"),
              textAlign = TextAlign.Center,
              fontFamily = FontFamily(Font(R.font.montserrat_bold)),
              fontSize = 21.sp,
              fontWeight = FontWeight.Medium,
              letterSpacing = 0.15.sp,
              color = md_theme_grey)
        },
        shape = MaterialTheme.shapes.small.copy(CornerSize(50)),
    ) {
      LaunchedEffect(searchText) { onSearchActivated(isActive) }

      if (isNoResultFound) {
        Text(
            "No results found",
            modifier = Modifier.padding(16.dp).testTag("NoResultsFound"),
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp,
            color = Color.Red)
      }
      // Itinerary items list
      val listToShow = if (isActive) items else viewModel.itineraryList.value ?: listOf()
      LazyColumn {
        items(listToShow) { itinerary ->
          ItineraryItem(
              itinerary = itinerary,
              onItineraryClick = { /* TODO: Implement navigation to itinerary details */})
        }
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
          Modifier.fillMaxWidth()
              .clickable { onItineraryClick(itinerary.id) }
              .padding(16.dp)
              .testTag("ItineraryItem")) {
        Text(text = itinerary.title, style = MaterialTheme.typography.bodyMedium)
      }
}
