package com.example.triptracker.screens.userProfile

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class UserProfileFollowers(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<UserProfileFollowers>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("FollowersScreen") }) {

  // Structural elements of the UI
  val followingTitle: KNode = child { hasTestTag("FollowersTitle") }
  val goBackButton: KNode = child { hasTestTag("GoBackButton") }
}
