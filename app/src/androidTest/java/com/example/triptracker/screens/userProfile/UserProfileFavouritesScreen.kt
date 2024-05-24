package com.example.triptracker.screens.userProfile

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class UserProfileFavouritesScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<UserProfileFavouritesScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("FavouritesScreen") }) {

  val favouritesTitle: KNode = child { hasTestTag("Title") }
  val goBackButton: KNode = child { hasTestTag("GoBackButton") }
  val favouritesList: KNode = child { hasTestTag("FavouritesList") }
}
