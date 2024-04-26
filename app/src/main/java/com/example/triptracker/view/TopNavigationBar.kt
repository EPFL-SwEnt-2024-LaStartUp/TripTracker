package com.example.triptracker.view

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.R
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_orange

/**
 * A top bar for the navigation.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param title The title of the top bar.
 * @param canNavigateBack Whether the top bar should have a back button.
 * @param navigateUp The action to perform when the back button is clicked.
 * @param actions The actions to display on the top bar (like a save button).
 */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun NavTopBar(
//    modifier: Modifier = Modifier,
//    title: String,
//    canNavigateBack: Boolean,
//    navigateUp: () -> Unit = {},
//    actions: @Composable () -> Unit = {}
//) {
//  if (canNavigateBack) {
//    TopAppBar(
//        title = { Text(text = title) },
//        actions = { actions() },
//        navigationIcon = {
//          IconButton(onClick = navigateUp) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back",
//                tint = md_theme_grey)
//          }
//        },
//        modifier = modifier)
//  } else {
//    TopAppBar(title = { Text(text = title) }, actions = { actions() }, modifier = modifier)
//  }
//}

/** Preview for the NavTopBar to have an example. */
//@Preview
//@Composable
//fun NavTopBarPreview() {
//  NavTopBar(
//      title = stringResource(id = R.string.app_name),
//      canNavigateBack = true,
//      navigateUp = {},
//      actions = {
//        FilledTonalButton(
//            onClick = {},
//            colors = ButtonDefaults.filledTonalButtonColors(containerColor = md_theme_orange),
//            modifier = Modifier.width(90.dp).height(30.dp),
//        ) {
//          Text(
//              text = "Save",
//              fontSize = 14.sp,
//              fontFamily = Montserrat,
//              fontWeight = FontWeight.SemiBold,
//              color = Color.White)
//        }
//      })
//}
