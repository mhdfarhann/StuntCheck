package com.farhan.tugasakhir.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.farhan.tugasakhir.components.AppBottomNavigation
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.viewmodel.HomeViewModel
import com.farhan.tugasakhir.viewmodel.ProfileScreenViewModel


@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigateBack: () -> Unit,
    navController: NavController,
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val userName by viewModel.userName.observeAsState("User")
    val profileImageUrl by viewModel.profileImageUrl.observeAsState("")
    val email by viewModel.email.observeAsState("user@example.com")
    val isProfileImageUpdated by viewModel.isProfileImageUpdated.observeAsState(null)
    val isPasswordChanged by viewModel.isPasswordChanged.observeAsState(null)
    val passwordChangeError by viewModel.passwordChangeError.observeAsState(null)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showUsernameDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                viewModel.updateProfileImage(it)
            }
        }
    )

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        bottomBar = {
            AppBottomNavigation(navController = navController)
        },
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val (background, profileImage, displayName, cardContent, loadingIndicator) = createRefs()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(colorResource(id = R.color.light_blue))
                        .constrainAs(background) {
                            top.linkTo(parent.top)
                        }
                )

                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .size(170.dp)
                        .padding(16.dp)
                        .constrainAs(profileImage) {
                            top.linkTo(background.bottom, margin = (-95).dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    val imagePainter = if (profileImageUrl.isNotEmpty()) {
                        rememberAsyncImagePainter(profileImageUrl)
                    } else {
                        rememberAsyncImagePainter("https://i.pinimg.com/736x/5e/39/6b/5e396bb1b17681759922dd10f8a9d702.jpg")
                    }

                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(170.dp)
                            .clip(CircleShape)
                            .border(2.dp, colorScheme.onSurface, CircleShape)
                    )

                    IconButton(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.BottomEnd)
                            .background(colorScheme.surface, shape = CircleShape)
                            .border(1.dp, colorScheme.onSurface, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile Picture",
                            tint = colorScheme.onSurface
                        )
                    }
                }

                val displayNameText = userName.ifEmpty { "Unknown" }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .constrainAs(displayName) {
                            top.linkTo(profileImage.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Text(
                        text = displayNameText,
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal
                        ),
                        textAlign = TextAlign.Center,
                        color = colorScheme.onBackground
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .constrainAs(cardContent) {
                            top.linkTo(displayName.bottom, margin = 40.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Email Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Email",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Normal,
                                    color = colorScheme.onSurface
                                ),
                            )
                            Text(
                                text = email,
                                style = TextStyle(
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Normal,
                                    color = colorScheme.onSurface
                                ),
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Change Password Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDialog = true }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Ganti Password",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Normal,
                                    color = colorScheme.onSurface
                                ),
                            )
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Ganti Password",
                                tint = colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Change Username Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showUsernameDialog = true }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Ganti Username",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Normal,
                                    color = colorScheme.onSurface
                                ),
                            )
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = "Ganti Username",
                                tint = colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Logout Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { homeViewModel.logout(navController) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Logout",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Normal,
                                    color = colorScheme.error
                                ),
                            )
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Logout",
                                tint = colorScheme.error
                            )
                        }
                    }
                }

                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = colorScheme.primary,
                        modifier = Modifier.constrainAs(loadingIndicator) {
                            top.linkTo(profileImage.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }

                when (isProfileImageUpdated) {
                    true -> {
                        Toast.makeText(context, "Foto berhasil diganti", Toast.LENGTH_SHORT).show()
                        viewModel.resetProfileImageUpdateState()
                    }
                    false -> {
                        Text(
                            "Failed to update profile image",
                            color = colorScheme.error,
                            modifier = Modifier.constrainAs(createRef()) {
                                top.linkTo(cardContent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )
                        viewModel.resetProfileImageUpdateState()
                    }
                    null -> { /* Do nothing */ }
                }

                when (isPasswordChanged) {
                    true -> {
                        Toast.makeText(context, "Password berhasil diganti", Toast.LENGTH_SHORT).show()
                        viewModel.resetPasswordChangeState()
                    }
                    false -> {
                        Text(
                            "Failed to change password",
                            color = colorScheme.error,
                            modifier = Modifier.constrainAs(createRef()) {
                                top.linkTo(cardContent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )
                        passwordChangeError?.let {
                            Text(
                                it,
                                color = colorScheme.error,
                                modifier = Modifier.constrainAs(createRef()) {
                                    top.linkTo(cardContent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                            )
                        }
                        viewModel.resetPasswordChangeState()
                    }
                    null -> { /* Do nothing */ }
                }
            }

            if (showDialog) {
                PasswordChangeDialog(
                    onDismiss = { showDialog = false },
                    onPasswordChange = { newPassword, confirmPassword ->
                        viewModel.changePassword(newPassword, confirmPassword)
                        showDialog = false
                    },
                    passwordChangeError = passwordChangeError,
                    visibilityIcon = painterResource(id = R.drawable.icon_visibility),
                    visibilityOffIcon = painterResource(id = R.drawable.icon_visibility_off)
                )
            }

            if (showUsernameDialog) {
                UsernameChangeDialog(
                    currentUsername = userName,
                    onDismiss = { showUsernameDialog = false },
                    onUsernameChange = { newUsername ->
                        viewModel.updateUsername(newUsername)
                        showUsernameDialog = false
                    }
                )
            }
        }
    )
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameChangeDialog(
    currentUsername: String,
    onDismiss: () -> Unit,
    onUsernameChange: (String) -> Unit
) {
    var newUsername by remember { mutableStateOf(currentUsername) }
    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Username", color = colorScheme.onSurface) },
        text = {
            Column {
                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("New Username", color = colorScheme.onSurface) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.onSurface
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onUsernameChange(newUsername) }
            ) {
                Text("Change", color = colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onDismiss) {
                Text("Cancel", color = colorScheme.onSurface)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordChangeDialog(
    onDismiss: () -> Unit,
    onPasswordChange: (String, String) -> Unit,
    passwordChangeError: String?,
    visibilityIcon: Painter,
    visibilityOffIcon: Painter,
    errorStatus: Boolean = false,
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localErrorMessage by remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password", color = colorScheme.onSurface) },
        text = {
            Column {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password", color = colorScheme.onSurface) },
                    isError = errorStatus,
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                            Icon(
                                painter = if (passwordVisible.value) visibilityIcon else visibilityOffIcon,
                                contentDescription = null,
                                tint = colorScheme.onSurface
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password", color = colorScheme.onSurface) },
                    isError = errorStatus,
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                            Icon(
                                painter = if (passwordVisible.value) visibilityIcon else visibilityOffIcon,
                                contentDescription = null,
                                tint = colorScheme.onSurface
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.onSurface
                    )
                )

                passwordChangeError?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        it,
                        color = colorScheme.error,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newPassword == confirmPassword) {
                        onPasswordChange(newPassword, confirmPassword)
                    } else {
                        localErrorMessage = "Passwords do not match"
                    }
                }
            ) {
                Text("Change", color = colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onDismiss) {
                Text("Cancel", color = colorScheme.onSurface)
            }
        }
    )
}




