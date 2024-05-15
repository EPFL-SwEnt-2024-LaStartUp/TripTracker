package com.example.triptracker.view.profile.subviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.triptracker.R
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.profile.secondaryContentStyle
import com.example.triptracker.view.profile.secondaryTitleStyle
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_light_dark

/**
 * This composable function displays the user's profile information.
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param userProfile : the user profile to display.
 * @param editable : a boolean to know if the profile is editable.
 */
@Composable
fun ProfileInfoView(navigation: Navigation, userProfile: UserProfile, editable: Boolean = true) {
  val MAX_PROFILE_NAME_LENGTH = 15

  var sizeUsername = (LocalConfiguration.current.screenHeightDp * 0.02f).sp
  if (userProfile.username.length > MAX_PROFILE_NAME_LENGTH) {
    sizeUsername = (LocalConfiguration.current.screenHeightDp * 0.018f).sp
  }

  Row(horizontalArrangement = Arrangement.Center) {
    // Profile picture
    Column() {
      AsyncImage(
          model = userProfile.profileImageUrl,
          contentDescription = "Profile picture",
          placeholder = painterResource(id = R.drawable.blankprofile),
          modifier =
              Modifier.shadow(
                      elevation = 15.dp,
                      shape = CircleShape,
                      ambientColor = md_theme_light_dark,
                      spotColor = md_theme_light_dark)
                  .padding(start = 15.dp)
                  .size((LocalConfiguration.current.screenHeightDp * 0.11f).dp)
                  .clip(CircleShape)
                  .testTag("ProfilePicture"),
          contentScale = ContentScale.Crop)
    }

    // Other informations
    Column(horizontalAlignment = Alignment.End) {
      Row(horizontalArrangement = Arrangement.End) {
        if (editable) {
          IconButton(onClick = { navigation.navController.navigate(Route.EDIT) }) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit")
          }
        }
        Column() {
          Text(
              text = userProfile.username,
              style =
                  TextStyle(
                      fontSize = sizeUsername,
                      lineHeight = (LocalConfiguration.current.screenHeightDp * 0.016f).sp,
                      fontFamily = Montserrat,
                      fontWeight = FontWeight(700),
                      color = md_theme_light_dark,
                      textAlign = TextAlign.Right,
                      letterSpacing = (LocalConfiguration.current.screenHeightDp * 0.0005f).sp,
                  ),
              modifier =
                  Modifier.width((LocalConfiguration.current.screenHeightDp * 0.67f).dp)
                      .wrapContentHeight()
                      .padding(
                          top = (LocalConfiguration.current.screenHeightDp * 0.012f).dp,
                          end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp)
                      .testTag("Username"))
          Text(
              text = userProfile.name + " " + userProfile.surname,
              style = secondaryTitleStyle(LocalConfiguration.current.screenHeightDp),
              modifier =
                  Modifier.align(Alignment.End)
                      .padding(
                          end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp,
                          bottom = (LocalConfiguration.current.screenHeightDp * 0.012f).dp)
                      .testTag("NameAndSurname"))
        }
      }

      Text(
          text = "Interests",
          style = secondaryTitleStyle(LocalConfiguration.current.screenHeightDp),
          modifier =
              Modifier.align(Alignment.End)
                  .padding(end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp))
      Text(
          text = "Hiking, Photography", // profile.interestsList
          style = secondaryContentStyle(LocalConfiguration.current.screenHeightDp),
          modifier =
              Modifier.align(Alignment.End)
                  .padding(
                      end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp,
                      bottom = (LocalConfiguration.current.screenHeightDp * 0.012f).dp),
      )
      Text(
          text = "Travel Style",
          style = secondaryTitleStyle(LocalConfiguration.current.screenHeightDp),
          modifier =
              Modifier.align(Alignment.End)
                  .padding(end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp))
      Text(
          text = "Adventure, Cultural", // profile.travelStyleList
          style = secondaryContentStyle(LocalConfiguration.current.screenHeightDp),
          modifier =
              Modifier.align(Alignment.End)
                  .padding(
                      end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp,
                      bottom = (LocalConfiguration.current.screenHeightDp * 0.012f).dp),
      )
      Text(
          text = "Languages",
          style = secondaryTitleStyle(LocalConfiguration.current.screenHeightDp),
          modifier =
              Modifier.align(Alignment.End)
                  .padding(end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp))
      Text(
          text = "English, Spanish", // profile.languagesList
          style = secondaryContentStyle(LocalConfiguration.current.screenHeightDp),
          modifier =
              Modifier.align(Alignment.End)
                  .padding(
                      end = (LocalConfiguration.current.screenHeightDp * 0.033f).dp,
                      bottom = (LocalConfiguration.current.screenHeightDp * 0.012f).dp),
      )
    }
  }
}
