package com.example.triptracker.view.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.triptracker.model.profile.AmbientUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeCategory
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel

var allProfilesFetched: List<UserProfile> = emptyList()

/**
 * HomeScreen composable that displays the list of itineraries
 *
 * @param navigation: Navigation object to use for navigation
 * @param homeViewModel: HomeViewModel to use for fetching itineraries
 * @param userProfileViewModel: UserProfileViewModel to use for fetching users
 * @param test: Boolean to test the function
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigation: Navigation,
    homeViewModel: HomeViewModel = viewModel(),
    userProfileViewModel: UserProfileViewModel = viewModel(),
    test: Boolean = false
) {
  var readyToDisplay by remember { mutableStateOf(false) }
  var allProfiles by remember { mutableStateOf(emptyList<UserProfile>()) }
  userProfileViewModel.fetchAllUserProfiles() { fetch ->
    if (fetch != null) {
      allProfiles = fetch
      readyToDisplay = true
    }
  }
  when (readyToDisplay || test) {
    false -> {
      Log.d("UserProfileList", "User profile list is null")
    }
    true -> {
      val selectedFilterType by homeViewModel.selectedFilter.observeAsState(FilterType.TITLE)
      allProfilesFetched = allProfiles
      // Filtered list of itineraries based on search query
      val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())
      var showFilterDropdown by remember { mutableStateOf(false) }
      var isSearchActive by remember { mutableStateOf(false) }
      val isNoResultFound =
          remember(filteredList, isSearchActive) {
            isSearchActive &&
                filteredList.isEmpty() &&
                homeViewModel.searchQuery.value!!.isNotEmpty()
          }

      Scaffold(
          topBar = {
            Column {
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
            }
            if (isSearchActive) {
              Box(
                  modifier =
                      Modifier.padding(290.dp, 25.dp, 25.dp, 235.dp)
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
          bottomBar = { NavigationBar(navigation = navigation) },
          modifier = Modifier.fillMaxWidth().testTag("HomeScreen")) {
            when (val itineraries = homeViewModel.itineraryList.value ?: emptyList()) {
              emptyList<Itinerary>() -> {
                Text(
                    text = "You do not have any itineraries yet.",
                    modifier = Modifier.padding(10.dp).testTag("NoItinerariesText"),
                    fontSize = 1.sp)
              }
              else -> {
                if (!test) {
                  Column(
                      modifier =
                          Modifier.fillMaxSize()
                              .padding(
                                  PaddingValues(
                                      0.dp,
                                      80.dp,
                                      0.dp,
                                      0.dp)) // this ensures having a padding at the top
                      ) {
                        // will display the list of itineraries
                        HomePager(
                            navigation = navigation, homeViewModel = homeViewModel, test = test)
                      }

                  // will display the list of itineraries

                  /*
                  TODO can be used to scroll to the top of the list
                  val showButton = listState.firstVisibleItemIndex > 0
                  AnimatedVisibility(visible = showButton) {
                      ScrollToTopButton()
                  }
                   */
                } else {
                  DisplayItineraries(
                      itineraries = itineraries,
                      navigation = navigation,
                      homeViewModel = homeViewModel,
                      test = test)
                }
              }
            }
          }
    }
  }
  Log.d("HomeScreen", "Rendering HomeScreen")
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
    viewModel: HomeViewModel,
    selectedFilterType: FilterType,
    isNoResultFound: Boolean = false,
    navigation: Navigation
) {
  val currProfile = AmbientUserProfile.current.userProfile.value
  var searchText by remember { mutableStateOf("") }
  val items = viewModel.filteredItineraryList.value ?: listOf()
  val focusManager = LocalFocusManager.current
  // If the search bar is active (in focus or contains text), we'll consider it active.
  var isActive by remember { mutableStateOf(false) }
  // Update the placeholder text based on the selected filter type
  val placeholderText =
      remember(selectedFilterType) {
        when (selectedFilterType) {
          FilterType.FLAME -> "Example: <500"
          FilterType.PIN -> "Example: EPFL"
          FilterType.TITLE -> "Find Itineraries"
          FilterType.USERNAME -> "Search for a User"
          FilterType.FAVOURITES -> "Find Favourites"
        }
      }

  // fixes the back button showing weird display
  BackHandler { onBackClicked() }

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
            Icon(
                modifier = Modifier.clickable { onBackClicked() }.testTag("BackButton"),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back")
          } else {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Menu")
          }
        },
        trailingIcon = {
          if (isActive) {
            Icon(
                modifier =
                    Modifier.clickable {
                          if (searchText.isEmpty()) {
                            // if click on clear button when text is empty, go back to home screen
                            onBackClicked()
                          } else {
                            // only clear
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
            onSearchActivated(false)
          }
        },
        placeholder = {
          Text(
              placeholderText,
              modifier = Modifier.padding(start = 1.dp).testTag("searchBarText"),
              textAlign = TextAlign.Center,
              fontFamily = FontFamily(Font(R.font.montserrat_bold)),
              fontSize = 20.sp,
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
      var listToShow = if (isActive) items else viewModel.itineraryList.value ?: listOf()
      LazyColumn {
        listToShow =
            listToShow.filter {
              val itin = it
              val ownerProfile = allProfilesFetched.find { it.mail == itin.userMail }
              if (ownerProfile != null) {
                ownerProfile.itineraryPrivacy == 0 ||
                    (ownerProfile.itineraryPrivacy == 1 &&
                        currProfile.followers.contains(ownerProfile.mail) &&
                        currProfile.following.contains(ownerProfile.mail))
              } else {
                false
              }
            }
        items(listToShow) { itinerary ->
          ItineraryItem(
              itinerary = itinerary,
              onItineraryClick = { navigation.navigateTo(Route.MAPS, itinerary.id) })
        }
      }
    }
  }
}

@Composable
fun DisplayItineraries(
    itineraries: List<Itinerary>,
    navigation: Navigation,
    homeViewModel: HomeViewModel,
    test: Boolean = false
) {
  var goodPadding = PaddingValues(0.dp, 0.dp, 0.dp, 70.dp)
  if (test) {
    goodPadding = PaddingValues(0.dp, 50.dp, 0.dp, 70.dp)
  }
  LazyColumn(
      modifier =
          Modifier.fillMaxSize()
              .padding(goodPadding) // this ensures having a padding at the bottom
              .testTag("ItineraryList"),
      contentPadding = PaddingValues(16.dp)) {
        items(itineraries) { itinerary ->
          Log.d("ItineraryToDisplay", "Displaying itinerary: $itinerary")
          DisplayItinerary(
              itinerary = itinerary,
              onClick = {
                navigation.navigateTo(Route.MAPS, itinerary.id)
                homeViewModel.incrementClickCount(itinerary.id)
              },
              displayImage = true,
              navigation = navigation)
        }
      }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePager(
    navigation: Navigation,
    homeViewModel: HomeViewModel = viewModel(),
    test: Boolean = false
) {
  val ambientProfile = AmbientUserProfile.current
  val userEmail = ambientProfile.userProfile.value.mail
  val tabs = listOf(HomeCategory.TRENDING.name, HomeCategory.FOLLOWING.name)
  val numTabs = tabs.size
  val pagerState =
      rememberPagerState(initialPage = 0) {
        numTabs // initial page is 0, trending tab
      }
  var selectedTab by remember { mutableStateOf(pagerState.currentPage) }
  var isSelected by remember { mutableStateOf(false) }

  // added for page animation, if the animation is not smooth, change to pagerState.scrollToPage
  LaunchedEffect(key1 = selectedTab) {
    pagerState.animateScrollToPage(page = selectedTab)
    when (HomeCategory.entries[selectedTab]) {
      HomeCategory.TRENDING -> homeViewModel.filterByTrending()
      HomeCategory.FOLLOWING -> homeViewModel.filterByFollowing(userEmail)
    }
  }

  // LaunchedEffect to synchronize the pager state with the selected tab
  LaunchedEffect(pagerState.currentPage) {
    selectedTab = pagerState.currentPage
    when (HomeCategory.entries[pagerState.currentPage]) {
      HomeCategory.TRENDING -> homeViewModel.filterByTrending()
      HomeCategory.FOLLOWING -> homeViewModel.filterByFollowing(userEmail)
    }
  }

  Column(modifier = Modifier.background(md_theme_light_onPrimary)) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        backgroundColor = md_theme_light_onPrimary) {
          tabs.forEachIndexed { index, title ->
            isSelected = index == selectedTab
            Tab(selected = isSelected, onClick = { selectedTab = index }) {
              Text(
                  title,
                  color = if (isSelected) md_theme_light_black else md_theme_grey,
                  fontFamily = Montserrat,
                  fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                  fontSize = 20.sp) // same size as itinerary title
            }
          }
        }

    HorizontalPager(state = pagerState, modifier = Modifier.testTag("HomePager")) { page ->
      when (HomeCategory.entries[page]) {
        HomeCategory.TRENDING -> {
          val trendingItineraries by homeViewModel.trendingList.observeAsState(emptyList())

          DisplayItineraries(
              itineraries =
                  trendingItineraries.filter {
                    val itin = it
                    val ownerProfile = allProfilesFetched.find { it.mail == itin.userMail }
                    if (ownerProfile != null) {
                      ownerProfile.itineraryPrivacy == 0 ||
                          (ownerProfile.itineraryPrivacy == 1 &&
                              ambientProfile.userProfile.value.followers.contains(
                                  ownerProfile.mail) &&
                              ambientProfile.userProfile.value.following.contains(
                                  ownerProfile.mail))
                    } else {
                      false
                    }
                  },
              navigation = navigation,
              homeViewModel = homeViewModel,
              test = test)
        }
        HomeCategory.FOLLOWING -> {
          val followingItineraries by homeViewModel.followingList.observeAsState(emptyList())
          if (followingItineraries.isEmpty()) {
            Text(
                text = "Not following anyone yet.",
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(start = 70.dp, bottom = 250.dp)
                        .testTag("NoFollowingText"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp,
                color = md_theme_light_black,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)))
          }
          DisplayItineraries(
              itineraries =
                  followingItineraries.filter {
                    val itin = it
                    val ownerProfile = allProfilesFetched.find { it.mail == itin.userMail }
                    if (ownerProfile != null) {
                      ownerProfile.itineraryPrivacy == 0 ||
                          (ownerProfile.itineraryPrivacy == 1 &&
                              ambientProfile.userProfile.value.followers.contains(
                                  ownerProfile.mail) &&
                              ambientProfile.userProfile.value.following.contains(
                                  ownerProfile.mail))
                    } else {
                      false
                    }
                  },
              navigation = navigation,
              homeViewModel = homeViewModel,
              test = test)
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
