package com.example.triptracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.triptracker.authentication.AuthResponse
import com.example.triptracker.model.authentication.SignInResult

object loggedUser {
  var email: String? = ""
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

  private val _authResult = MutableLiveData<AuthResponse<SignInResult>>()
  val authResult: LiveData<AuthResponse<SignInResult>> = _authResult

  fun onSignInResult(
      result: Boolean,
      userName: String? = "",
      email: String? = "",
      photoUrl: String? = ""
  ) {
    _authResult.value =
        if (result) {
          loggedUser.email = email
          AuthResponse.Success(SignInResult(userName, email, photoUrl))
        } else {
          AuthResponse.Error("Error")
        }
  }
}
