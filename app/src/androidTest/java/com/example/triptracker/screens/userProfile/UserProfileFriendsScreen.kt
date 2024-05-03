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
  val friendsTitle: KNode = child { hasTestTag("FriendsFinderTitle") }
  val goBackButton: KNode = child { hasTestTag("GoBackButton") }
  val friendsList: KNode = child { hasTestTag("FriendsList") }
  val removeButton: KNode = child { hasTestTag("RemoveButton") }
  val friendProfile: KNode = child { hasTestTag("FriendProfile") }
}
