package com.example.streamflix.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInHelper(private val context: Context) {

    private val auth:  FirebaseAuth = FirebaseAuth. getInstance()
    private var googleSignInClient: GoogleSignInClient

    // TODO: Replace with your actual Web Client ID from Firebase Console
    private val webClientId = "241431964036-9qttv94t5gqu666gnvgag34ogprvbf2d.apps.googleusercontent.com"

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleSignInResult(
        data: Intent?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account, onSuccess, onFailure)
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed", e)
            onFailure("Google sign-in failed: ${e.message}")
        }
    }

    private fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(account?. idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("GoogleSignIn", "Firebase authentication successful")
                    onSuccess()
                } else {
                    Log.e("GoogleSignIn", "Firebase authentication failed", task.exception)
                    onFailure(task.exception?. message ?: "Authentication failed")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}