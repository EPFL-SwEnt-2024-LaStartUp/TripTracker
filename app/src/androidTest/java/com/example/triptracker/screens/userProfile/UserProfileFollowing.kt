package com.example.triptracker.screens.userProfile

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class UserProfileFollowing(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<UserProfileFollowing>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("FollowingScreen") }) {

  // Structural elements of the UI
  val followingTitle: KNode = child { hasTestTag("FollowingTitle") }
  val goBackButton: KNode = child { hasTestTag("GoBackButton") }
}
