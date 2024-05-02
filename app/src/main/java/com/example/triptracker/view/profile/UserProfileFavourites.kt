package com.example.triptracker.view.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.viewmodel.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserProfileFavourite(
    homeViewModel: HomeViewModel = viewModel(),
    navigation: Navigation,
    test: Boolean = false
) {
  Log.d("HomeScreen", "Rendering HomeScreen")

  // TODO Not implemented yet -> next sprint
  // homeViewModel.setSearchFilter(FilterType.USERNAME)
  // homeViewModel.setSearchQuery("Cleoooo")
  // val filteredList by homeViewModel.filteredItineraryList.observeAsState(initial = emptyList())
  val filteredList = emptyList<Itinerary>()
  Scaffold(
      topBar = {},
      bottomBar = { NavigationBar(navigation) },
      modifier = Modifier.testTag("UserProfileFavouriteScreen")) {
        Box {
          Box(modifier = Modifier.fillMaxWidth().padding(top = 100.dp)) {
            Text(
                text = "Favourites not implemented yet, please check back later!",
                modifier =
                    Modifier.padding(30.dp).align(Alignment.TopCenter).testTag("NoFavouritesText"),
                fontSize = 16.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = md_theme_grey)
          }

          Box(
              modifier =
                  Modifier.fillMaxWidth()
                      .height(100.dp)
                      .padding(horizontal = 16.dp)
                      .background(Color.White),
              contentAlignment = Alignment.Center) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(vertical = 10.dp)) { // Row for the back button
                      Icon(
                          imageVector = Icons.Default.ArrowBack,
                          contentDescription = "Back",
                          modifier =
                              Modifier.weight(1f)
                                  .testTag("FavouritesBackButton")
                                  .clickable { navigation.goBack() }
                                  .align(Alignment.CenterVertically),
                      )
                      Text(
                          text = "Favourites",
                          fontSize = 28.sp,
                          fontFamily = Montserrat,
                          fontWeight = FontWeight.SemiBold,
                          color = md_theme_light_dark,
                          modifier = Modifier.fillMaxWidth().weight(8f).testTag("FavouritesTitle"),
                          textAlign = TextAlign.Center)

                      Spacer(modifier = Modifier.weight(1f))
                    }
              }
        }
      }
}
