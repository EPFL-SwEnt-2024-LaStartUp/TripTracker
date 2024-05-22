package com.example.triptracker.view.profile.subviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triptracker.view.theme.Montserrat
import com.example.triptracker.view.theme.md_theme_grey
import com.example.triptracker.view.theme.md_theme_light_dark
import com.example.triptracker.view.theme.md_theme_orange

/**
 * This composable function displays a drop-down selector.
 *
 * @param label : the label of the drop-down selector.
 * @param options : the list of elements to display in the drop-down selector
 * @param selectedOptions : the list of elements the user has selected
 * @param onOptionSelected :
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selectedOptions: List<String>,
    onOptionSelected: (List<String>) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
  var expanded by remember { mutableStateOf(false) }

  Column(modifier = modifier) {
    Text(
        text = label,
        fontSize = 14.sp,
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        color = md_theme_grey,
        modifier = Modifier.padding(bottom = 4.dp),
    )
    OutlinedTextField(
        value =
            if (selectedOptions.isEmpty()) placeholder
            else selectedOptions.joinToString(", ") { it.toString() },
        onValueChange = {},
        readOnly = true,
        placeholder = { Text(placeholder) },
        trailingIcon = {
          IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
          }
        },
        modifier = Modifier.fillMaxWidth().heightIn(min = 65.dp).clickable { expanded = !expanded },
        textStyle =
            TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal),
        colors =
            TextFieldDefaults.outlinedTextFieldColors(
                unfocusedTextColor = md_theme_grey,
                unfocusedBorderColor = md_theme_grey,
                unfocusedLabelColor = md_theme_grey,
                cursorColor = md_theme_grey,
                focusedBorderColor = md_theme_grey,
                focusedLabelColor = Color.White,
            ),
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth().background(md_theme_light_dark)) {
          options.forEach { option ->
            val isSelected = selectedOptions.contains(option.toString())
            DropdownMenuItem(
                text = {
                  Text(option.toString(), color = if (isSelected) md_theme_orange else Color.White)
                },
                onClick = {
                  val newSelection =
                      if (isSelected) {
                        selectedOptions - option.toString()
                      } else {
                        selectedOptions + option.toString()
                      }
                  onOptionSelected(newSelection)
                })
          }
        }
  }
}
