package com.farhan.tugasakhir.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.farhan.tugasakhir.uievent.RegisterUIEvent
import com.farhan.tugasakhir.uistate.RegisterUIState
import com.farhan.tugasakhir.data.rules.Validator
import com.farhan.tugasakhir.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

        var registerUIState = mutableStateOf(RegisterUIState())
        val allValidationPassed = mutableStateOf(false)
        var registerInProgress = mutableStateOf(false)
        var emailAlreadyRegistered = mutableStateOf(false) // New state for email already registered

        private var emailTouched = mutableStateOf(false)
        private var passwordTouched = mutableStateOf(false)

        fun onEvent(event: RegisterUIEvent, navController: NavController, context: Context) {
                when (event) {
                        is RegisterUIEvent.NameChanged -> {
                                registerUIState.value = registerUIState.value.copy(
                                        nama = event.nama
                                )
                        }
                        is RegisterUIEvent.EmailChanged -> {
                                emailTouched.value = true
                                registerUIState.value = registerUIState.value.copy(
                                        email = event.email
                                )
                                validateDataWithRules()
                        }
                        is RegisterUIEvent.PasswordChanged -> {
                                passwordTouched.value = true
                                registerUIState.value = registerUIState.value.copy(
                                        password = event.password
                                )
                                validateDataWithRules()
                        }
                        is RegisterUIEvent.RegisterButtonClicked -> {
                                signUp(navController, context)
                        }
                }
        }

        private fun signUp(navController: NavController, context: Context) {
                createUserInFirebase(
                        email = registerUIState.value.email,
                        password = registerUIState.value.password,
                        navController = navController,
                        context = context
                )
        }

        private fun validateDataWithRules() {
                val emailResult = Validator.validateEmail(registerUIState.value.email)
                val passwordResult = Validator.validatePassword(registerUIState.value.password)

                registerUIState.value = registerUIState.value.copy(
                        emailError = emailTouched.value && !emailResult.status,
                        passwordError = passwordTouched.value && !passwordResult.status
                )

                allValidationPassed.value = emailResult.status && passwordResult.status
        }

        private fun createUserInFirebase(email: String, password: String, navController: NavController, context: Context) {
                registerInProgress.value = true

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                                registerInProgress.value = false
                                if (task.isSuccessful) {
                                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                                        userId?.let { id ->
                                                val user = hashMapOf(
                                                        "name" to registerUIState.value.nama,
                                                        "email" to email
                                                )
                                                FirebaseFirestore.getInstance().collection("users").document(id)
                                                        .set(user)
                                                        .addOnSuccessListener {
                                                                Toast.makeText(context, "Berhasil buat akun", Toast.LENGTH_SHORT).show()
                                                                navController.navigate(Screen.LoginScreen.route)
                                                        }
                                                        .addOnFailureListener { e ->
                                                                Toast.makeText(context, "Error saving user: $e", Toast.LENGTH_SHORT).show()
                                                        }
                                        }
                                } else {
                                        if (task.exception is FirebaseAuthUserCollisionException) {
                                                emailAlreadyRegistered.value = true
                                        } else {
                                                Toast.makeText(context, "Gagal buat akun", Toast.LENGTH_SHORT).show()
                                        }
                                }
                        }
                        .addOnFailureListener {
                                registerInProgress.value = false
                                if (it is FirebaseAuthUserCollisionException) {
                                        emailAlreadyRegistered.value = true
                                } else {
                                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
        }
}

