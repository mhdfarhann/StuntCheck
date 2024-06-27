package com.farhan.tugasakhir.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ControlPoint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ControlPoint
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.farhan.tugasakhir.R
import com.farhan.tugasakhir.data.model.BottomNavigationItem
import com.farhan.tugasakhir.data.rules.Validator
import com.farhan.tugasakhir.navigation.Screen


@Composable
fun HeadingTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp)
            .heightIn(min = 60.dp),
        style = TextStyle(
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
        , color = colorResource(id = R.color.light_blue),
        textAlign = TextAlign.Center
    )
}

@Composable
fun NormalTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal
        )
        ,  color = colorScheme.onBackground,
        textAlign = TextAlign.Center
    )
}

@Composable
fun WelcomeTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal
        )
        , color = colorScheme.onBackground,
        textAlign = TextAlign.Center
    )
}


@Composable
fun MyTextFieldComponent(
    labelValue: String,
    painterResource: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    errorMessage: String = ""
) {
    val textValue = remember { mutableStateOf("") }

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorScheme.surface),
            textStyle = TextStyle(
                color = colorScheme.onBackground
            ),
            label = { Text(text = labelValue) },
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = colorScheme.onBackground,
                unfocusedLabelColor = colorScheme.onBackground,
                cursorColor = colorScheme.onBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = if (errorStatus) Color.Red else Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            maxLines = 1,
            value = textValue.value,
            onValueChange = {
                textValue.value = it
                onTextSelected(it)
            },
            leadingIcon = {
                Icon(painter = painterResource, contentDescription = "")
            },
            isError = errorStatus
        )
        if (errorStatus) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


@Composable
fun PasswordTextFieldComponent(
    labelValue: String,
    lockIcon: Painter,
    visibilityIcon: Painter,
    visibilityOffIcon: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    errorMessage: String = ""
) {
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    Column {
        TextField(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorScheme.surface),
            textStyle = TextStyle(
                color = colorScheme.onBackground
            ),
            label = { Text(text = labelValue) },
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = colorScheme.onBackground,
                unfocusedLabelColor = colorScheme.onBackground,
                cursorColor = colorScheme.onBackground,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = if (errorStatus) Color.Red else Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            singleLine = true,
            maxLines = 1,
            value = password.value,
            onValueChange = {
                password.value = it
                onTextSelected(it)
            },
            leadingIcon = {
                Icon(painter = lockIcon, contentDescription = "")
            },
            trailingIcon = {
                Row {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        val iconPainter = if (passwordVisible.value) visibilityOffIcon else visibilityIcon
                        Icon(painter = iconPainter, contentDescription = "")
                    }
                }
            },
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            isError = errorStatus
        )
        if (errorStatus) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


@Composable
fun ClickableRegTextComponent(value: String, onTextSelected : (String) -> Unit) {
    val initialText = "Belum Punya akun?, Yuk "
    val regText = "Daftar Dulu"
    val textColor = colorScheme.onBackground

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = textColor)) {
            append(initialText)
        }
        withStyle(style = SpanStyle(color = colorResource(id = R.color.light_blue), fontWeight = FontWeight.Bold)) {
            pushStringAnnotation(tag = regText, annotation = regText)
            append(regText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .heightIn(min = 60.dp),
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString, onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableRegTextComponent", "{${span.item}}")

                    if (span.item == regText) {
                        onTextSelected(span.item)
                    }
                }
        }
    )
}


@Composable
fun ClickableLogTextComponent(value: String, onTextSelected : (String) -> Unit) {
    val initialText = "Sudah Punya akun?, Yuk "
    val logText = "Login"
    val textColor = colorScheme.onBackground

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = textColor)) {
            append(initialText)
        }
        withStyle(style = SpanStyle(color = colorResource(id = R.color.light_blue), fontWeight = FontWeight.Bold) ) {
            pushStringAnnotation(tag = logText, annotation = logText)
            append(logText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .heightIn(min = 60.dp),
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString, onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableLogTextComponent", "{${span.item}}")

                    if (span.item == logText) {
                        onTextSelected(span.item)
                    }
                }
        }
    )
}


@Composable
fun ButtonComponent(value: String, onButtonClicked : () -> Unit, isEnabled : Boolean = false){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
        onButtonClicked.invoke()
    },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        enabled = isEnabled
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    color = colorResource(id = R.color.light_blue),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.white)
            )
        }

    }
}


@Composable
fun ButtonStartComponent(
    value: String,
    navController: NavController
){
    Button(onClick = {
        navController.navigate(Screen.LoginScreen.route)
    },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue))
    ) {

        Text(text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

    }

}


@Composable
fun ToolComponent(
    photoUrl: Painter,
    nameTool: String,
    description: String,
    navController: NavController,
    destination: String
) {
    val isDarkMode = isSystemInDarkTheme()

    val backgroundColor = if (isDarkMode) {
        colorScheme.surface
    } else {
        colorResource(id = R.color.light_blue)
    }

    val textColor = if (isDarkMode) {
        Color.White
    } else {
        Color.Black
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val boxSize = when {
        screenWidth < 390.dp -> 70.dp
        screenWidth < 400.dp -> 80.dp
        else -> 80.dp
    }

    val fontSize = when {
        screenWidth < 390.dp -> 18.sp
        screenWidth < 400.dp -> 20.sp
        else -> 20.sp
    }

    val fontSizeTxt = when {
        screenWidth < 390.dp -> 12.sp
        screenWidth < 400.dp -> 14.sp
        else -> 14.sp
    }

    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(destination)
            },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(boxSize)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.white))
            ) {
                Image(
                    painter = photoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .scale(1f)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nameTool,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSize,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic
                    ),
                    color = textColor,
                    fontSize = fontSizeTxt
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .padding(top = 5.dp, end = 10.dp),
                tint = textColor
            )
        }
    }
}

@Composable
fun ToolComponentCheck(
    photoUrl: Painter,
    nameTool: String,
    description: String,
    navController: NavController
) {
    ToolComponent(photoUrl, nameTool, description, navController, Screen.CheckScreen.route)
}

@Composable
fun ToolComponentList(
    photoUrl: Painter,
    nameTool: String,
    description: String,
    navController: NavController
) {
    ToolComponent(photoUrl, nameTool, description, navController, Screen.ListScreen.route)
}

@Composable
fun ToolComponentAdd(
    photoUrl: Painter,
    nameTool: String,
    description: String,
    navController: NavController
) {
    ToolComponent(photoUrl, nameTool, description, navController, Screen.AddScreen.route)
}


@Composable
fun HeadingToolsTextComponent(value: String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 40.dp)
            .heightIn(min = 60.dp),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
        , color = colorScheme.onSurface,
    )
}



@Composable
fun AppBottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Medical",
            selectedIcon = Icons.Filled.ControlPoint,
            unselectedIcon = Icons.Outlined.ControlPoint
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val selectedItemIndex = when (currentDestination?.route) {
        Screen.HomeScreen.route -> 0
        Screen.MapScreen.route -> 1
        Screen.ProfileScreen.route -> 2
        else -> 0
    }

    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, bottomNavigationItem ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    val route = when (index) {
                        0 -> Screen.HomeScreen.route
                        1 -> Screen.MapScreen.route
                        2 -> Screen.ProfileScreen.route
                        else -> Screen.HomeScreen.route
                    }
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        BadgedBox(badge = {}) {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    bottomNavigationItem.selectedIcon
                                } else bottomNavigationItem.unselectedIcon,
                                contentDescription = bottomNavigationItem.title,
                                tint = if (index == selectedItemIndex) {
                                    colorScheme.onBackground
                                } else {
                                    colorScheme.onBackground
                                }
                            )
                        }
                        Text(
                            text = bottomNavigationItem.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (index == selectedItemIndex) {
                                colorScheme.onBackground
                            } else {
                                colorScheme.onBackground
                            }
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun ForgotPasswordDialog(
    onDismiss: () -> Unit,
    onSendEmail: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Reset Password", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        error = false
                    },
                    label = { Text("Email") },
                    isError = error
                )
                if (error) {
                    Text(text = "Email tidak valid", color = Color.Red)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue))
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            if (Validator.validateEmail(email).status) {
                                onSendEmail(email)
                            } else {
                                error = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue))
                    ) {
                        Text("Kirim")
                    }
                }
            }
        }
    }
}

@Composable
fun EmailSentDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Email Telah Dikirim", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Silakan periksa email Anda untuk mengatur ulang kata sandi Anda.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.light_blue))
                ) {
                    Text("OK")
                }
            }
        }
    }
}


@Composable
fun ClickableTextComponent(value: String, onTextSelected: () -> Unit) {
    ClickableText(
        text = AnnotatedString(value),
        modifier = Modifier.padding(5.dp),
        onClick = { onTextSelected() },
        style = TextStyle(
            color = colorResource(id = R.color.light_blue),
            fontWeight = FontWeight.Bold
        )
    )
}