package com.example.triptracker.view.map

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.R
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_light_black
import com.example.triptracker.view.theme.md_theme_light_onPrimary
import com.example.triptracker.view.theme.md_theme_orange
import com.example.triptracker.viewmodel.RecordViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun AddSpot(recordViewModel: RecordViewModel, LatLng: LatLng) {

    var boxDisplayed by remember { mutableStateOf(true) }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    when (boxDisplayed) {
        true ->
        Box(modifier =
        Modifier
            .fillMaxSize()
            .padding(15.dp)
            .background(color = md_theme_light_black, shape = RoundedCornerShape(35.dp))){
            Column (modifier = Modifier.matchParentSize()){
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    IconButton(
                        modifier = Modifier.padding(10.dp),
                        onClick = { boxDisplayed = false }) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close", tint = md_theme_light_onPrimary)
                    }
                }

                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Text(text = "Add New Spot To Path", color = md_theme_orange, fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontWeight = FontWeight.Normal,
                        fontSize = 30.sp,)
                }

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                    horizontalArrangement = Arrangement.Center) {
                    TextField(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        value = location,
                        placeholder = { Text("Name of the spot", color = md_theme_light_onPrimary) },
                        onValueChange = { location = it },
                        label = { Text("Name of the spot", color = md_theme_light_onPrimary) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Map,
                                contentDescription = "Location",
                                tint = md_theme_orange
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedLabelColor = md_theme_light_onPrimary,
                            focusedLabelColor = md_theme_orange,
                            cursorColor = md_theme_orange,
                            backgroundColor = md_theme_dark_gray,
                            focusedIndicatorColor = md_theme_orange,
                        ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        minLines = 1,
                        maxLines = 1
                    )
                }

                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    TextField(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                            .fillMaxWidth()
                            .onFocusChanged { },
                        value = description,
                        placeholder = { Text("Give a short description of the spot you want to add to the path !", color = md_theme_light_onPrimary) },
                        onValueChange = { description = it },
                        label = { Text("Description", color = md_theme_light_onPrimary) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Description,
                                contentDescription = "Description",
                                tint = md_theme_orange
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            unfocusedLabelColor = md_theme_light_onPrimary,
                            focusedLabelColor = md_theme_orange,
                            cursorColor = md_theme_orange,
                            backgroundColor = md_theme_dark_gray,
                            focusedIndicatorColor = md_theme_orange,
                        ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        minLines = 5,
                        maxLines = 5,
                    )
                }


                Row (modifier = Modifier.fillMaxWidth().fillMaxHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                    FilledTonalButton(
                        onClick = {
                            //TODO
                            },
                        modifier = Modifier
                            .padding(50.dp)
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(0.1f),
                        colors =
                        ButtonDefaults.filledTonalButtonColors(
                            containerColor = md_theme_orange, contentColor = Color.White),
                    ) {
                        Text(
                            text = "Save",
                            fontSize = 24.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White)
                    }
                }
            }

        }
        false ->{}
    }

}


@Preview
@Composable
fun AddSpotPreview() {
    AddSpot(RecordViewModel(), LatLng(46.5249808, 6.5750806))
}