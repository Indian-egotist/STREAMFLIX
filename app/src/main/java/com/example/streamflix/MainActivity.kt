package com.example.streamflix

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.streamflix.app.data.ui.screens.HomeScreen
import com.example.streamflix.app.data.ui.theme.StreamFlixTheme
import com.example.streamflix.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if user is authenticated
        checkAuthenticationStatus()

        setContent {
            StreamFlixTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    HomeScreen()
                }
            }
        }
    }

    private fun checkAuthenticationStatus() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val currentUser = SupabaseClient.auth.currentUserOrNull()

                if (currentUser == null) {
                    // User is not authenticated, redirect to LoginActivity
                    navigateToLogin()
                }
                // If user is authenticated, stay on MainActivity
            } catch (e: Exception) {
                // Error checking auth status, redirect to login to be safe
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        // Recheck authentication when activity resumes
        checkAuthenticationStatus()
    }
}