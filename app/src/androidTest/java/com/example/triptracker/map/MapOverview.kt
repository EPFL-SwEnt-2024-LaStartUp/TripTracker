package com.example.triptracker.map

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.example.triptracker.view.map.MapOverview
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MapOverview(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MapOverview>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("MapOverview") }) {

  // Structural elements of the UI
  val mapCityName: KNode = child { hasTestTag("cityName") }
  val askUserLocationScreen: KNode = child { hasTestTag("askUserLocationScreen") }
}
