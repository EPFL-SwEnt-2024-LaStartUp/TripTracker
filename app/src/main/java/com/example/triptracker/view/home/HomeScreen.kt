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
            if (isSearchActive) {
              FilterDropdownMenu(
                  selectedFilterType = selectedFilterType,
                  showFilterDropdown = remember { mutableStateOf(false) },
                  homeViewModel = homeViewModel)
            }
          },
          bottomBar = { NavigationBar(navigation = navigation) },
          modifier = Modifier.fillMaxWidth().testTag("HomeScreen")) {
            when (val itineraries = homeViewModel.itineraryList.value ?: emptyList()) {
              emptyList<Itinerary>() -> NoItinerariesMessage()
              else -> {
                if (!test) {
                  HomePager(navigation = navigation, homeViewModel = homeViewModel, test = test)
                } else {
                  DisplayItineraries(
                      itineraries = itineraries,
                      navigation = navigation,
                      homeViewModel = homeViewModel,
                      test = test,
                      tabSelected = HomeCategory.TRENDING)
                }
              }
            }
          }
    }
  }
  Log.d("HomeScreen", "Rendering HomeScreen")
}

@Composable
fun NoItinerariesMessage() {
  Text(
      text = "You do not have any itineraries yet.",
      modifier = Modifier.padding(10.dp).testTag("NoItinerariesText"),
      fontSize = 1.sp)
}

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
  var isActive by remember { mutableStateOf(false) }
  val placeholderText = getPlaceholderText(selectedFilterType)

  BackHandler { onBackClicked() }

  Box(modifier = Modifier.fillMaxWidth()) {
    SearchBar(
        modifier =
            Modifier.fillMaxWidth()
                .padding(horizontal = 17.dp, vertical = 5.dp)
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
                            onBackClicked()
                          } else {
                            searchText = ""
                            viewModel.setSearchQuery(searchText)
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
          if (!activeState) {
            searchText = ""
            viewModel.setSearchQuery("")
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
      DisplaySearchResults(isActive, items, viewModel, navigation)
    }
  }
}

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
            modifier = Modifier.padding(10.dp).width(400.dp).testTag("DropDownFilter")) {
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

fun getPlaceholderText(selectedFilterType: FilterType): String {
  return when (selectedFilterType) {
    FilterType.FLAME -> "Example: <500"
    FilterType.PIN -> "Example: EPFL"
    FilterType.TITLE -> "Find Itineraries"
    FilterType.USERNAME -> "Search for a User"
    FilterType.FAVOURITES -> "Find Favourites"
  }
}

@Composable
fun DisplayItineraries(
    itineraries: List<Itinerary>,
    navigation: Navigation,
    homeViewModel: HomeViewModel,
    test: Boolean = false,
    tabSelected: HomeCategory = HomeCategory.TRENDING,
) {
  val goodPadding =
      if (test) PaddingValues(0.dp, 50.dp, 0.dp, 70.dp) else PaddingValues(0.dp, 0.dp, 0.dp, 70.dp)
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
          if (tabSelected == HomeCategory.TRENDING) {
            homeViewModel.filterByTrending()
          } else {
            homeViewModel.filterByFollowing(usermail)
          }
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

  Column(modifier = Modifier.background(md_theme_light_onPrimary)) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        backgroundColor = MaterialTheme.colorScheme.background) {
          tabs.forEachIndexed { index, title ->
            val flower =
                if (ambientProfile.userProfile.value.flowerMode == 1) {
                  if (title == HomeCategory.FOLLOWING.name) " \uD83C\uDF38" else " \uD83C\uDF37"
                } else ""
            val isSelected = index == selectedTab
            Tab(selected = isSelected, onClick = { selectedTab = index }) {
              Text(
                  "$title$flower",
                  color =
                      if (isSelected) MaterialTheme.colorScheme.onBackground
                      else MaterialTheme.colorScheme.onSurface,
                  fontFamily = Montserrat,
                  fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                  fontSize = 20.sp)
            }
          }
        }

    HorizontalPager(
        state = pagerState,
        modifier =
            Modifier.testTag("HomePager").background(MaterialTheme.colorScheme.background)) { page
          ->
          val itineraries =
              when (HomeCategory.entries[page]) {
                HomeCategory.TRENDING ->
                    homeViewModel.trendingList.observeAsState(emptyList()).value
                HomeCategory.FOLLOWING ->
                    homeViewModel.followingList.observeAsState(emptyList()).value
              }

          if (itineraries.isEmpty() && HomeCategory.entries[page] == HomeCategory.FOLLOWING) {
            Text(
                text = "Not following anyone yet.",
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(start = 70.dp, bottom = 250.dp)
                        .testTag("NoFollowingText"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)))
          } else {
            DisplayItineraries(
                itineraries =
                    itineraries.filter {
                      val ownerProfile =
                          allProfilesFetched.find { profile -> profile.mail == it.userMail }
                      ownerProfile?.let { profile ->
                        profile.itineraryPrivacy == 0 ||
                            (profile.itineraryPrivacy == 1 &&
                                ambientProfile.userProfile.value.followers.contains(profile.mail) &&
                                ambientProfile.userProfile.value.following.contains(profile.mail))
                      } ?: false
                    },
                navigation = navigation,
                homeViewModel = homeViewModel,
                test = test,
                tabSelected = HomeCategory.entries[page])
          }
        }
  }
}

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

    LaunchedEffect(isRefreshing) {
      if (isRefreshing) {
        pullToRefreshState.startRefresh()
      } else {
        Log.e("MAMAMIA", "endRefresh")
        pullToRefreshState.endRefresh()
      }
    }

    PullToRefreshContainer(
        state = pullToRefreshState,
        modifier = Modifier.align(Alignment.TopCenter).offset(y = (-40).dp).testTag("Refreshing"),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground)
  }
}
