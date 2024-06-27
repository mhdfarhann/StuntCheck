package com.farhan.tugasakhir.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.farhan.tugasakhir.components.ButtonComponent
import com.farhan.tugasakhir.components.ClickableRegTextComponent
import com.farhan.tugasakhir.components.ClickableTextComponent
import com.farhan.tugasakhir.components.EmailSentDialog
import com.farhan.tugasakhir.components.ForgotPasswordDialog
import com.farhan.tugasakhir.components.HeadingTextComponent
import com.farhan.tugasakhir.components.MyTextFieldComponent
import com.farhan.tugasakhir.components.NormalTextComponent
import com.farhan.tugasakhir.components.PasswordTextFieldComponent
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.viewmodel.LoginViewModel
import com.farhan.tugasakhir.uievent.LoginUIEvent
import com.farhan.tugasakhir.navigation.Screen

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var showEmailSentDialog by remember { mutableStateOf(false) }

    BackHandler {
        navController.navigate(Screen.WelcomeScreen.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeadingTextComponent(value = stringResource(id = R.string.StuntCheck))
            NormalTextComponent(value = stringResource(id = R.string.YukLogin))
            Spacer(modifier = Modifier.height(40.dp))

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.icon_email),
                onTextSelected = {
                    loginViewModel.onEvent(LoginUIEvent.EmailChanged(it), navController)
                },
                errorStatus = loginViewModel.loginUIState.value.emailError,
                errorMessage = "Invalid email"
            )

            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                lockIcon = painterResource(id = R.drawable.icon_lock),
                visibilityIcon = painterResource(id = R.drawable.icon_visibility),
                visibilityOffIcon = painterResource(id = R.drawable.icon_visibility_off),
                onTextSelected = {
                    loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it), navController)
                },
                errorStatus = loginViewModel.loginUIState.value.passwordError,
                errorMessage = "Password setidaknya memiliki 8 karakter"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickableTextComponent(value = stringResource(id = R.string.lupa_password)) {
                showForgotPasswordDialog = true
            }

            ClickableRegTextComponent(value = stringResource(id = R.string.DaftarDulu)) {
                navController.navigate(Screen.RegisterScreen.route)
            }

            ButtonComponent(value = stringResource(id = R.string.login), onButtonClicked = {
                loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked, navController)
            }, isEnabled = loginViewModel.allValidationPassed.value)
        }

        if (loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }

    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            onDismiss = { showForgotPasswordDialog = false },
            onSendEmail = { email ->
                loginViewModel.resetPassword(email) {
                    showForgotPasswordDialog = false
                    showEmailSentDialog = true
                }
            }
        )
    }

    if (showEmailSentDialog) {
        EmailSentDialog(onDismiss = { showEmailSentDialog = false })
    }

    if (loginViewModel.loginFailed.value) {
        AlertDialog(
            onDismissRequest = { loginViewModel.loginFailed.value = false },
            title = { Text(text = "Login Gagal") },
            text = { Text("Email atau password salah. Silakan coba lagi.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        loginViewModel.loginFailed.value = false
                    }
                ) {
                    Text(
                        "OK",
                        color = colorResource(id = R.color.light_blue),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = colorScheme.surface
        )
    }
}









