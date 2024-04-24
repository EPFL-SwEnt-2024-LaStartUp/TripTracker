package com.example.triptracker.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.triptracker.R
import com.example.triptracker.view.theme.md_theme_grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTopBar(
    modifier: Modifier = Modifier,
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
  if (canNavigateBack) {
    TopAppBar(
        title = { Text(text = title) },
        actions = { actions() },
        navigationIcon = {
          IconButton(onClick = navigateUp) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = md_theme_grey)
          }
        },
        modifier = modifier)
  } else {
    TopAppBar(title = { Text(text = title) }, actions = { actions() }, modifier = modifier)
  }
}
