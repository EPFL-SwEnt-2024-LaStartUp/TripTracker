package com.example.triptracker.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AddSpotScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AddSpotScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AddSpotScreen") }) {

  // Structural elements of the UI
  val title: KNode = child { hasTestTag("SpotTitle") }
  val locationRow: KNode = child { hasTestTag("SpotLocation") }
  val locationText: KNode = locationRow.child { hasTestTag("LocationText") }
  private val locationDropDownMenu: KNode = locationRow.child { hasTestTag("LocationDropDown") }
  val inputLocationProposal: KNode = locationDropDownMenu.child { hasClickAction() }
  val description: KNode = child { hasTestTag("SpotDescription") }
  val pictures: KNode = child { hasTestTag("SpotPictures") }
  val saveButton: KNode = child { hasTestTag("SaveButton") }

  val editPicture: KNode = child { hasTestTag("EditPicture") }
}
