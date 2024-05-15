package com.example.triptracker.view.profile.subviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.triptracker.view.profile.buttonTextStyle
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_orange

/**
 * This composable function displays a button with an icon and a label. It is used in the
 * UserProfileOverview screen
 *
 * @param label : the label of the button.
 * @param icon : the icon of the button.
 * @param onClick : the action to perform when the button is clicked.
 * @param modifier : the modifier for the button.
 */
@Composable
fun ProfileButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
  Button(
      onClick = onClick,
      colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_dark),
      modifier =
          modifier
              .height((LocalConfiguration.current.screenHeightDp * 0.17f).dp)
              .width((LocalConfiguration.current.screenWidthDp * 0.425f).dp)
              .testTag("ProfileButton")
              .background(color = md_theme_light_dark, shape = RoundedCornerShape(16.dp))) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Icon(
              icon,
              contentDescription = "$label icon",
              tint = md_theme_orange,
              modifier = Modifier.size((LocalConfiguration.current.screenHeightDp * 0.04f).dp))

          Text(
              text = label,
              style = buttonTextStyle(LocalConfiguration.current.screenHeightDp),
              modifier =
                  Modifier.padding(
                      vertical = (LocalConfiguration.current.screenHeightDp * 0.015f).dp))
        }
      }
}
