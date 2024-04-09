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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.Route
import com.example.triptracker.view.TopLevelDestination
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_orange
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
          Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 15.dp)) {
            // Profile picture
            Column() {
              AsyncImage(
                  model =
                      "https://img-19.commentcamarche.net/wzKKufHO7dLH-WPFdXJHEmOmi7E=/1500x/smart/2d8c2b30aee345008ee860087f8bcdc9/ccmcms-commentcamarche/36120212.jpg",
                  contentDescription = "Profile picture",
                  placeholder = painterResource(id = R.drawable.blankprofile),
                  modifier =
                      Modifier.padding(horizontal = 15.dp)
                          .shadow(
                              elevation = 15.dp,
                              shape = CircleShape,
                              ambientColor = Color.Black,
                              spotColor = Color.Black)
                          .size(110.dp)
                          .clip(CircleShape),
                  contentScale = ContentScale.Crop)
            }
            // Other informations
            Column() {
              Text(
                  text = "Laura Craft", // I think we only show the pseudo here and keep birthdate
                  // name and surname private.
                  style =
                      TextStyle(
                          fontSize = 24.sp,
                          lineHeight = 16.sp,
                          fontFamily = FontFamily(Font(R.font.montserrat)),
                          fontWeight = FontWeight(700),
                          color = Color.Black,
                          textAlign = TextAlign.Right,
                          letterSpacing = 0.5.sp,
                      ),
                  modifier = Modifier.width(250.dp).height(37.dp))
              Text(
                  text = "Interests",
                  style =
                      TextStyle(
                          fontSize = 14.sp,
                          lineHeight = 16.sp,
                          fontFamily = FontFamily(Font(R.font.montserrat)),
                          fontWeight = FontWeight(400),
                          color = md_theme_light_dark,
                          textAlign = TextAlign.Right,
                          letterSpacing = 0.5.sp,
                      ),
                  modifier = Modifier.align(Alignment.End))
              Text(
                  text = "Hiking, Photography",
                  style =
                      TextStyle(
                          fontSize = 12.sp,
                          lineHeight = 16.sp,
                          fontFamily = FontFamily(Font(R.font.montserrat)),
                          fontWeight = FontWeight(400),
                          color = md_theme_dark_gray,
                          textAlign = TextAlign.Right,
                          letterSpacing = 0.5.sp,
                      ),
                  modifier = Modifier.align(Alignment.End))

              /*add more informations later if UserProfile is udpated*/
            }
          }
          // Number of trips, followers and following when implemented in the data classes
          Row(modifier = Modifier.height(250.dp).align(Alignment.CenterHorizontally)) {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 40.dp)) {
                  Text(
                      text = "NBR",
                      modifier = Modifier.align(Alignment.CenterHorizontally),
                      style =
                          TextStyle(
                              fontSize = 24.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(700),
                              color = md_theme_light_dark,
                              textAlign = TextAlign.Center,
                              letterSpacing = 0.5.sp,
                          ))
                  Text(
                      text = "Trips",
                      modifier = Modifier.align(Alignment.CenterHorizontally),
                      style =
                          TextStyle(
                              fontSize = 12.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(300),
                              color = md_theme_dark_gray,
                              textAlign = TextAlign.Center,
                              letterSpacing = 0.5.sp,
                          ))
                }
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                  Text(
                      text = "NBR",
                      modifier = Modifier.align(Alignment.CenterHorizontally),
                      style =
                          TextStyle(
                              fontSize = 24.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(700),
                              color = md_theme_light_dark,
                              textAlign = TextAlign.Center,
                              letterSpacing = 0.5.sp,
                          ))
                  Text(
                      text = "Followers",
                      modifier = Modifier.align(Alignment.CenterHorizontally),
                      style =
                          TextStyle(
                              fontSize = 12.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(300),
                              color = md_theme_dark_gray,
                              textAlign = TextAlign.Center,
                              letterSpacing = 0.5.sp,
                          ))
                }
            Column(
                modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                  Text(
                      text = "NBR",
                      modifier = Modifier.align(Alignment.CenterHorizontally),
                      style =
                          TextStyle(
                              fontSize = 24.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(700),
                              color = md_theme_light_dark,
                              textAlign = TextAlign.Center,
                              letterSpacing = 0.5.sp,
                          ))
                  Text(
                      text = "Following",
                      modifier = Modifier.align(Alignment.CenterHorizontally),
                      style =
                          TextStyle(
                              fontSize = 12.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(300),
                              color = md_theme_dark_gray,
                              textAlign = TextAlign.Center,
                              letterSpacing = 0.5.sp,
                          ))
                }
          }
          // Favourites, Friends, Settings and MyTrips tiles
          Box(
              modifier =
                  Modifier.height(300.dp).width(350.dp).align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = { /*TODO*/},
                    colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
                    modifier =
                        Modifier.align(Alignment.TopStart)
                            .height(130.dp)
                            .width(160.dp)
                            .background(
                                color = md_theme_light_dark,
                                shape = RoundedCornerShape(size = 16.dp)))
                {
                    Column(modifier = Modifier.width(150.dp)) {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite icon",
                            tint = md_theme_orange,
                            modifier = Modifier.padding(vertical = 15.dp)

                        )
                        Text(
                            text = "Favourites",
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat)),
                                fontWeight = FontWeight(600),
                                color = md_theme_dark_gray,
                                letterSpacing = 0.5.sp,
                            ),
                            modifier = Modifier.padding(vertical = 15.dp)
                        )
                    }
                }
                Button(
                    onClick = { /*TODO*/},
                    colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
                    modifier =
                        Modifier.align(Alignment.TopEnd)
                            .height(130.dp)
                            .width(160.dp)
                            .background(
                                color = md_theme_light_dark,
                                shape = RoundedCornerShape(size = 16.dp)))
                {
                    Column(modifier = Modifier.width(150.dp)) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Friends icon",
                            tint = md_theme_orange,
                            modifier = Modifier.padding(vertical = 15.dp)

                        )
                        Text(
                            text = "Friends",
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat)),
                                fontWeight = FontWeight(600),
                                color = md_theme_dark_gray,
                                letterSpacing = 0.5.sp,
                            ),
                            modifier = Modifier.padding(vertical = 15.dp)
                        )
                    }
                }
              Button(
                  onClick = { /*TODO*/},
                  colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
                  modifier =
                  Modifier.align(Alignment.BottomStart)
                      .height(130.dp)
                      .width(160.dp)
                      .background(
                          color = md_theme_light_dark,
                          shape = RoundedCornerShape(size = 16.dp)))
              {
                  Column(modifier = Modifier.width(150.dp)) {
                      Icon(
                          Icons.Outlined.List,
                          contentDescription = "MyTrips Icon",
                          tint = md_theme_orange,
                          modifier = Modifier.padding(vertical = 15.dp)

                      )
                      Text(
                          text = "MyTrips",
                          style = TextStyle(
                              fontSize = 20.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(600),
                              color = md_theme_dark_gray,
                              letterSpacing = 0.5.sp,
                          ),
                          modifier = Modifier.padding(vertical = 15.dp)
                      )
                  }
              }
              Button(
                  onClick = { /*TODO*/},
                  colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
                  modifier =
                  Modifier.align(Alignment.BottomEnd)
                      .height(130.dp)
                      .width(160.dp)
                      .background(
                          color = md_theme_light_dark,
                          shape = RoundedCornerShape(size = 16.dp)))
              {
                  Column(modifier = Modifier.width(150.dp)) {
                      Icon(
                          Icons.Outlined.Settings,
                          contentDescription = "Settings",
                          tint = md_theme_orange,
                          modifier = Modifier.padding(vertical = 15.dp)

                      )
                      Text(
                          text = "Settings",
                          style = TextStyle(
                              fontSize = 20.sp,
                              lineHeight = 16.sp,
                              fontFamily = FontFamily(Font(R.font.montserrat)),
                              fontWeight = FontWeight(600),
                              color = md_theme_dark_gray,
                              letterSpacing = 0.5.sp,
                          ),
                          modifier = Modifier.padding(vertical = 15.dp)
                      )
                  }
              }
              }
        }
      }
}
/**
 * Composable displaying the profile overview featuring information about follower, following. 4
 * clickable buttons are available to access to the MyTrips, Favorites, Friends and settings views.
 *
 * @param userProfileViewModel: The view model of the UserProfile
 * @param navigation: The navigation of the app to switch between different views
 */
@Composable
fun UserProfileOverview(
    userProfileViewModel: UserProfileViewModel = UserProfileViewModel(),
    navigation: Navigation
) {
    // UID of the user that needs to be fetched from ??, usred to fetch the profile from the database
    val uid = "TODO"
    val profile = userProfileViewModel.userProfileList.getUserProfile(uid)
    val url = profile.profileImageUrl

    Scaffold(
        topBar = {}, bottomBar = { NavigationBar(navigation) }, modifier = Modifier.fillMaxSize()) {
            innerPadding ->
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
                Column() {
                    Text(
                        text = profile.name, // I think we only show the pseudo here and keep birthdate
                        // name and surname private.
                        style =
                        TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(700),
                            color = Color.Black,
                            textAlign = TextAlign.Right,
                            letterSpacing = 0.5.sp,
                        ),
                        modifier = Modifier.width(250.dp).height(37.dp))
                    Text(
                        text = "Interests",
                        style =
                        TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(400),
                            color = md_theme_light_dark,
                            textAlign = TextAlign.Right,
                            letterSpacing = 0.5.sp,
                        ),
                        modifier = Modifier.align(Alignment.End))
                    Text(
                        text = "Hiking, Photography",
                        style =
                        TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(400),
                            color = md_theme_dark_gray,
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
                    Text(
                        text = "NBR",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style =
                        TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(700),
                            color = md_theme_light_dark,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp,
                        ))
                    Text(
                        text = "Trips",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style =
                        TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(300),
                            color = md_theme_dark_gray,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp,
                        ))
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                    Text(
                        text = "NBR",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style =
                        TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(700),
                            color = md_theme_light_dark,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp,
                        ))
                    Text(
                        text = "Followers",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style =
                        TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(300),
                            color = md_theme_dark_gray,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp,
                        ))
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 30.dp)) {
                    Text(
                        text = "NBR",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style =
                        TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(700),
                            color = md_theme_light_dark,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp,
                        ))
                    Text(
                        text = "Following",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style =
                        TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat)),
                            fontWeight = FontWeight(300),
                            color = md_theme_dark_gray,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.5.sp,
                        ))
                }
            }
            // Favourites, Friends, Settings and MyTrips tiles
            Box(
                modifier =
                Modifier.height(300.dp).width(350.dp).align(Alignment.CenterHorizontally)) {
                Button(
                    onClick = {
                        navigation.navigateTo(
                            TopLevelDestination(Route.FAVORITES, R.drawable.favorite, "Favorites")
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
                    modifier =
                    Modifier.align(Alignment.TopStart)
                        .height(130.dp)
                        .width(160.dp)
                        .background(
                            color = md_theme_light_dark,
                            shape = RoundedCornerShape(size = 16.dp)))
                {
                    Column(modifier = Modifier.width(150.dp)) {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite icon",
                            tint = md_theme_orange,
                            modifier = Modifier.padding(vertical = 15.dp)

                        )
                        Text(
                            text = "Favourites",
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat)),
                                fontWeight = FontWeight(600),
                                color = md_theme_dark_gray,
                                letterSpacing = 0.5.sp,
                            ),
                            modifier = Modifier.padding(vertical = 15.dp)
                        )
                    }
                }
                Button(
                    onClick = {
                        navigation.navigateTo(
                            TopLevelDestination(Route.FRIENDS, R.drawable.friends, "Friends"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
                    modifier =
                    Modifier.align(Alignment.TopEnd)
                        .height(130.dp)
                        .width(160.dp)
                        .background(
                            color = md_theme_light_dark,
                            shape = RoundedCornerShape(size = 16.dp)))
                {
                    Column(modifier = Modifier.width(150.dp)) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Friends icon",
                            tint = md_theme_orange,
                            modifier = Modifier.padding(vertical = 15.dp)

                        )
                        Text(
                            text = "Friends",
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat)),
                                fontWeight = FontWeight(600),
                                color = md_theme_dark_gray,
                                letterSpacing = 0.5.sp,
                            ),
                            modifier = Modifier.padding(vertical = 15.dp)
                        )
                    }
                }
                Button(
                    onClick = {
                        navigation.navigateTo(
                            TopLevelDestination(Route.MYTRIPS, R.drawable.mytrips, "MyTrips"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
                    modifier =
                    Modifier.align(Alignment.BottomStart)
                        .height(130.dp)
                        .width(160.dp)
                        .background(
                            color = md_theme_light_dark,
                            shape = RoundedCornerShape(size = 16.dp)))
                {
                    Column(modifier = Modifier.width(150.dp)) {
                        Icon(
                            Icons.Outlined.List,
                            contentDescription = "MyTrips Icon",
                            tint = md_theme_orange,
                            modifier = Modifier.padding(vertical = 15.dp)

                        )
                        Text(
                            text = "MyTrips",
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat)),
                                fontWeight = FontWeight(600),
                                color = md_theme_dark_gray,
                                letterSpacing = 0.5.sp,
                            ),
                            modifier = Modifier.padding(vertical = 15.dp)
                        )
                    }
                }
                Button(
                    onClick = {
                        navigation.navigateTo(
                            TopLevelDestination(Route.SETTINGS, R.drawable.settings, "Settings"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
                    modifier =
                    Modifier.align(Alignment.BottomEnd)
                        .height(130.dp)
                        .width(160.dp)
                        .background(
                            color = md_theme_light_dark,
                            shape = RoundedCornerShape(size = 16.dp)))
                {
                    Column(modifier = Modifier.width(150.dp)) {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = md_theme_orange,
                            modifier = Modifier.padding(vertical = 15.dp)

                        )
                        Text(
                            text = "Settings",
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat)),
                                fontWeight = FontWeight(600),
                                color = md_theme_dark_gray,
                                letterSpacing = 0.5.sp,
                            ),
                            modifier = Modifier.padding(vertical = 15.dp)
                        )
                    }
                }
            }
        }
    }
}
