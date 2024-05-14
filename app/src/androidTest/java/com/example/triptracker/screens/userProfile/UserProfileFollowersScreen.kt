package com.example.triptracker.screens.userProfile

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

/** This class represents the UserProfileFollowers Screen and the elements it contains. */
class UserProfileFollowersScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<UserProfileFollowersScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("FollowersScreen") }) {

  // Structural elements of the UI
  val followersTitle: KNode = child { hasTestTag("FollowersTitle") }
  val goBackButton: KNode = child { hasTestTag("GoBackButton") }
    val followersList: KNode = child { hasTestTag("FollowersList") }

    // FriendListView
    val friendsList: KNode = child { hasTestTag("FriendsList") }
    val friendProfile: KNode = child { hasTestTag("FriendProfile") }

    // RemoveFriendButton
    val removeButton: KNode = child { hasTestTag("RemoveButton") }

    // FriendSearchBar
    val searchBar: KNode = child { hasTestTag("SearchBar") }
    val searchBarText: KNode = child { hasTestTag("SearchBarText") }
    val searchIcon: KNode = child { hasTestTag("SearchIcon") }
    val clearButton: KNode = child { hasTestTag("ClearButton") }
}
