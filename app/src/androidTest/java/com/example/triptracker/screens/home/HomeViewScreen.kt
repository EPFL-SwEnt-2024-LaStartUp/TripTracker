package com.example.triptracker.screens.home

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class HomeViewScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<HomeViewScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("HomeScreen") }) {

  // Structural elements of the UI
  val searchBar: KNode = child { hasTestTag("searchBar") }
  val itinerarySearch: KNode = searchBar.child { hasSetTextAction() }
  val itinerary: KNode = child { hasTestTag("Itinerary") }
  val profilePic: KNode = child { hasTestTag("ProfilePic") }
}
