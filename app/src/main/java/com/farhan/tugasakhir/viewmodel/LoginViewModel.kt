package com.farhan.tugasakhir.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.farhan.tugasakhir.uievent.LoginUIEvent
import com.farhan.tugasakhir.uistate.LoginUIState
import com.farhan.tugasakhir.data.rules.Validator
import com.farhan.tugasakhir.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())
    val allValidationPassed = mutableStateOf(false)
    val loginInProgress = mutableStateOf(false)
    val loginFailed = mutableStateOf(false) // State untuk menandakan kegagalan login

    private var emailTouched = mutableStateOf(false)
    private var passwordTouched = mutableStateOf(false)

    fun onEvent(event: LoginUIEvent, navController: NavController?) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                emailTouched.value = true
                loginUIState.value = loginUIState.value.copy(email = event.email)
                validateLoginUIDataWithRules()
            }
            is LoginUIEvent.PasswordChanged -> {
                passwordTouched.value = true
                loginUIState.value = loginUIState.value.copy(password = event.password)
                validateLoginUIDataWithRules()
            }
            is LoginUIEvent.LoginButtonClicked -> {
                navController?.let { login(it) }
            }
        }
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(loginUIState.value.email)
        val passwordResult = Validator.validatePassword(loginUIState.value.password)

        loginUIState.value = loginUIState.value.copy(
            emailError = emailTouched.value && !emailResult.status,
            passwordError = passwordTouched.value && !passwordResult.status
        )

        allValidationPassed.value = emailResult.status && passwordResult.status
    }

    private fun login(navController: NavController) {
        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                loginInProgress.value = false
                if (task.isSuccessful) {
                    navController.navigate(Screen.HomeScreen.route)
                } else {
                    loginFailed.value = true // Set state jika login gagal
                }
            }
            .addOnFailureListener {
                loginInProgress.value = false
                loginFailed.value = true // Set state jika login gagal
            }
    }

    fun resetPassword(email: String, onComplete: () -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Reset password email sent.")
                } else {
                    task.exception?.let {
                        Log.e(TAG, "Error sending reset password email", it)
                    }
                }
                onComplete()
            }
    }
}



