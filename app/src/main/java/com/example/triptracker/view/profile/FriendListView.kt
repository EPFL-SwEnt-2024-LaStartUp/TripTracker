package com.example.triptracker.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.compose.rememberAsyncImagePainter
import com.example.triptracker.R
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * This composable function displays the user's followers or following list.
 *
 * @param userProfileViewModel : ViewModel for the UserProfile class
 * @param userProfile : the profile of the current user
 * @param followers : Boolean to determine if the list is for followers or following
 */
@Composable
fun FriendListView(
    userProfileViewModel: UserProfileViewModel,
    userProfile: UserProfile,
    followers: Boolean,
) {
  // TODO: Remove this hardcoded list of friends
  val friends =
      listOf(
          UserProfile(
              "schifferlitheo@gmail.com",
              "Theo",
              "Schifferli",
              "15-October-1946",
              "Tete la malice",
              "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcS5n-VN2sv2jYgbMF3kVQWkYZQtdlQzje7_-9SYrgFe6w6gUQmL",
              listOf("cleorenaud38@gmail.com"),
              emptyList()))

  // Display the list of user's profiles
  LazyColumn(
      modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(15.dp).testTag("FriendListScreen"),
      verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (friends.isEmpty()) {
          item {
            Text(
                text = if (followers) "No followers" else "Not following anyone",
                style =
                    TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight(700),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp),
                modifier = Modifier.fillMaxWidth().padding(10.dp))
          }
        } else {
          items(friends) { friend ->
            if (friend != null) {
              // Display the user's profile
              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .height(105.dp)
                          .background(md_theme_light_dark, shape = RoundedCornerShape(35.dp))
                          .testTag("FriendProfile"),
                  contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier.fillMaxHeight().padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                          // Image painter for loading from a URL
                          val imagePainter =
                              rememberAsyncImagePainter(model = friend.profileImageUrl)

                          Image(
                              painter = imagePainter,
                              contentDescription = "${userProfile.username}'s profile picture",
                              contentScale = ContentScale.Crop,
                              modifier =
                                  Modifier.size(62.dp)
                                      .clip(RoundedCornerShape(50))
                                      .align(Alignment.CenterVertically))
                          Column(
                              modifier = Modifier.fillMaxHeight().padding(start = 15.dp),
                              verticalArrangement = Arrangement.Center) {
                                Text(
                                    text = friend.username,
                                    style =
                                        TextStyle(
                                            fontSize = 16.sp,
                                            lineHeight = 16.sp,
                                            fontFamily = FontFamily(Font(R.font.montserrat)),
                                            fontWeight = FontWeight(600),
                                            color = Color.White,
                                            textAlign = TextAlign.Left,
                                            letterSpacing = 0.5.sp),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1)
                                Row(
                                    // modifier = Modifier.fillMaxWidth(),
                                    //                              verticalAlignment =
                                    // Alignment.CenterVertically
                                    ) {
                                      Text(
                                          text = "${friend.name} ${friend.surname}",
                                          style =
                                              TextStyle(
                                                  fontSize = 14.sp,
                                                  lineHeight = 16.sp,
                                                  fontFamily = FontFamily(Font(R.font.montserrat)),
                                                  fontWeight = FontWeight(600),
                                                  color = md_theme_dark_gray,
                                                  textAlign = TextAlign.Left,
                                                  letterSpacing = 0.5.sp),
                                          overflow = TextOverflow.Ellipsis,
                                          maxLines = 1)
                                    }
                              }
                          Column(
                              modifier = Modifier.fillMaxWidth().width(95.dp),
                              horizontalAlignment = Alignment.End,
                          ) {
                            if (followers) {
                              // Display the remove follower button
                              RemoveFriendButton(
                                  remove = {
                                    userProfileViewModel.removeFollower(userProfile, friend)
                                  },
                                  undoRemove = {
                                    userProfileViewModel.addFollower(userProfile, friend)
                                  },
                                  followers = true)
                            } else {
                              RemoveFriendButton(
                                  remove = {
                                    userProfileViewModel.removeFollower(friend, userProfile)
                                  },
                                  undoRemove = {
                                    userProfileViewModel.addFollower(friend, userProfile)
                                  },
                                  followers = false)
                            }
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
fun RemoveFriendButton(remove: () -> Unit, undoRemove: () -> Unit, followers: Boolean) {
  // State to determine if the follower is removed and update the button text
  var isRemoved by remember { mutableStateOf(false) }

  Button(
      onClick = {
        isRemoved = !isRemoved
        // Remove the follower if the button is clicked
        if (isRemoved) {
          remove()
        }
        // Undo the removal if the button is clicked again
        else {
          undoRemove()
        }
      },
      colors =
          if (!isRemoved) {
            ButtonDefaults.buttonColors(
                containerColor = md_theme_orange, contentColor = md_theme_light_onPrimary)
          } else {
            ButtonDefaults.buttonColors(
                containerColor = md_theme_grey, contentColor = md_theme_light_onPrimary)
          },
      modifier = Modifier.height(40.dp).width(95.dp).testTag("RemoveButton"),
      contentPadding =
          PaddingValues( // Reduce the padding around the text
              start = 2.dp,
              top = 4.dp,
              end = 2.dp,
              bottom = 4.dp)) { // .testTag(if (isRemoved) { "RemoveButton" } else {
        // "UndoButton" })) {
        Text(
            text =
                // Display the appropriate button text based on whether we are prompting
                // follower or following and whether the friend have been removed or not
                if (!followers && isRemoved) "Follow" // when displaying removed following
                else if (!followers && !isRemoved) "Following" // when displaying following
                else if (followers && isRemoved) "Undo" // when displaying removed followers
                else "Remove", // when displaying followers
            modifier = Modifier.fillMaxWidth(),
            style =
                TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight(500),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp))
      }
}
