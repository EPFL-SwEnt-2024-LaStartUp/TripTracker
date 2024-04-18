package com.example.triptracker.screens.map

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class RecordScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<RecordScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("RecordScreen") }) {

  val recordTitle: KNode = child { hasTestTag("RecordTitle") }
  val recordButton: KNode = child { hasTestTag("RecordButton") }
  val currentLocation: KNode = child { hasTestTag("CurrentLocation") }
  val timerTitle: KNode = child { hasTestTag("TimerTitle") }
}
