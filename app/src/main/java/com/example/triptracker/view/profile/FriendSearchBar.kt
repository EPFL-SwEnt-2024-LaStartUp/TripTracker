package com.example.triptracker.view.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.R
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.viewmodel.UserProfileViewModel

/**
 * This composable function will display a search bar for the user to search for friends.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendSearchBar(
    userProfile: UserProfile,
    onSearchActivated: (Boolean) -> Unit,
    viewModel: UserProfileViewModel,
    isNoResultFound: Boolean = false,
) {
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    // If the search bar is active (in focus or contains text), we'll consider it active.
    var isActive by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        SearchBar(
            modifier =
            Modifier.fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 5.dp)
                .testTag("searchBar"),
            query = searchText,
            onQueryChange = { newText ->
                searchText = newText
                viewModel.setSearchQuery(newText)
                onSearchActivated(isActive)
                isActive = newText.isNotEmpty() || focusManager.equals(true)
            },
            onSearch = {
                viewModel.setSearchQuery(searchText)
                isActive = false
            },
            leadingIcon = {
                if (!isActive) {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "Menu")
                }
            },
            trailingIcon = {
                if (isActive) {
                    Icon(
                        modifier =
                        Modifier.clickable {
                            if (searchText.isEmpty()) {
                                isActive = false // Deactivate the search bar if text is empty
                            } else {
                                searchText = "" // Clear the text but keep the search bar active
                                viewModel.setSearchQuery(searchText) // Reset search query
                            }
                            onSearchActivated(isActive)
                        }
                            .testTag("ClearButton"),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear text field")
                }
            },
            active = isActive,
            onActiveChange = { activeState ->
                isActive = activeState
                if (!activeState) { // When deactivating, clear the search text.
                    searchText = ""
                    viewModel.setSearchQuery("") // Reset search query
                }
            },
            placeholder = {
                Text(
                    "Find Users",
                    modifier = Modifier.padding(start = 10.dp).testTag("searchBarText"),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.15.sp,
                    color = md_theme_grey
                )
            },
            shape = MaterialTheme.shapes.small.copy(CornerSize(50)),
        ) {
            LaunchedEffect(searchText) { onSearchActivated(isActive) }

            if (isNoResultFound) {
                Text(
                    "No results found",
                    modifier = Modifier.padding(16.dp).testTag("NoResultsFound"),
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.15.sp,
                    color = Color.Red)
            }
        }
    }
}