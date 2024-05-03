package com.example.triptracker.screens.userProfile

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class UserProfileMyTripsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<UserProfileMyTripsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("UserProfileMyTripsScreen") }) {

  val myTripsTitle: KNode = child { hasTestTag("MyTripsTitle") }
  val goBackButton: KNode = child { hasTestTag("GoBackButton") }
  val myTripsList: KNode = child { hasTestTag("MyTripsList") }
}
