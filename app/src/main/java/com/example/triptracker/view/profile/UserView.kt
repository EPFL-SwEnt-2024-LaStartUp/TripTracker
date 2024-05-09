package com.example.triptracker.view.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.WaitingScreen
import com.example.triptracker.view.home.DisplayItinerary
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.FilterType
import com.example.triptracker.viewmodel.HomeViewModel
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * This composable function displays the user profile of the user passed as a parameter. It is used
 * to access more detailed information about a user (as his/her saved and recorded trips)
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param userMail : the user profile to display.
 * @param userProfileViewModel : the view model to handle the user profile.
 */
@Composable
fun UserView(
    navigation: Navigation,
    profile: MutableUserProfile,
    userMail: String,
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    test: Boolean = false
) {
  var readyToDisplay by remember { mutableStateOf(false) }

  val loggedUser by remember { mutableStateOf(profile) }
  var displayedUser by remember { mutableStateOf(UserProfile("")) }
  userProfileViewModel.getUserProfile(userMail) { fetchedUser ->
    if (fetchedUser != null) {
      displayedUser = fetchedUser
      readyToDisplay = true
    }
  }

    homeViewModel.setSearchFilter(FilterType.USERNAME)
    homeViewModel.setSearchQuery(displayedUser.username)

    // Observe the filtered itinerary list from the ViewModel
    val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())

    when (readyToDisplay) {
    false -> {
      WaitingScreen()
    }
    true -> {
      var areConnected by remember {
        mutableStateOf(loggedUser.userProfile.value.following.contains(displayedUser.mail))
      }

      val myTripsList = homeViewModel.filteredItineraryList
      var tripCount = 0
      myTripsList.observeForever(Observer { list -> tripCount = list.size })
      homeViewModel.setSearchFilter(FilterType.USERNAME)
      homeViewModel.setSearchQuery(
          displayedUser.username) // Filters the list of trips on user that created it

      Scaffold(
          topBar = {
            Row(
                modifier = Modifier.height(100.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start) {
                  Button(
                      onClick = { navigation.goBack() },
                      colors =
                          ButtonDefaults.buttonColors(
                              containerColor = Color.Transparent,
                              contentColor = MaterialTheme.colorScheme.onSurface),
                      modifier = Modifier.testTag("GoBackButton")) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                      }

                  Text(
                      text = displayedUser.username,
                      style =
                          TextStyle(
                              fontSize = 24.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(700),
                              color = MaterialTheme.colorScheme.onSurface,
                              textAlign = TextAlign.Start,
                              letterSpacing = 0.5.sp,
                          ),
                      modifier =
                          Modifier.weight(1f).height(37.dp).padding(5.dp).testTag("UsernameTitle"))
                }
          },
          bottomBar = { NavigationBar(navigation) },
          modifier = Modifier.fillMaxSize().testTag("UserView")) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxHeight(1f).fillMaxWidth(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Column (
                    modifier = Modifier.wrapContentHeight().fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.30f).wrapContentHeight(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            val imagePainter =
                                rememberAsyncImagePainter(model = displayedUser.profileImageUrl)

                            Image(
                                painter = imagePainter,
                                contentDescription =
                                "${displayedUser.username}'s profile picture",
                                contentScale = ContentScale.Crop,
                                modifier =
                                Modifier.clip(CircleShape)
                                    .size(130.dp)
                                    .shadow(
                                        elevation = 15.dp,
                                        shape = CircleShape,
                                        ambientColor = md_theme_light_dark,
                                        spotColor = md_theme_light_dark
                                    )
                                    .testTag("ProfilePicture")
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(0.70f).wrapContentHeight(),
                            horizontalAlignment = Alignment.End,
                        ) {
                            Text(
                                text = "${displayedUser.name} ${displayedUser.surname}",
                                style =
                                TextStyle(
                                    fontSize = 24.sp,
                                    lineHeight = 25.sp,
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight(700),
                                    color = md_theme_light_dark,
                                    textAlign = TextAlign.Right,
                                    letterSpacing = 0.5.sp,
                                ),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                                modifier = Modifier.padding(bottom = 20.dp)
                                    .testTag("NameAndSurname")
                            )
                            Text(
                                text = "Interests",
                                style = AppTypography.secondaryTitleStyle,
                                modifier = Modifier.align(Alignment.End).testTag("InterestTitle")
                            )
                            Text(
                                text = "Hiking, Photography", // profile.interestsList
                                style = AppTypography.secondaryContentStyle,
                                modifier =
                                Modifier.align(Alignment.End)
                                    .padding(bottom = 20.dp)
                                    .testTag("InterestsList")
                            )
                            Text(
                                text = "Travel Style",
                                style = AppTypography.secondaryTitleStyle,
                                modifier = Modifier.align(Alignment.End).testTag("TravelStyleTitle")
                            )
                            Text(
                                text = "Adventure, Cultural", // profile.travelStyleList
                                style = AppTypography.secondaryContentStyle,
                                modifier =
                                Modifier.align(Alignment.End)
                                    .padding(bottom = 20.dp)
                                    .testTag("TravelStyleList")
                            )
                            Text(
                                text = "Languages",
                                style = AppTypography.secondaryTitleStyle,
                                modifier = Modifier.align(Alignment.End).testTag("LanguagesTitle")
                            )
                            Text(
                                text = "English, Spanish", // profile.languagesList
                                style = AppTypography.secondaryContentStyle,
                                modifier = Modifier.align(Alignment.End).testTag("LanguagesList")
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 40.dp, end = 40.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (areConnected) {
                                    userProfileViewModel.removeFollowing(loggedUser, displayedUser)
                                } else {
                                    userProfileViewModel.addFollowing(loggedUser, displayedUser)
                                }
                                areConnected = !areConnected
                            },
                            colors =
                            if (areConnected) {
                                ButtonDefaults.buttonColors(
                                    containerColor = md_theme_orange,
                                    contentColor = md_theme_light_onPrimary
                                )
                            } else {
                                ButtonDefaults.buttonColors(
                                    containerColor = md_theme_grey,
                                    contentColor = md_theme_light_onPrimary
                                )
                            },
                            modifier =
                            Modifier.height(40.dp).fillMaxWidth().testTag("FollowingButton"),
                        ) {
                            Text(
                                text = if (areConnected) "Following" else "Follow",
                                style =
                                TextStyle(
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight(500),
                                    color = MaterialTheme.colorScheme.surface,
                                    textAlign = TextAlign.Center,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.wrapContentHeight().align(Alignment.CenterHorizontally)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically).padding(40.dp)
                        ) {
                            Text(
                                text = "$tripCount", // Call to the filtered Itinerarylist
                                modifier =
                                Modifier.align(Alignment.CenterHorizontally).testTag("TripsCount"),
                                style = AppTypography.bigNumberStyle
                            )
                            Text(
                                text = "Trips",
                                modifier =
                                Modifier.align(Alignment.CenterHorizontally).testTag("TripsTitle"),
                                style = AppTypography.categoryTextStyle
                            )
                        }
                        Column(
                            modifier =
                            Modifier.align(Alignment.CenterVertically)
                                .padding(horizontal = 30.dp)
                        ) {
                            Text(
                                text = "${displayedUser.followers.size}",
                                modifier =
                                Modifier.align(Alignment.CenterHorizontally)
                                    .testTag("FollowersCount"),
                                style = AppTypography.bigNumberStyle
                            )
                            Text(
                                text = "Followers",
                                modifier =
                                Modifier.align(Alignment.CenterHorizontally)
                                    .testTag("FollowersTitle"),
                                style = AppTypography.categoryTextStyle
                            )
                        }
                        Column(
                            modifier =
                            Modifier.align(Alignment.CenterVertically)
                                .padding(horizontal = 30.dp)
                        ) {
                            Text(
                                text = "${displayedUser.following.size}",
                                modifier =
                                Modifier.align(Alignment.CenterHorizontally)
                                    .testTag("FollowingCount"),
                                style = AppTypography.bigNumberStyle
                            )
                            Text(
                                text = "Following",
                                modifier =
                                Modifier.align(Alignment.CenterHorizontally)
                                    .testTag("FollowingTitle"),
                                style = AppTypography.categoryTextStyle
                            )
                        }
                    }
                }
                when (filteredList) {
                    // If the displayed user doesn't have any itineraries we display a message
                    emptyList<Itinerary>() -> {
                        // Display a message when the list is empty
                        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                            contentAlignment = Alignment.Center) {
                            Text(
                                text = "${displayedUser.name} ${displayedUser.surname} does not have any trips yet",
                                modifier =
                                Modifier.padding(30.dp).align(Alignment.Center).testTag("NoTripsText"),
                                fontSize = 16.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = md_theme_grey)
                        }
                    }
                    else -> {
                        // Display the list of itineraries when the list is not empty
                        val listState = rememberLazyListState()
                        LazyColumn(
                            modifier =
                            Modifier.fillMaxWidth().fillMaxHeight()
                                .testTag("MyTripsList"),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                            state = listState) {
                            items(filteredList) { itinerary ->
                                Log.d("ItineraryToDisplay", "Displaying itinerary: $itinerary")
                                DisplayItinerary(
                                    itinerary = itinerary,
                                    navigation = navigation,
                                    onClick = { navigation.navigateTo(Route.MAPS, itinerary.id) },
                                    test = test,
                                )
                            }
                        }
                    }
                }
                }
          }
    }
  }
}
