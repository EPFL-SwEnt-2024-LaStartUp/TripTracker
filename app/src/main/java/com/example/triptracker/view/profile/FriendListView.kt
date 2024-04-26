package com.example.triptracker.view.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.R
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_secondaryContainer
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
  val friends =
      if (followers) {
        userProfile.followers
      } else {
        userProfile.following
      }

  // Display the list of user's profiles
  LazyColumn(
      modifier = Modifier.fillMaxWidth().padding(15.dp).testTag("FriendListScreen"),
  ) {
    items(friends) { friend ->
      // Display the user's profile
      Row(
          modifier = Modifier.height(70.dp).fillMaxWidth(),
      ) {
        Column(modifier = Modifier.fillMaxHeight()) {
          Text(
              text = friend.username,
              style =
                  TextStyle(
                      fontSize = 20.sp,
                      lineHeight = 16.sp,
                      fontFamily = FontFamily(Font(R.font.montserrat)),
                      fontWeight = FontWeight(700),
                      color = Color.Black,
                      textAlign = TextAlign.Right,
                      letterSpacing = 0.5.sp),
          )
          Row(
              // modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = friend.name,
                    style = AppTypography.secondaryTitleStyle,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = friend.surname,
                    style = AppTypography.secondaryTitleStyle,
                )
              }
        }
        Column(
            modifier = Modifier.fillMaxWidth().testTag("Friend"),
            horizontalAlignment = Alignment.End) {
              if (followers) {
                // Display the remove follower button
                RemoveFriendButton(
                    remove = { userProfileViewModel.removeFollowerInDb(userProfile, friend) },
                    undoRemove = { userProfileViewModel.addFollowersInDb(userProfile, friend) },
                    followers = true)
              } else {
                RemoveFriendButton(
                    remove = { userProfileViewModel.removeFollowingInDb(userProfile, friend) },
                    undoRemove = { userProfileViewModel.addFollowingInDb(userProfile, friend) },
                    followers = false)
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
                containerColor = md_theme_light_dark,
                contentColor = md_theme_light_secondaryContainer)
          } else {
            ButtonDefaults.buttonColors(
                containerColor = md_theme_light_secondaryContainer,
                contentColor = md_theme_light_dark)
          },
      modifier =
          Modifier.height(40.dp)
              .width(120.dp)
              .testTag("RemoveButton")) { // .testTag(if (isRemoved) { "RemoveButton" } else {
        // "UndoButton" })) {
        Text(
            text =
                // Display the appropriate button text based on whether we are prompting
                // follower or following and whether the friend have been removed or not
                if (!followers && isRemoved) "Follow" // when displaying removed following
                else if (!followers && !isRemoved) "Following" // when displaying following
                else if (followers && isRemoved) "Undo" // when displaying removed followers
                else "Remove" // when displaying followers
            )
      }
}

// @Preview
// @Composable
// fun FriendListViewPreview() {
//    val mockUser1 =
//        UserProfile(
//            "1",
//            "Alice",
//            "Smith",
//            Date(2021, 1, 1),
//            "AliceS",
//            "stupid-image-url.com",
//            emptyList(),
//            emptyList()
//        )
//
//    val mockUser2 =
//        UserProfile("2", "Bob", "Johnson", Date(2021, 1, 1), "BobJ", null, emptyList(),
// emptyList())
//
//    val mockUser3 =
//        UserProfile(
//            "3",
//            "Charlie",
//            "Brown",
//            Date(2021, 1, 1),
//            "CharlieB",
//            null,
//            listOf(mockUser1, mockUser2),
//            listOf(mockUser1, mockUser2)
//        )
//
//    val mockUsers = listOf(mockUser1, mockUser2, mockUser3)
//
//    FriendListView(userProfileViewModel = UserProfileViewModel(), userProfile = mockUser3,
// followers = true)
// }
