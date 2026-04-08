package com.example.streamflix.app.data.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamflix.LoginActivity
import com.example.streamflix.supabase.AuthHelper
import kotlinx.coroutines.launch

@Composable
fun AccountScreen() {
    val context = LocalContext.current
    val authHelper = remember { AuthHelper() }
    val scope = rememberCoroutineScope()

    var userEmail by remember { mutableStateOf("Loading...") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Get user info
    LaunchedEffect(Unit) {
        val user = authHelper.getCurrentUser()
        userEmail = user?.email ?: "Guest"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE50914))
                        .border(3.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userEmail.firstOrNull()?.uppercase() ?: "U",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userEmail,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { /* TODO: Edit profile */ },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Text("Edit Profile")
                }
            }
        }

        // Account Section
        item {
            SectionHeader("Account")
        }

        item {
            SettingsItem(
                icon = Icons.Default.AccountCircle,
                title = "Profile Settings",
                onClick = { /* TODO */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Change Password",
                onClick = { /* TODO: Navigate to change password */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Email,
                title = "Email Preferences",
                onClick = { /* TODO */ }
            )
        }

        // App Settings Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("App Settings")
        }

        item {
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                onClick = { /* TODO */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Download,
                title = "Download Settings",
                onClick = { /* TODO */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Videocam,
                title = "Video Quality",
                onClick = { /* TODO */ }
            )
        }

        // Help Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("Help & Support")
        }

        item {
            SettingsItem(
                icon = Icons.Default.Help,
                title = "Help Center",
                onClick = { /* TODO */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "About",
                onClick = { /* TODO */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.PrivacyTip,
                title = "Privacy Policy",
                onClick = { /* TODO */ }
            )
        }

        // Logout Button
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE50914)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", fontSize = 16.sp)
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Version 1.0.0",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", color = Color.White) },
            text = { Text("Are you sure you want to logout?", color = Color.Gray) },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            authHelper.signOut(
                                onSuccess = {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                },
                                onError = { }
                            )
                        }
                    }
                ) {
                    Text("Logout", color = Color(0xFFE50914))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color.White)
                }
            },
            containerColor = Color(0xFF1A1A1A)
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFFE50914),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}