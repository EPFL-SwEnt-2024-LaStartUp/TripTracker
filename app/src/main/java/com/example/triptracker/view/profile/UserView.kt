package com.example.triptracker.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.Navigation
import com.example.triptracker.viewmodel.UserProfileViewModel
import java.lang.reflect.Modifier

/**
 * This composable function displays the user profile of the user passed as a parameter.
 * It is used to access more detailed information about a user (as his/her saved and recorded trips)
 *
 * @param navigation : the navigation object to navigate to other screens.
 * @param userProfile : the user profile to display.
 * @param userProfileViewModel : the view model to handle the user profile.
 */
@Composable
fun UserView(
    navigation: Navigation,
    userProfile: UserProfile,
    userProfileViewModel: UserProfileViewModel
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.height(100.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

            }
        },
        bottomBar = { NavigationBar(navigation) },
        modifier = Modifier.fillMaxSize().testTag("UserView")
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }

    }
}