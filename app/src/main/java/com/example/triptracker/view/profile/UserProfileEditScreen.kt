package com.example.triptracker.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.view.NavTopBar
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.NavigationBar
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_light_error
import com.example.triptracker.view.theme.md_theme_orange
import java.util.Date

@Composable
fun UserProfileEditScreen(navigation: Navigation) {
    val profile = // TODO: replace with actual user profile
        UserProfile(
            "mail", "name", "surname", Date(), "pseudo", "profileImageUrl", listOf(), listOf())

    var mail by remember { mutableStateOf("") }
    var isMailEmpty by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var isNameEmpty by remember { mutableStateOf(false) }

    var date by remember { mutableStateOf("") }

    Scaffold(
        topBar = { NavTopBar(title = "Edit Profile", canNavigateBack = true, navigateUp = { navigation.goBack() },
            actions = {SaveButton()})
        },
    modifier = Modifier.testTag("UserProfileEditScreen")) { innerPadding ->
        Box(
            modifier =
            Modifier.fillMaxHeight()
                .fillMaxWidth()
                .padding(innerPadding)
                .fillMaxSize()
                .background(md_theme_light_dark, shape = RoundedCornerShape(35.dp)),
            contentAlignment = Alignment.TopCenter) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = "Edit your profile",
                    fontSize = 36.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 50.dp, start = 30.dp, end = 30.dp),
                )

                // Text field for the name
                Text(
                    text = "Enter your name",
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = md_theme_grey,
                    modifier = Modifier.padding(top = 50.dp, start = 30.dp, end = 30.dp),
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        isNameEmpty = it.isEmpty() // Update empty state
                    },
                    label = {
                        Text(
                            text = "Name",
                            fontSize = 14.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    },
                    modifier =
                    Modifier.fillMaxWidth(1f)
                        .padding(top = 5.dp, bottom = 5.dp, start = 30.dp, end = 30.dp),
                    textStyle =
                    TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Normal),
                    colors =
                    OutlinedTextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        unfocusedBorderColor =
                        if (isNameEmpty) md_theme_light_error else md_theme_grey,
                        unfocusedLabelColor =
                        if (isNameEmpty) md_theme_light_error else md_theme_grey,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                    ))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.width(20.dp))
                    // add a button to save the description

                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
        }
    }
}

@Composable
fun SaveButton() {
    FilledTonalButton(
        onClick = {
            // TODO: add logic when save button is clicked
        },
        colors =
        ButtonDefaults.filledTonalButtonColors(
            containerColor = md_theme_orange, contentColor = Color.White),
    ) {
        Text(
            text = "Save",
            fontSize = 18.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            color = Color.White)
    }
}

@Preview
@Composable
        /* Visual preview of the User Profile Edit Screen */
fun UserProfileEditScreenPreview() {
    val navController = rememberNavController()
    val navigation = remember(navController) { Navigation(navController) }
    UserProfileEditScreen(navigation)
}
