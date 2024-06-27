package com.farhan.tugasakhir.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.farhan.tugasakhir.components.ButtonComponent
import com.farhan.tugasakhir.components.ClickableLogTextComponent
import com.farhan.tugasakhir.components.HeadingTextComponent
import com.farhan.tugasakhir.components.MyTextFieldComponent
import com.farhan.tugasakhir.components.NormalTextComponent
import com.farhan.tugasakhir.components.PasswordTextFieldComponent
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.viewmodel.RegisterViewModel
import com.farhan.tugasakhir.uievent.RegisterUIEvent
import com.farhan.tugasakhir.navigation.Screen

@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel = viewModel()) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadingTextComponent(value = stringResource(id = R.string.StuntCheck))
            NormalTextComponent(value = stringResource(id = R.string.DaftarDulu))
            Spacer(modifier = Modifier.height(40.dp))

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.nama),
                painterResource(id = R.drawable.icon_person),
                onTextSelected = {
                    registerViewModel.onEvent(RegisterUIEvent.NameChanged(it), navController, context)
                },
                errorStatus = registerViewModel.registerUIState.value.namaError
            )

            MyTextFieldComponent(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.icon_email),
                onTextSelected = {
                    registerViewModel.onEvent(RegisterUIEvent.EmailChanged(it), navController, context)
                },
                errorStatus = registerViewModel.registerUIState.value.emailError,
                errorMessage = "Email tidak valid"
            )
            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                lockIcon = painterResource(id = R.drawable.icon_lock),
                visibilityIcon = painterResource(id = R.drawable.icon_visibility),
                visibilityOffIcon = painterResource(id = R.drawable.icon_visibility_off),
                onTextSelected = {
                    registerViewModel.onEvent(RegisterUIEvent.PasswordChanged(it), navController, context)
                },
                errorStatus = registerViewModel.registerUIState.value.passwordError,
                errorMessage = "Password harus minimal 8 karakter"
            )

            ClickableLogTextComponent(value = stringResource(id = R.string.YukLogin)) {
                navController.navigate(Screen.LoginScreen.route)
            }

            ButtonComponent(value = stringResource(id = R.string.daftar), onButtonClicked = {
                registerViewModel.onEvent(RegisterUIEvent.RegisterButtonClicked, navController, context)
            }, isEnabled = registerViewModel.allValidationPassed.value)
        }

        if (registerViewModel.registerInProgress.value) {
            CircularProgressIndicator()
        }
    }

    if (registerViewModel.emailAlreadyRegistered.value) {
        AlertDialog(
            onDismissRequest = { registerViewModel.emailAlreadyRegistered.value = false },
            title = { Text(text = "Email Sudah Terdaftar") },
            text = { Text("Email yang Anda masukkan sudah terdaftar. Silakan gunakan email lain atau login.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        registerViewModel.emailAlreadyRegistered.value = false
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




