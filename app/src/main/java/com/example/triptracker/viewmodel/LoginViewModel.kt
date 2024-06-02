package com.example.triptracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.triptracker.authentication.AuthResponse
import com.example.triptracker.model.authentication.SignInResult

/**
 * ViewModel for the login screen.
 *
 * @param application: Application context.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

  /** Enum class to represent the authentication status. */
  enum class AuthStatus {
    LOGGED_IN,
    CREATE_ACCOUNT,
    ERROR
  }

  private val _authResult = MutableLiveData<AuthResponse<SignInResult>>()
  val authResult: LiveData<AuthResponse<SignInResult>> = _authResult

  /**
   * Function to handle the sign-in result.
   */
  fun onSignInResult(result: AuthStatus) {
    _authResult.value =
        when (result) {
          AuthStatus.LOGGED_IN -> AuthResponse.Success()
          AuthStatus.CREATE_ACCOUNT -> AuthResponse.Loading()
          AuthStatus.ERROR -> AuthResponse.Error("Error")
        }
  }
}
