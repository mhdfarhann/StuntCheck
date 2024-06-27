package com.farhan.tugasakhir.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.farhan.tugasakhir.components.ButtonStartComponent
import com.farhan.tugasakhir.components.HeadingTextComponent
import com.farhan.tugasakhir.components.WelcomeTextComponent
import com.farhan.tugasakhir.R

@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(20.dp)
    ) {
        HeadingTextComponent(value = stringResource(id = R.string.StuntCheck))
        Spacer(modifier = Modifier.height(10.dp))
        WelcomeTextComponent(value = stringResource(id = R.string.welcome_msg))

        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = "",
                modifier = Modifier.size(280.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(40.dp))

        ButtonStartComponent(
            value = stringResource(id = R.string.mulai_sekarang),
            navController = navController
        )
    }
}

