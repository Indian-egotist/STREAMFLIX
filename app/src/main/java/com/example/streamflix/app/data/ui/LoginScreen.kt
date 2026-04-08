package com.example.streamflix.app.data.ui

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamflix.MainActivity
import com.example.streamflix.auth.GoogleSignInHelper
import com.example.streamflix.supabase.AuthHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToSignup: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
) {
    val context = LocalContext.current
    val authHelper = remember { AuthHelper() }
    val googleSignInHelper = remember { GoogleSignInHelper(context) }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("LoginScreen", "=== Google Sign-In Result ===")
        Log.d("LoginScreen", "Result code: ${result.resultCode}")

        when (result.resultCode) {
            Activity.RESULT_OK -> {
                Log.d("LoginScreen", "✅ RESULT_OK received")
                isLoading = true

                googleSignInHelper.handleSignInResult(
                    data = result.data,
                    onSuccess = {
                        Log.d("LoginScreen", "✅ Login successful!")
                        isLoading = false
                        Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    },
                    onFailure = { error ->
                        Log.e("LoginScreen", "❌ Login failed: $error")
                        isLoading = false
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                )
            }

            Activity.RESULT_CANCELED -> {
                Log.w("LoginScreen", "⚠️ RESULT_CANCELED received")
                isLoading = false

                // Check if it's an error or user cancellation
                result.data?.let { data ->
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        task.getResult(ApiException::class.java)
                    } catch (e: ApiException) {
                        Log.e("LoginScreen", "❌ Error Code: ${e.statusCode}")

                        val errorMsg = when (e.statusCode) {
                            10 -> "Configuration error: Check SHA-1 fingerprint in Google Console"
                            12500 -> "Update Google Play Services"
                            12501 -> "Sign-in cancelled"
                            else -> "Error code ${e.statusCode}: ${e.message}"
                        }

                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                        return@rememberLauncherForActivityResult
                    }
                }

                Toast.makeText(context, "Sign-in cancelled", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Log.e("LoginScreen", "❌ Unexpected result code: ${result.resultCode}")
                isLoading = false
                Toast.makeText(context, "Sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Auto-login check
    LaunchedEffect(Unit) {
        val isLoggedIn = authHelper.isUserLoggedIn()
        if (isLoggedIn) {
            context.startActivity(Intent(context, MainActivity::class.java))
            (context as? Activity)?.finish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Text(
                text = "STREAMFLIX",
                color = Color(0xFFE50914),
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign in to continue",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Email, "Email", tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF333333),
                    unfocusedContainerColor = Color(0xFF333333),
                    focusedBorderColor = Color(0xFFE50914),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                enabled = !isLoading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, "Password", tint = Color.Gray)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            if (passwordVisible) "Hide" else "Show",
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF333333),
                    unfocusedContainerColor = Color(0xFF333333),
                    focusedBorderColor = Color(0xFFE50914),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                enabled = !isLoading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            Text(
                text = "Forgot Password?",
                color = Color(0xFFE50914),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(enabled = !isLoading) { onNavigateToForgotPassword() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    scope.launch {
                        authHelper.signIn(
                            email = email.trim(),
                            password = password,
                            onSuccess = {
                                isLoading = false
                                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, MainActivity::class.java))
                                (context as? Activity)?.finish()
                            },
                            onError = { error ->
                                isLoading = false
                                Toast.makeText(context, "Login failed: $error", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                enabled = !isLoading,
                shape = RoundedCornerShape(25.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Text("  OR  ", color = Color.Gray, fontSize = 14.sp)
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign-In Button
            OutlinedButton(
                onClick = {
                    Log.d("LoginScreen", "🔵 Google button clicked")
                    isLoading = true

                    try {
                        val signInIntent = googleSignInHelper.getSignInIntent()
                        googleSignInLauncher.launch(signInIntent)
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "❌ Failed to launch", e)
                        isLoading = false
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(25.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "G",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4285F4)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Continue with Google",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Google Sign-In launcher
            val googleSignInLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                Log.d("LoginScreen", "=== Google Sign-In Result ===")
                Log.d("LoginScreen", "Result code: ${result.resultCode}")

                // ALWAYS reset loading state
                isLoading = false

                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d("LoginScreen", "✅ RESULT_OK - Processing...")

                        googleSignInHelper.handleSignInResult(
                            data = result.data,
                            onSuccess = {
                                Log.d("LoginScreen", "🎉 Login successful!")
                                Toast.makeText(context, "Welcome to StreamFlix!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                                (context as? Activity)?.finish()
                            },
                            onFailure = { error ->
                                Log.e("LoginScreen", "❌ Login failed: $error")
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            }
                        )
                    }

                    Activity.RESULT_CANCELED -> {
                        Log.w("LoginScreen", "⚠️ RESULT_CANCELED")
                        Toast.makeText(context, "Sign-in cancelled", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Log.e("LoginScreen", "❌ Unexpected result code: ${result.resultCode}")
                        Toast.makeText(context, "Sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // Sign Up
            Row {
                Text("New to StreamFlix? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Sign up now",
                    color = Color(0xFFE50914),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(enabled = !isLoading) { onNavigateToSignup() }
                )
            }
        }
    }
}