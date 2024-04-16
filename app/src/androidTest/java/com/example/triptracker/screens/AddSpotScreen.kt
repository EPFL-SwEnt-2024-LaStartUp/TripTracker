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
  val locationBox: KNode = locationRow.child { hasTestTag("LocationBox") }
  val locationText: KNode = locationBox.child { hasTestTag("LocationText") }
  private val locationDropDownMenu: KNode = locationBox.child { hasTestTag("LocationDropDown") }
  val inputLocationProposal: KNode = locationDropDownMenu.child { hasClickAction() }

  //  val locationDropDown: KNode = locationBox.child { hasTestTag("LocationDropDown") }
  //  val locationDropDownItem: KNode = locationDropDown.child { hasTestTag("LocationDropDownItem")
  // }
  val description: KNode = child { hasTestTag("SpotDescription") }
  val pictures: KNode = child { hasTestTag("SpotPictures") }
  val saveButton: KNode = child { hasTestTag("SaveButton") }

  val editPicture: KNode = child { hasTestTag("EditPicture") }
}
