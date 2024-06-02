package com.example.triptracker.view.profile.subviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.triptracker.model.profile.MutableUserProfile
import com.example.triptracker.model.profile.Relationship
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_light_onSurface
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * This composable function displays the user's followers or following list.
 *
 * @param viewModel : ViewModel for the UserProfile class
 * @param profile : the profile of the current user
 * @param relationship : the relationship between the current user and the friend
 * @param friendList : List of friends' email to display
 */
@Composable
fun FriendListView(
    navigation: Navigation,
    viewModel: UserProfileViewModel,
    profile: MutableUserProfile,
    relationship: Relationship,
    friendList: State<List<UserProfile>>
) {

  val userProfile by remember { mutableStateOf(profile) }

  // If there is no profile corresponding to the search query we display a message
  // If we are in the friend finder view and the search query is empty we don't display profiles
  if (friendList.value.isEmpty() ||
      (relationship == Relationship.FRIENDS && viewModel.searchQuery.value == "")) {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().testTag("NotDisplayingProfile"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
              text =
                  if (relationship == Relationship.FOLLOWER) {
                    "No followers"
                  } else if (relationship == Relationship.FOLLOWING) {
                    "Not following anyone"
                  } else {
                    "Start searching for friends"
                  },
              style =
                  TextStyle(
                      fontSize = 20.sp,
                      lineHeight = 16.sp,
                      fontFamily = Montserrat,
                      fontWeight = FontWeight(600),
                      color = MaterialTheme.colorScheme.onSurface,
                      textAlign = TextAlign.Center,
                      letterSpacing = 0.5.sp),
              modifier = Modifier.fillMaxWidth().padding(10.dp))
        }
  } else {
    // Display the list of user's profiles
    LazyColumn(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(15.dp).testTag("FriendList"),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
          items(friendList.value) { friend ->
            // we do not prompt the profile of the current user
            if (friend.mail != userProfile.userProfile.value.mail) {
              // Display the user's profile
              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .height((LocalConfiguration.current.screenHeightDp * 0.14).dp)
                          .background(
                              md_theme_light_onSurface,
                              shape =
                                  RoundedCornerShape(
                                      (LocalConfiguration.current.screenHeightDp * 0.05f).dp))
                          .testTag("FriendProfile")
                          .clickable {
                            navigation.navController.navigate(Route.USER + "/${friend.mail}")
                          },
                  contentAlignment = Alignment.Center) {
                    Row(
                        modifier =
                            Modifier.fillMaxHeight()
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                          // Image painter for loading from a URL
                          val imagePainter =
                              rememberAsyncImagePainter(model = friend.profileImageUrl)

                          Image(
                              painter = imagePainter,
                              contentDescription = "${friend.username}'s profile picture",
                              contentScale = ContentScale.Crop,
                              modifier =
                                  Modifier.size(
                                          (LocalConfiguration.current.screenHeightDp * 0.09f).dp)
                                      .clip(RoundedCornerShape(50))
                                      .align(Alignment.CenterVertically))
                          Column(
                              modifier =
                                  Modifier.fillMaxHeight()
                                      .width((LocalConfiguration.current.screenWidthDp * 0.45f).dp)
                                      .padding(start = 15.dp),
                              verticalArrangement = Arrangement.Center) {
                                Text(
                                    text = friend.username,
                                    style =
                                        TextStyle(
                                            fontSize =
                                                (LocalConfiguration.current.screenHeightDp * 0.02f)
                                                    .sp,
                                            lineHeight =
                                                (LocalConfiguration.current.screenHeightDp * 0.02f)
                                                    .sp,
                                            fontFamily = Montserrat,
                                            fontWeight = FontWeight(550),
                                            color = md_theme_light_onPrimary,
                                            textAlign = TextAlign.Left,
                                            letterSpacing = 0.5.sp),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1)

                                Text(
                                    text = "${friend.name} ${friend.surname}",
                                    style =
                                        TextStyle(
                                            fontSize =
                                                (LocalConfiguration.current.screenHeightDp * 0.018f)
                                                    .sp,
                                            lineHeight =
                                                (LocalConfiguration.current.screenHeightDp * 0.02f)
                                                    .sp,
                                            fontFamily = Montserrat,
                                            fontWeight = FontWeight(600),
                                            color = md_theme_dark_gray,
                                            textAlign = TextAlign.Left,
                                            letterSpacing = 0.5.sp),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1)
                              }
                          Column(
                              modifier = Modifier.wrapContentWidth(),
                              horizontalAlignment = Alignment.End,
                          ) {
                            // Display the remove friend button
                            RemoveFriendButton(
                                viewModel = viewModel,
                                profile = profile,
                                friend = friend,
                                relationship = relationship)
                          }
                        }
                  }
            }
          }
        }
  }
}

/** This composable function displays a button to remove a follower. */
@Composable
fun RemoveFriendButton(
    viewModel: UserProfileViewModel,
    profile: MutableUserProfile,
    friend: UserProfile,
    relationship: Relationship
) {
  val userProfile by remember { mutableStateOf(profile) }

  // we fetch the last version of the follower
  var updatedFriend = friend.copy()
  viewModel.getUserProfile(friend.mail) { fetchedUser ->
    if (fetchedUser != null) {
      updatedFriend = fetchedUser
    }
  }

  // variable to keep track of whether the user and follower are connected (following/follower)
  var areConnected by remember {
    mutableStateOf(userProfile.userProfile.value.following.contains(friend.mail))
  }

  if (relationship == Relationship.FOLLOWER) {
    areConnected = userProfile.userProfile.value.followers.contains(friend.mail)
  }

  Button(
      onClick = {
        if (relationship == Relationship.FRIENDS || relationship == Relationship.FOLLOWING) {
          if (areConnected) {
            viewModel.removeFollowing(userProfile, updatedFriend)
          } else {
            viewModel.addFollowing(userProfile, updatedFriend)
          }
        } else if (relationship == Relationship.FOLLOWER) {
          if (areConnected) {
            viewModel.removeFollower(userProfile, updatedFriend)
          } else {
            viewModel.addFollower(userProfile, updatedFriend)
          }
        }
        areConnected = !areConnected
      },
      colors =
          if (areConnected) {
            ButtonDefaults.buttonColors(
                containerColor = md_theme_grey, contentColor = md_theme_light_onPrimary)
          } else {
            ButtonDefaults.buttonColors(
                containerColor = md_theme_orange, contentColor = md_theme_light_onPrimary)
          },
      modifier =
          Modifier.height((LocalConfiguration.current.screenHeightDp * 0.05f).dp)
              .width((LocalConfiguration.current.screenWidthDp * 0.23f).dp)
              .testTag("RemoveButton"),
      contentPadding =
          PaddingValues( // Reduce the padding around the text
              start = 2.dp, top = 4.dp, end = 2.dp, bottom = 4.dp)) {
        Text(
            text =
                // Display the appropriate button text based on whether we are prompting
                // followers, following or profiles and whether the button have been toggled or not
                //
                if (relationship == Relationship.FOLLOWING ||
                    relationship == Relationship.FRIENDS) {
                  if (areConnected) "Following" else "Follow"
                } else {
                  if (areConnected) "Remove" else "Undo"
                },
            modifier = Modifier.fillMaxWidth(),
            style =
                TextStyle(
                    fontSize = (LocalConfiguration.current.screenHeightDp * 0.014f).sp,
                    lineHeight = (LocalConfiguration.current.screenHeightDp * 0.014f).sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight(500),
                    color = md_theme_light_onPrimary,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp))
      }
}
