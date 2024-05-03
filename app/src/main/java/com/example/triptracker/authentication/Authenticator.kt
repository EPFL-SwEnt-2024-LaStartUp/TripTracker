package com.example.triptracker.authentication

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

/**
 * Interface for authentication that provides methods for signing in, signing out, and checking if a
 * user is signed in.
 */
interface Authenticator {

  var signInLauncher: ActivityResultLauncher<Intent>?

  fun createSignInIntent(applicationContext: Context): Intent

  fun signIn(signInIntent: Intent)

  fun signOut()

  fun delete()
}
