package com.example.triptracker.screens.userProfile

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

/** This class represents the UserProfileFollowers Screen and the elements it contains. */
class UserView(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<UserView>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("UserView") }) {

  // Structural elements of the UI
  val usernameTitle: KNode = child { hasTestTag("UsernameTitle") }
  val goBackButton: KNode = child { hasTestTag("GoBackButton") }
  val profilePicture: KNode = child { hasTestTag("ProfilePicture") }
  val nameAndSurname: KNode = child { hasTestTag("NameAndSurname") }
  val interestTitle: KNode = child { hasTestTag("InterestTitle") }
  val interestList: KNode = child { hasTestTag("InterestList") }
  val travelStyleTitle: KNode = child { hasTestTag("TravelStyleTitle") }
  val travelStyleList: KNode = child { hasTestTag("TravelStyleList") }
  val languagesTitle: KNode = child { hasTestTag("LanguagesTitle") }
  val languagesList: KNode = child { hasTestTag("LanguagesList") }
  val followingButton: KNode = child { hasTestTag("FollowingButton") }
  val tripsTitle: KNode = child { hasTestTag("TripsTitle") }
  val tripsCount: KNode = child { hasTestTag("TripsCount") }
  val followersTitle: KNode = child { hasTestTag("FollowersTitle") }
  val followersCount: KNode = child { hasTestTag("FollowersCount") }
  val followingTitle: KNode = child { hasTestTag("FollowingTitle") }
  val followingCount: KNode = child { hasTestTag("FollowingCount") }
}
