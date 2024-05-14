package com.example.triptracker.screens.userProfile

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

/** This class represents the UserProfileFriends Screen and the elements it contains. */
class UserProfileFriendsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<UserProfileFriendsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("FriendsFinderScreen") }) {

  // Structural elements of the UI

  // UserProfileFriends
  val friendsTitle: KNode = child { hasTestTag("FriendsFinderTitle") }
  val goBackButton: KNode = child { hasTestTag("GoBackButton") }

  // FriendListView
  val friendsList: KNode = child { hasTestTag("FriendsList") }
  val friendProfile: KNode = child { hasTestTag("FriendProfile") }
  val notDisplayingProfile: KNode = child { hasTestTag("NotDisplayingProfile") }
}
