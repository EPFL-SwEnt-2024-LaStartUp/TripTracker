package com.example.triptracker.view.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeCategory
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel

var allProfilesFetched: List<UserProfile> = emptyList()

/**
 * Composable function to display the home screen. The home screen displays a list of itineraries
 * that the user can scroll through. The user can also search for itineraries using the search bar
 * at the top of the screen. The user can also filter the itineraries by title, username, flame
 * count or favourites. The user can also switch between the trending and following categories using
 * the tabs at the top of the screen. Can also swipe left or right to switch between the two
 * categories. The user can also click on an itinerary to view the itinerary details.
 *
 * @param navigation the navigation object to use for navigation
 * @param homeViewModel the view model to use for fetching itineraries
 * @param userProfileViewModel the view model to use for fetching users
 * @param test the boolean to test the function (default is false)
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
    allProfiles = fetch
    readyToDisplay = true
  }
  when (readyToDisplay || test) {
    false -> {
      Log.d("UserProfileList", "User profile list is null")
    }
    true -> {
      allProfilesFetched = allProfiles
      val selectedFilterType by homeViewModel.selectedFilter.observeAsState(FilterType.TITLE)
      val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())
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
              SearchBarImplementation(
                  onBackClicked = { navigation.goBack() },
                  viewModel = homeViewModel,
                  onSearchActivated = { isActive -> isSearchActive = isActive },
                  navigation = navigation,
                  selectedFilterType = selectedFilterType,
                  isNoResultFound = isNoResultFound)
            }
            DisplayDropDownIfActive(
                isSearchActive = isSearchActive,
                selectedFilterType = selectedFilterType,
                homeViewModel = homeViewModel)
          },
          bottomBar = { NavigationBar(navigation = navigation) },
          modifier = Modifier.fillMaxWidth().testTag("HomeScreen")) {
            when (val itineraries = homeViewModel.itineraryList.value ?: emptyList()) {
              emptyList<Itinerary>() -> NoItinerariesMessage()
              else -> {
                DisplayContent(
                    itineraries = itineraries,
                    navigation = navigation,
                    homeViewModel = homeViewModel,
                    userProfileViewModel = userProfileViewModel,
                    test = test)
              }
            }
          }
    }
  }
  Log.d("HomeScreen", "Rendering HomeScreen")
}

/** Composable function to display the content of the home screen depending on the test boolean. */
@Composable
fun DisplayContent(
    itineraries: List<Itinerary>,
    navigation: Navigation,
    homeViewModel: HomeViewModel,
    userProfileViewModel: UserProfileViewModel,
    test: Boolean
) {
  if (!test) {
    TabsAndPager(navigation = navigation, homeViewModel = homeViewModel)
  } else {
    DisplayItineraries(
        itineraries = itineraries,
        navigation = navigation,
        homeViewModel = homeViewModel,
        test = test,
        tabSelected = HomeCategory.TRENDING)
  }
}

/** Displays a message when the user does not have any itineraries yet. */
@Composable
fun NoItinerariesMessage() {
  Text(
      text = "You do not have any itineraries yet.",
      modifier = Modifier.padding(10.dp).testTag("NoItinerariesText"),
      fontSize = 1.sp)
}

/**
 * Displays the search bar at the top of the screen
 *
 * @param onBackClicked: Function to call when the back button is clicked
 * @param onSearchActivated: Function to call when the search bar is activated
 * @param viewModel: HomeViewModel to use for searching itineraries
 * @param selectedFilterType: FilterType to use for filtering itineraries
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
  var searchText = remember { mutableStateOf("") }
  val items = viewModel.filteredItineraryList.value ?: listOf()
  val focusManager = LocalFocusManager.current
  var isActive by remember { mutableStateOf(false) }
  val placeholderText = getPlaceholderText(selectedFilterType)

  BackHandler { onBackClicked() }

  Box(modifier = Modifier.fillMaxWidth()) {
    SearchBar(
        modifier =
            Modifier.fillMaxWidth()
                .padding(horizontal = 17.dp, vertical = 5.dp)
                .testTag("searchBar"),
        query = searchText.value,
        onQueryChange = { newText ->
          searchText.value = newText
          viewModel.setSearchQuery(newText)
          onSearchActivated(isActive)
          isActive = newText.isNotEmpty() || focusManager.equals(true)
        },
        onSearch = {
          viewModel.setSearchQuery(searchText.value)
          isActive = false
        },
        leadingIcon = { DisplayLeadingIcon(isActive, onBackClicked) },
        trailingIcon = {
          DisplayTrailingIcon(isActive, onBackClicked, viewModel, searchText, onSearchActivated)
        },
        active = isActive,
        onActiveChange = { activeState ->
          isActive = activeState
          resetIfNotActive(activeState, searchText, viewModel, onSearchActivated)
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

      DisplayNoResultFount(isNoResultFound)

      DisplaySearchResults(isActive, items, viewModel, navigation)
    }
  }
}

/**
 * Displays the leading icon in the search bar
 *
 * @param isActive: Boolean to indicate if the search bar is active
 * @param onBackClicked: Function to call when the back button is clicked
 */
@Composable
fun DisplayLeadingIcon(isActive: Boolean, onBackClicked: () -> Unit) {
  if (isActive) {
    Icon(
        modifier = Modifier.clickable { onBackClicked() }.testTag("BackButton"),
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Back")
  } else {
    Icon(imageVector = Icons.Default.Search, contentDescription = "Menu")
  }
}

/**
 * Displays the trailing icon in the search bar
 *
 * @param isActive: Boolean to indicate if the search bar is active
 * @param onBackClicked: Function to call when the back button is clicked
 * @param viewModel: HomeViewModel to use for searching itineraries
 * @param searchText: MutableState to store the search text
 * @param onSearchActivated: Function to call when the search bar is activated
 */
@Composable
fun DisplayTrailingIcon(
    isActive: Boolean,
    onBackClicked: () -> Unit,
    viewModel: HomeViewModel,
    searchText: MutableState<String>,
    onSearchActivated: (Boolean) -> Unit
) {
  if (isActive) {
    Icon(
        modifier =
            Modifier.clickable {
                  if (searchText.value.isEmpty()) {
                    onBackClicked()
                  } else {
                    searchText.value = ""
                    viewModel.setSearchQuery(searchText.value)
                  }
                  onSearchActivated(isActive)
                }
                .testTag("ClearButton"),
        imageVector = Icons.Default.Close,
        contentDescription = "Clear text field")
  }
}

/**
 * Resets the search bar text and searchQuery if it is not active
 *
 * @param activeState: Boolean to indicate if the search bar is active
 * @param searchText: MutableState to store the search text
 * @param viewModel: HomeViewModel to use for searching itineraries
 * @param onSearchActivated: Function to call when the search bar is activated
 */
fun resetIfNotActive(
    activeState: Boolean,
    searchText: MutableState<String>,
    viewModel: HomeViewModel,
    onSearchActivated: (Boolean) -> Unit
) {
  if (!activeState) {
    searchText.value = ""
    viewModel.setSearchQuery("")
    onSearchActivated(false)
  }
}

/**
 * Displays a message when no results are found
 *
 * @param isNoResultFound: Boolean to indicate if no results were found
 */
@Composable
fun DisplayNoResultFount(isNoResultFound: Boolean) {
  val horizontalPlacement = LocalConfiguration.current.screenWidthDp * 0.6f
  val verticalPlacement = LocalConfiguration.current.screenHeightDp * 0.024f
  if (isNoResultFound) {
    Text(
        "No results found",
        modifier =
            Modifier.padding((horizontalPlacement / 2).dp, verticalPlacement.dp, 0.dp, 0.dp)
                .testTag("NoResultsFound"),
        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.15.sp,
        color = Color.Red)
  }
}

/**
 * Displays the search results
 *
 * @param isActive: Boolean to indicate if the search bar is active
 * @param items: List of itineraries to display
 * @param viewModel: HomeViewModel to use for searching itineraries
 * @param navigation: Navigation object to use for navigation
 */
@Composable
fun DisplaySearchResults(
    isActive: Boolean,
    items: List<Itinerary>,
    viewModel: HomeViewModel,
    navigation: Navigation
) {
  val currProfile = AmbientUserProfile.current.userProfile.value
  var listToShow = if (isActive) items else viewModel.itineraryList.value ?: listOf()
  LazyColumn {
    listToShow =
        listToShow.filter {
          val itin = it
          val ownerProfile = allProfilesFetched.find { it.mail == itin.userMail }
          shouldDisplayItinerary(ownerProfile, currProfile)
        }
    items(listToShow) { itinerary ->
      ItineraryItem(
          itinerary = itinerary,
          onItineraryClick = { navigation.navigateTo(Route.MAPS, itinerary.id) })
    }
  }
}

/**
 * Function to check if the itinerary should be displayed based on the owner's profile and the
 * current user's profile
 *
 * @param ownerProfile: UserProfile of the owner of the itinerary
 * @param currProfile: UserProfile of the current user
 * @return Boolean to indicate if the itinerary should be displayed
 */
fun shouldDisplayItinerary(ownerProfile: UserProfile?, currProfile: UserProfile): Boolean {
  return when (ownerProfile) {
    null -> false
    else -> {
      ownerProfile.itineraryPrivacy == 0 || ownerProfile == currProfile ||
          (ownerProfile.itineraryPrivacy == 1 &&
              currProfile.followers.contains(ownerProfile.mail) &&
              currProfile.following.contains(ownerProfile.mail))
    }
  }
}

/**
 * Composable function to display a dropdown menu for filtering itineraries
 *
 * @param selectedFilterType: FilterType to use for filtering itineraries
 * @param showFilterDropdown: MutableState to indicate if the dropdown menu is shown
 * @param homeViewModel: HomeViewModel to use for fetching itineraries
 * @return DropdownMenu to display the filter dropdown menu
 */
@Composable
fun FilterDropdownMenu(
    selectedFilterType: FilterType,
    showFilterDropdown: MutableState<Boolean>,
    homeViewModel: HomeViewModel
) {
  val horizontalPlacement = LocalConfiguration.current.screenWidthDp * 0.6f
  val verticalPlacement = LocalConfiguration.current.screenHeightDp * 0.024f
  Box(
      modifier =
          Modifier.padding(PaddingValues(horizontalPlacement.dp, verticalPlacement.dp, 0.dp, 0.dp))
              .fillMaxWidth()
              .fillMaxHeight()
              .testTag("DropDownBox")) {
        DropdownMenu(
            expanded = showFilterDropdown.value,
            onDismissRequest = { showFilterDropdown.value = false },
            modifier = Modifier.width(400.dp).testTag("DropDownFilter")) {
              FilterType.entries.forEach { filterType ->
                DropdownMenuItem(
                    text = {
                      Text(
                          text = filterType.name.replace('_', ' '),
                          modifier = Modifier.testTag("FilterText"))
                    },
                    onClick = {
                      homeViewModel.setSearchFilter(filterType)
                      showFilterDropdown.value = false
                    })
              }
            }
        Text(
            text = selectedFilterType.name,
            modifier =
                Modifier.clickable { showFilterDropdown.value = true }
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
            fontSize = 12.sp,
        )
      }
}

/**
 * Function to get the placeholder text for the search bar based on the selected filter type
 *
 * @param selectedFilterType: FilterType to use for filtering itineraries
 * @return the placeholder text to display in the search bar
 */
fun getPlaceholderText(selectedFilterType: FilterType): String {
  return when (selectedFilterType) {
    FilterType.FLAME -> "Example: <500"
    FilterType.PIN -> "Example: EPFL"
    FilterType.TITLE -> "Find Itineraries"
    FilterType.USERNAME -> "Search for a User"
    FilterType.FAVOURITES -> "Find Favourites"
  }
}

/**
 * Composable function to display a list of itineraries
 *
 * @param itineraries: List of itineraries to display
 * @param navigation: Navigation object to use for navigation
 * @param homeViewModel: HomeViewModel to use for fetching itineraries
 * @param test: Boolean to test the function (default is false)
 * @param tabSelected: HomeCategory to use for filtering itineraries (default is TRENDING)
 * @param userProfileViewModel: UserProfileViewModel to use for fetching users (default is
 *   viewModel())
 */
@Composable
fun DisplayItineraries(
    itineraries: List<Itinerary>,
    navigation: Navigation,
    homeViewModel: HomeViewModel,
    test: Boolean = false,
    tabSelected: HomeCategory = HomeCategory.TRENDING,
    userProfileViewModel: UserProfileViewModel = viewModel()
) {
  val goodPadding =
      if (test) PaddingValues(0.dp, 50.dp, 0.dp, 70.dp) else PaddingValues(0.dp, 10.dp, 0.dp, 70.dp)
  val usermail = AmbientUserProfile.current.userProfile.value.mail
  val isRefreshing = remember { mutableStateOf(false) }
  PullToRefreshLazyColumn(
      modifier =
          Modifier.fillMaxSize()
              .padding(goodPadding)
              .testTag("ItineraryList")
              .background(color = MaterialTheme.colorScheme.background),
      items = itineraries,
      isRefreshing = isRefreshing.value,
      onRefresh = {
        isRefreshing.value = true
        homeViewModel.fetchItineraries {
          isRefreshing.value = false
          filterByTabSelected(tabSelected, homeViewModel, usermail, userProfileViewModel)
        }
      },
      content = { itinerary ->
        DisplayItinerary(
            itinerary = itinerary,
            onClick = {
              navigation.navigateTo(Route.MAPS, itinerary.id)
              homeViewModel.incrementClickCount(itinerary.id)
            },
            displayImage = true,
            navigation = navigation)
      })
}

/**
 * Function to filter itineraries based on the selected tab
 *
 * @param tabSelected: HomeCategory to use for filtering itineraries
 * @param homeViewModel: HomeViewModel to use for fetching itineraries
 * @param usermail: String to use for filtering itineraries
 * @param userProfileViewModel: UserProfileViewModel to use for fetching users
 */
fun filterByTabSelected(
    tabSelected: HomeCategory,
    homeViewModel: HomeViewModel,
    usermail: String,
    userProfileViewModel: UserProfileViewModel
) {
  if (tabSelected == HomeCategory.TRENDING) {
    homeViewModel.filterByTrending()
  } else {
    userProfileViewModel.fetchAllUserProfiles { profiles -> allProfilesFetched = profiles }
    homeViewModel.filterByFollowing(usermail)
  }
}

/**
 * Represents the tabs and pager for the home screen. Contains two tabs that can be clicked to
 * switch between the trending and following categories. Or you can swipe left or right to switch
 * between the two categories (Trending and Following.
 *
 * @param navigation the navigation object to use for navigation
 * @param homeViewModel the view model to use for fetching itineraries
 * @param test the boolean to test the function (default is false)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsAndPager(
    navigation: Navigation,
    homeViewModel: HomeViewModel = viewModel(),
    test: Boolean = false
) {
  val ambientProfile = AmbientUserProfile.current
  val userEmail = ambientProfile.userProfile.value.mail
  val tabs = listOf(HomeCategory.TRENDING.name, HomeCategory.FOLLOWING.name)
  val numTabs = tabs.size
  val pagerState = rememberPagerState(initialPage = 0) { numTabs }
  var selectedTab by remember { mutableStateOf(pagerState.currentPage) }

  LaunchedEffect(selectedTab) {
    pagerState.animateScrollToPage(page = selectedTab)
    when (HomeCategory.entries[selectedTab]) {
      HomeCategory.TRENDING -> homeViewModel.filterByTrending()
      HomeCategory.FOLLOWING -> homeViewModel.filterByFollowing(userEmail)
    }
  }

  LaunchedEffect(pagerState.currentPage) {
    selectedTab = pagerState.currentPage
    when (HomeCategory.entries[pagerState.currentPage]) {
      HomeCategory.TRENDING -> homeViewModel.filterByTrending()
      HomeCategory.FOLLOWING -> homeViewModel.filterByFollowing(userEmail)
    }
  }

  val verticalPlacement = LocalConfiguration.current.screenHeightDp * 0.09f
  Column(
      modifier =
          Modifier.background(md_theme_light_onPrimary)
              .padding(0.dp, verticalPlacement.dp, 0.dp, 0.dp)) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            backgroundColor = MaterialTheme.colorScheme.background) {
              tabs.forEachIndexed { index, title ->
                val flower = getFlower(ambientProfile, title)
                val isSelected = index == selectedTab
                Tab(selected = isSelected, onClick = { selectedTab = index }) {
                  Text(
                      "$title$flower",
                      color = getColor(isSelected),
                      fontFamily = Montserrat,
                      fontWeight = getFontWeight(isSelected),
                      fontSize = 20.sp)
                }
              }
            }

        HorizontalPager(
            state = pagerState,
            modifier =
                Modifier.testTag("HomePager").background(MaterialTheme.colorScheme.background)) {
                page ->
              val itineraries = itinerariesForPage(page, homeViewModel)
              DisplayPagerContent(
                  itineraries = itineraries,
                  navigation = navigation,
                  homeViewModel = homeViewModel,
                  verticalPlacement = verticalPlacement,
                  ambientProfile = ambientProfile,
                  test = test,
                  page = page)
            }
      }
}

/**
 * Function to display the content of the pager based on the current page
 *
 * @param itineraries: List of itineraries to display
 * @param navigation: Navigation object to use for navigation
 * @param homeViewModel: HomeViewModel to use for fetching itineraries
 * @param verticalPlacement: Float to use for vertical placement
 * @param ambientProfile: MutableUserProfile to use for fetching user profile
 * @param test: Boolean to test the function
 * @param page: Int to use for the current page
 * @return the content to display in the pager
 */
@Composable
fun DisplayPagerContent(
    itineraries: List<Itinerary>,
    navigation: Navigation,
    homeViewModel: HomeViewModel,
    verticalPlacement: Float,
    ambientProfile: MutableUserProfile,
    test: Boolean,
    page: Int
) {
  if (checkIfFollowingCategory(itineraries, page)) {
    NotFollowingText(itineraries, page, verticalPlacement)
  } else {
    DisplayItineraries(
        itineraries =
            itineraries.filter {
              val ownerProfile = allProfilesFetched.find { profile -> profile.mail == it.userMail }
              shouldDisplayItinerary(ownerProfile, ambientProfile.userProfile.value)
            },
        navigation = navigation,
        homeViewModel = homeViewModel,
        test = test,
        tabSelected = HomeCategory.entries[page])
  }
}

/**
 * Function to get the flower emoji based on the user's flower mode
 *
 * @param ambientProfile: MutableUserProfile to use for fetching user profile
 * @param title: String to use for the title
 * @return the flower emoji to display, if any
 */
fun getFlower(ambientProfile: MutableUserProfile, title: String): String {
  val flowerStr =
      if (ambientProfile.userProfile.value.flowerMode == 1) {
        if (title == HomeCategory.FOLLOWING.name) " \uD83C\uDF38" else " \uD83C\uDF37"
      } else ""
  return flowerStr
}

/** Function to get the color based on if the tab is selected */
@Composable
fun getColor(isSelected: Boolean): Color {
  return if (isSelected) MaterialTheme.colorScheme.onBackground
  else MaterialTheme.colorScheme.onSurface
}

/** Function to get the font weight based on if the tab is selected */
@Composable
fun getFontWeight(isSelected: Boolean): FontWeight {
  return if (isSelected) FontWeight.SemiBold else FontWeight.Normal
}

/**
 * Function to get the itineraries for the current page.
 *
 * @param page the current page index
 * @param homeViewModel the view model to get the itineraries
 * @return the list of itineraries for the current page
 */
@Composable
fun itinerariesForPage(
    page: Int,
    homeViewModel: HomeViewModel,
): List<Itinerary> {
  return when (HomeCategory.entries[page]) {
    HomeCategory.TRENDING -> homeViewModel.trendingList.observeAsState(emptyList()).value
    HomeCategory.FOLLOWING -> homeViewModel.followingList.observeAsState(emptyList()).value
  }
}

// Helper function to check if the current category is the following category
fun checkIfFollowingCategory(itineraries: List<Itinerary>, page: Int): Boolean {
  return itineraries.isEmpty() && HomeCategory.entries[page] == HomeCategory.FOLLOWING
}

/**
 * Function to display a text message when the user is not following anyone yet.
 *
 * @param itineraries list of itineraries to check if it is empty
 * @param page the current page index
 * @param verticalPlacement the vertical placement of the text
 */
@Composable
fun NotFollowingText(itineraries: List<Itinerary>, page: Int, verticalPlacement: Float) {
  if (itineraries.isEmpty() && HomeCategory.entries[page] == HomeCategory.FOLLOWING) {
    Text(
        text = "Not following anyone yet.",
        modifier =
            Modifier.fillMaxWidth()
                .padding(
                    start = 70.dp,
                    top = (verticalPlacement * 3).dp,
                    bottom = (verticalPlacement * 5.3).dp)
                .testTag("NoFollowingText"),
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp,
        color = MaterialTheme.colorScheme.onBackground,
        fontFamily = FontFamily(Font(R.font.montserrat_regular)))
  }
}

/**
 * Function to display drop down menu if the search bar is active.
 *
 * @param isSearchActive: Boolean to indicate if the search bar is active
 * @param selectedFilterType: FilterType to use for filtering itineraries
 * @param homeViewModel: HomeViewModel to use for fetching itineraries
 */
@Composable
fun DisplayDropDownIfActive(
    isSearchActive: Boolean,
    selectedFilterType: FilterType,
    homeViewModel: HomeViewModel
) {
  if (isSearchActive) {
    FilterDropdownMenu(
        selectedFilterType = selectedFilterType,
        showFilterDropdown = remember { mutableStateOf(false) },
        homeViewModel = homeViewModel)
  }
}

/**
 * Function to display an itinerary item in search mode.
 *
 * @param itinerary the itinerary to display
 * @param onItineraryClick the function to call when the itinerary is clicked
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

/**
 * Function to enable pull to refresh in a LazyColumn.
 *
 * @param items the list of items to display
 * @param content the content to display
 * @param isRefreshing the boolean to indicate if the list is refreshing
 * @param onRefresh the function to call when the list is refreshing
 * @param modifier the modifier to apply to the LazyColumn
 * @param lazyListState the state of the LazyColumn
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> PullToRefreshLazyColumn(
    items: List<T>,
    content: @Composable (T) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
  val pullToRefreshState = rememberPullToRefreshState()
  Box(modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
          items(items) { content(it) }
        }

    if (pullToRefreshState.isRefreshing) {
      LaunchedEffect(true) { onRefresh() }
    }

    LaunchedEffect(isRefreshing) { updateRefreshState(isRefreshing, pullToRefreshState) }

    PullToRefreshContainer(
        state = pullToRefreshState,
        modifier = Modifier.align(Alignment.TopCenter).offset(y = (-40).dp).testTag("Refreshing"),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground)
  }
}

/**
 * Function to update the refresh state of the pull to refresh container.
 *
 * @param isRefreshing the boolean to indicate if the list is refreshing
 * @param pullToRefreshState the state of the pull to refresh container
 */
@OptIn(ExperimentalMaterial3Api::class)
fun updateRefreshState(isRefreshing: Boolean, pullToRefreshState: PullToRefreshState) {
  if (isRefreshing) {
    pullToRefreshState.startRefresh()
  } else {
    pullToRefreshState.endRefresh()
  }
}
