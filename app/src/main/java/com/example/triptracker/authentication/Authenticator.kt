package com.example.triptracker.authentication

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface Authenticator {

  var signInLauncher: ActivityResultLauncher<Intent>?

  fun createSignInIntent(applicationContext: Context): Intent

  fun signIn(signInIntent: Intent)

  fun signOut()

  fun delete()

  fun isSignedIn(): Boolean
}
