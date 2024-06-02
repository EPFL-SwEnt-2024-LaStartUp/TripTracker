package com.example.triptracker.authentication

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

/**
 * Interface for authentication that provides methods for signing in, signing out, and checking if a
 * user is signed in.
 */
interface Authenticator {

  /* Activity result launcher for sign in */
  var signInLauncher: ActivityResultLauncher<Intent>?

  /* Function to create a sign in intent */
  fun createSignInIntent(applicationContext: Context): Intent

  /* Function to sign in the user */
  fun signIn(signInIntent: Intent)

  /* Function to sign out the user */
  fun signOut(context: Context)

  /* Function to check if the user is signed in */
  fun isSignedIn(context: Context): Boolean
}
