package com.example.triptracker.view.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.triptracker.R
import com.example.triptracker.view.theme.md_theme_light_outlineVariant

/** This file contains helper function for the layout of the UserProfile screen. */

/**
 * This function returns a TextStyle for the big number displayed in the UserProfile screen.
 *
 * @param size : the size of the screen.
 * @return a TextStyle for the big number.
 */
@Composable
fun bigNumberStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.024f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.inverseSurface,
      textAlign = TextAlign.Center,
      letterSpacing = (size * 0.0005f).sp)
}

/**
 * This function returns a TextStyle for the category text displayed in the UserProfile screen.
 *
 * @param size : the size of the screen.
 * @return a TextStyle for the category text.
 */
@Composable
fun categoryTextStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.012f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.Light,
      color = MaterialTheme.colorScheme.inverseSurface,
      textAlign = TextAlign.Center,
      letterSpacing = (size * 0.0005f).sp)
}

/**
 * This function returns a TextStyle for the button text displayed in the UserProfile screen.
 *
 * @param size : the size of the screen.
 * @return a TextStyle for the button text.
 */
@Composable
fun buttonTextStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.020f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.SemiBold,
      color = md_theme_light_outlineVariant,
      letterSpacing = (size * 0.0005f).sp)
}

/**
 * This function returns a TextStyle for the secondary title displayed in the UserProfile screen.
 *
 * @param size : the size of the screen.
 * @return a TextStyle for the secondary title.
 */
@Composable
fun secondaryTitleStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.014f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight(400),
      color = MaterialTheme.colorScheme.inverseSurface,
      textAlign = TextAlign.Right,
      letterSpacing = (size * 0.0005f).sp)
}

/**
 * This function returns a TextStyle for the secondary content displayed in the UserProfile screen.
 *
 * @param size : the size of the screen.
 * @return a TextStyle for the secondary content.
 */
@Composable
fun secondaryContentStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.012f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight(400),
      color = MaterialTheme.colorScheme.inverseSurface,
      textAlign = TextAlign.Right,
      letterSpacing = (size * 0.0005f).sp)
}
