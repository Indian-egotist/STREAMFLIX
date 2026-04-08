package com.example.streamflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen //splash_screen
import androidx.compose. ui.graphics.Color
import com.example.streamflix.app.data.ui.LoginScreen
import com.example.streamflix.app.data.ui.theme.StreamFlixTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamFlixTheme {
                Surface(
                    modifier = Modifier. fillMaxSize(),
                    color = Color.Black
                ) {
                    LoginScreen()
                }
            }
        }
    }
}