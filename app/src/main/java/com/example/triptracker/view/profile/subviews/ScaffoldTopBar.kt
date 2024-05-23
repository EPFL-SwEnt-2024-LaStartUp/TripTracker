package com.example.triptracker.view.profile.subviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.theme.Montserrat

/**
 * This composable function displays the top bar of the scaffold for user profile views
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param label : the label to display in the top bar.
 */
@Composable
fun ScaffoldTopBar(navigation: Navigation, label: String) {
  Row(
      modifier =
          Modifier.height((LocalConfiguration.current.screenHeightDp * 0.075).dp).fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start) {
        // Button to navigate back to the user profile
        Button(
            onClick = { navigation.goBack() },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.testTag("GoBackButton")) {
              Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        Text(
            text = label,
            style =
                TextStyle(
                    fontSize = (LocalConfiguration.current.screenHeightDp * 0.03f).sp,
                    lineHeight = (LocalConfiguration.current.screenHeightDp * 0.016f).sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left,
                    letterSpacing = (LocalConfiguration.current.screenHeightDp * 0.0005f).sp,
                ),
            modifier =
                Modifier.width((LocalConfiguration.current.screenHeightDp * 0.67f).dp)
                    .wrapContentHeight()
                    .testTag("Title"))
      }
}
