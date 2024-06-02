package com.example.triptracker.authentication

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/** Implementation of the Authenticator interface for Google sign-in. */
class GoogleAuthenticator(
    //    private val context: Context
) : Authenticator {

  override var signInLauncher: ActivityResultLauncher<Intent>? = null

  /**
   * Creates a sign-in intent for Google sign-in.
   *
   * @param applicationContext The context of the application.
   * @return The sign-in intent.
   */
  override fun createSignInIntent(applicationContext: Context): Intent {
    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestProfile()
            .build()
    val googleSignInClient = GoogleSignIn.getClient(applicationContext, gso)
    return googleSignInClient.signInIntent
  }

  /**
   * Signs in the user using Google sign-in.
   *
   * @param signInIntent The sign-in intent.
   */
  override fun signIn(signInIntent: Intent) {
    signInLauncher?.launch(signInIntent)
  }

  /**
   * Signs out the user using Google sign-in.
   *
   * @param context The context of the application.
   */
  override fun signOut(context: Context) {
    Firebase.auth.signOut()
    GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
  }

  /**
   * Checks if a user is signed in using Google sign-in.
   *
   * @param context The context of the application.
   * @return True if the user is signed in, false otherwise.
   */
  override fun isSignedIn(context: Context): Boolean {
    return GoogleSignIn.getLastSignedInAccount(context) != null
  }

  /**
   * Gets the signed-in account using Google sign-in.
   *
   * @param context The context of the application.
   * @return The signed-in account.
   */
  fun getSignedInAccount(context: Context) = GoogleSignIn.getLastSignedInAccount(context)
}
