package com.farhan.tugasakhir


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.farhan.tugasakhir.app.StuntCheckApp
import com.farhan.tugasakhir.data.model.femaleStandards
import com.farhan.tugasakhir.data.model.maleStandards
import com.farhan.tugasakhir.ui.theme.TugasAkhirTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        installSplashScreen()

        setContent {
            TugasAkhirTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StuntCheckApp(maleStandards = maleStandards, femaleStandards = femaleStandards)
                }
            }

        }
    }
}


