package com.example.triptracker.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.viewmodel.UserProfileViewModel

/* Visual preview without any logic */
@Preview
@Composable
fun UserProfilePreview() {
  Scaffold(
      topBar = {},
      bottomBar = { /*Navigation Bar when it will be implemented */},
      modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
          Row(modifier = Modifier.height(75.dp).fillMaxSize()) {}

          // Profile picture and name (later maybe more informations depending of data classes
          // updates
          Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            // Profile picture
            Column() {
              AsyncImage(
                  model =
                      "https://img-19.commentcamarche.net/wzKKufHO7dLH-WPFdXJHEmOmi7E=/1500x/smart/2d8c2b30aee345008ee860087f8bcdc9/ccmcms-commentcamarche/36120212.jpg",
                  contentDescription = "Profile picture",
                  placeholder = painterResource(id = R.drawable.blankprofile),
                  modifier =
                      Modifier.shadow(
                              elevation = 15.dp,
                              shape = CircleShape,
                              ambientColor = Color.Black,
                              spotColor = Color.Black)
                          .size(110.dp)
                          .clip(CircleShape),
                  contentScale = ContentScale.Crop)
            }
            // Other informations
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
              Text(
                  text =
                      "MICHAEL JACKSON", // I think we only show the pseudo here and keep birthdate
                  // name and surname private.
                  style =
                      TextStyle(
                          fontSize = 24.sp,
                          lineHeight = 16.sp,
                          fontWeight = FontWeight(700),
                          textAlign = TextAlign.Right,
                          letterSpacing = 0.5.sp,
                      ))
              Text(
                  text = "Center of Interests",
                  style =
                      TextStyle(
                          fontSize = 20.sp,
                          lineHeight = 13.sp,
                          fontWeight = FontWeight(300),
                          textAlign = TextAlign.Right,
                          letterSpacing = 0.5.sp,
                      ),
                  modifier = Modifier.align(Alignment.End))

              /*add more informations later if UserProfile is udpated*/
            }
          }
          // Number of trips, followers and following when implemented in the data classes
          Row(modifier = Modifier.height(275.dp).align(Alignment.CenterHorizontally)) {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 40.dp)) {
                  Text("NBR", modifier = Modifier.align(Alignment.CenterHorizontally))
                  Text("Trips", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                  Text("NBR", modifier = Modifier.align(Alignment.CenterHorizontally))
                  Text("Followers", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                  Text("NBR", modifier = Modifier.align(Alignment.CenterHorizontally))
                  Text("Following", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
          }
          // Favourites, Friends, Settings and MyTrips tiles
          Box(
              modifier =
                  Modifier.height(235.dp).width(330.dp).align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = { /*TODO*/},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier =
                        Modifier.align(Alignment.TopStart)
                            .height(100.dp)
                            .width(145.dp)
                            .background(
                                color = Color.Black, shape = RoundedCornerShape(size = 16.dp))) {
                      Icon(
                          Icons.Outlined.FavoriteBorder,
                          contentDescription = "Favorite icon",
                          tint = Color(0xFFFF9800),
                          modifier = Modifier.padding(5.dp))
                      Text("Favorites")
                    }
                Button(
                    onClick = { /*TODO*/},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier =
                        Modifier.align(Alignment.TopEnd)
                            .height(100.dp)
                            .width(145.dp)
                            .background(
                                color = Color.Black, shape = RoundedCornerShape(size = 16.dp))) {
                      Icon(
                          Icons.Outlined.Person,
                          contentDescription = "Friends icon",
                          tint = Color(0xFFFF9800),
                          modifier = Modifier.padding(5.dp))
                      Text("Friends")
                    }
                Button(
                    onClick = { /*TODO*/},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier =
                        Modifier.align(Alignment.BottomStart)
                            .height(100.dp)
                            .width(145.dp)
                            .background(
                                color = Color.Black, shape = RoundedCornerShape(size = 16.dp))) {
                      Icon(
                          Icons.Outlined.List,
                          contentDescription = "MyTrips icon",
                          tint = Color(0xFFFF9800),
                          modifier = Modifier.padding(5.dp))
                      Text("MyTrips")
                    }
                Button(
                    onClick = { /*TODO*/},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier =
                        Modifier.align(Alignment.BottomEnd)
                            .height(100.dp)
                            .width(145.dp)
                            .background(
                                color = Color.Black, shape = RoundedCornerShape(size = 16.dp))) {
                      Icon(
                          Icons.Outlined.Settings,
                          contentDescription = "Settings icon",
                          tint = Color(0xFFFF9800),
                          modifier = Modifier.padding(5.dp))
                      Text("Settings")
                    }
              }
        }
      }
}

@Composable
fun UserProfileOverview(
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
    navigation: Navigation
) {
  // Url de la photo de profil à récupérer grace à l'uid
  val uid = "TODO"
  val profile = userProfileViewModel.userProfileList.getUserProfile(uid)
  val url = profile.profileImageUrl

  Scaffold(
      topBar = {},
      bottomBar = { /*Navigation Bar when it will be implemented */},
      modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
          Row(modifier = Modifier.height(75.dp).fillMaxSize()) {}

          // Profile picture and name (later maybe more informations depending of data classes
          // updates
          Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            // Profile picture
            Column() {
              AsyncImage(
                  model = url,
                  contentDescription = "Profile picture",
                  placeholder = painterResource(id = R.drawable.blankprofile),
                  modifier =
                      Modifier.shadow(
                              elevation = 15.dp,
                              shape = CircleShape,
                              ambientColor = Color.Black,
                              spotColor = Color.Black)
                          .size(110.dp)
                          .clip(CircleShape),
                  contentScale = ContentScale.Crop)
            }
            // Other informations
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
              Text(
                  text =
                      profile
                          .name, // I think we only show the pseudo here and keep birthdate name and
                  // surname private.
                  style =
                      TextStyle(
                          fontSize = 24.sp,
                          lineHeight = 16.sp,
                          fontWeight = FontWeight(700),
                          textAlign = TextAlign.Right,
                          letterSpacing = 0.5.sp,
                      ))
              Text(
                  text = "Center of Interests",
                  style =
                      TextStyle(
                          fontSize = 20.sp,
                          lineHeight = 13.sp,
                          fontWeight = FontWeight(300),
                          textAlign = TextAlign.Right,
                          letterSpacing = 0.5.sp,
                      ),
                  modifier = Modifier.align(Alignment.End))

              /*add more informations later if UserProfile is udpated*/
            }
          }
          // Number of trips, followers and following when implemented in the data classes
          Row(modifier = Modifier.height(275.dp).align(Alignment.CenterHorizontally)) {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 40.dp)) {
                  Text("NBR", modifier = Modifier.align(Alignment.CenterHorizontally))
                  Text("Trips", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                  Text("NBR", modifier = Modifier.align(Alignment.CenterHorizontally))
                  Text("Followers", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                  Text("NBR", modifier = Modifier.align(Alignment.CenterHorizontally))
                  Text("Following", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
          }
          // Favourites, Friends, Settings and MyTrips tiles
          Box(
              modifier =
                  Modifier.height(235.dp).width(330.dp).align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = {
                      navigation.navigateTo(
                          TopLevelDestination(
                              Route.FAVORITES, Icons.Outlined.FavoriteBorder, "Favorites"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier =
                        Modifier.align(Alignment.TopStart)
                            .height(100.dp)
                            .width(145.dp)
                            .background(
                                color = Color.Black, shape = RoundedCornerShape(size = 16.dp))) {
                      Icon(
                          Icons.Outlined.FavoriteBorder,
                          contentDescription = "Favorite icon",
                          tint = Color(0xFFFF9800),
                          modifier = Modifier.padding(5.dp))
                      Text("Favorites")
                    }
                Button(
                    onClick = {
                      navigation.navigateTo(
                          TopLevelDestination(Route.FRIENDS, Icons.Outlined.Person, "Friends"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier =
                        Modifier.align(Alignment.TopEnd)
                            .height(100.dp)
                            .width(145.dp)
                            .background(
                                color = Color.Black, shape = RoundedCornerShape(size = 16.dp))) {
                      Icon(
                          Icons.Outlined.Person,
                          contentDescription = "Friends icon",
                          tint = Color(0xFFFF9800),
                          modifier = Modifier.padding(5.dp))
                      Text("Friends")
                    }
                Button(
                    onClick = {
                      navigation.navigateTo(
                          TopLevelDestination(Route.MYTRIPS, Icons.Outlined.List, "MyTrips"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier =
                        Modifier.align(Alignment.BottomStart)
                            .height(100.dp)
                            .width(145.dp)
                            .background(
                                color = Color.Black, shape = RoundedCornerShape(size = 16.dp))) {
                      Icon(
                          Icons.Outlined.List,
                          contentDescription = "MyTrips icon",
                          tint = Color(0xFFFF9800),
                          modifier = Modifier.padding(5.dp))
                      Text("MyTrips")
                    }
                Button(
                    onClick = {
                      navigation.navigateTo(
                          TopLevelDestination(Route.SETTINGS, Icons.Outlined.Settings, "Settings"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier =
                        Modifier.align(Alignment.BottomEnd)
                            .height(100.dp)
                            .width(145.dp)
                            .background(
                                color = Color.Black, shape = RoundedCornerShape(size = 16.dp))) {
                      Icon(
                          Icons.Outlined.Settings,
                          contentDescription = "Settings icon",
                          tint = Color(0xFFFF9800),
                          modifier = Modifier.padding(5.dp))
                      Text("Settings")
                    }
              }
        }
      }
}
