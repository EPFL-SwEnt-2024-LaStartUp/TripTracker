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
  val removeButton: KNode = child { hasTestTag("RemoveButton") }
  val followerProfile: KNode = child { hasTestTag("FriendProfile") }
}
