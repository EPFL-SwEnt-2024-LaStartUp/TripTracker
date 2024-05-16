package com.example.triptracker.view.profile

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.triptracker.R
import com.example.triptracker.view.theme.md_theme_dark_gray
import com.example.triptracker.view.theme.md_theme_light_dark

/** This file contains helper function for the layout of the UserProfile screen. */
fun bigNumberStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.024f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.Bold,
      color = md_theme_light_dark,
      textAlign = TextAlign.Center,
      letterSpacing = (size * 0.0005f).sp)
}

fun categoryTextStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.012f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.Light,
      color = md_theme_dark_gray,
      textAlign = TextAlign.Center,
      letterSpacing = (size * 0.0005f).sp)
}

fun buttonTextStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.020f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight.SemiBold,
      color = md_theme_dark_gray,
      letterSpacing = (size * 0.0005f).sp)
}

fun secondaryTitleStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.014f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight(400),
      color = md_theme_light_dark,
      textAlign = TextAlign.Right,
      letterSpacing = (size * 0.0005f).sp)
}

fun secondaryContentStyle(size: Int): TextStyle {
  return TextStyle(
      fontSize = (size * 0.012f).sp,
      lineHeight = (size * 0.016f).sp,
      fontFamily = FontFamily(Font(R.font.montserrat)),
      fontWeight = FontWeight(400),
      color = md_theme_dark_gray,
      textAlign = TextAlign.Right,
      letterSpacing = (size * 0.0005f).sp)
}
