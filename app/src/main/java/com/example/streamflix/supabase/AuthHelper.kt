package com.example.streamflix.supabase

import android.util.Log
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo

class AuthHelper {

    private val auth = SupabaseClient.auth

    suspend fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d("AuthHelper", "Sign up successful")
            onSuccess()
        } catch (e: Exception) {
            Log.e("AuthHelper", "Sign up failed", e)
            onError(e.message ?: "Sign up failed")
        }
    }

    suspend fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d("AuthHelper", "Sign in successful")
            onSuccess()
        } catch (e: Exception) {
            Log.e("AuthHelper", "Sign in failed", e)
            onError(e.message ?: "Sign in failed")
        }
    }

    suspend fun signOut(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            auth.signOut()
            Log.d("AuthHelper", "Sign out successful")
            onSuccess()
        } catch (e: Exception) {
            Log.e("AuthHelper", "Sign out failed", e)
            onError(e.message ?: "Sign out failed")
        }
    }

    suspend fun getCurrentUser(): UserInfo? {
        return try {
            auth.currentUserOrNull()
        } catch (e: Exception) {
            Log.e("AuthHelper", "Failed to get current user", e)
            null
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        return getCurrentUser() != null
    }

    suspend fun resetPassword(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Use your Supabase project URL as redirectTo
            auth.resetPasswordForEmail(
                email = email,
                redirectUrl = "https://pxgbfohiprahsbsjoyiz.supabase.co/auth/v1/verify"
            )
            Log.d("AuthHelper", "✅ Password reset email sent to: $email")
            onSuccess()
        } catch (e: Exception) {
            Log.e("AuthHelper", "❌ Password reset failed", e)
            onError(e.message ?: "Failed to send reset email")
        }
    }

    suspend fun updatePassword(
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            auth.updateUser {
                password = newPassword
            }
            Log.d("AuthHelper", "Password updated successfully")
            onSuccess()
        } catch (e: Exception) {
            Log.e("AuthHelper", "Password update failed", e)
            onError(e.message ?: "Password update failed")
        }
    }
}