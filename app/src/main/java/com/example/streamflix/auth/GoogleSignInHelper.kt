package com.example.streamflix.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.streamflix.supabase.SupabaseClient
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleSignInHelper(private val context: Context) {

    private val auth = SupabaseClient.auth
    private var googleSignInClient: GoogleSignInClient

    // Android Client ID
    private val androidClientId = "241431964036-g0fg6cd963ggje248snmrdkj4dlo4s8o.apps.googleusercontent.com"

    init {
        Log.d("GoogleSignIn", "Initializing with Client ID: ${androidClientId.take(30)}...")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidClientId)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
        Log.d("GoogleSignIn", "GoogleSignInClient initialized")
    }

    fun getSignInIntent(): Intent {
        Log.d("GoogleSignIn", "Creating sign-in intent...")
        return googleSignInClient.signInIntent
    }

    fun handleSignInResult(
        data: Intent?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        Log.d("GoogleSignIn", "=== Handling Sign-In Result ===")

        if (data == null) {
            Log.e("GoogleSignIn", "❌ Intent data is null")
            onFailure("No data received from Google Sign-In")
            return
        }

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)

            if (account != null) {
                Log.d("GoogleSignIn", "✅ Google account obtained")
                Log.d("GoogleSignIn", "   Email: ${account.email}")
                Log.d("GoogleSignIn", "   ID Token present: ${account.idToken != null}")
                Log.d("GoogleSignIn", "   ID Token length: ${account.idToken?.length ?: 0}")

                supabaseAuthWithGoogle(account, onSuccess, onFailure)
            } else {
                Log.e("GoogleSignIn", "❌ Google account is null")
                onFailure("Google account is null")
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "❌ ApiException: ${e.statusCode}")
            Log.e("GoogleSignIn", "   Message: ${e.message}")

            val errorMessage = when (e.statusCode) {
                10 -> {
                    Log.e("GoogleSignIn", "   ERROR 10: This should not happen - SHA-1 is correct!")
                    "Configuration error. Please wait 5-10 minutes and try again."
                }
                12500 -> {
                    Log.e("GoogleSignIn", "   ERROR 12500: Google Play Services issue")
                    "Google Play Services error. Please update Google Play Services."
                }
                12501 -> {
                    Log.e("GoogleSignIn", "   ERROR 12501: User cancelled")
                    "Sign-in cancelled"
                }
                7 -> {
                    Log.e("GoogleSignIn", "   ERROR 7: Network error")
                    "Network error. Please check your internet connection."
                }
                else -> {
                    Log.e("GoogleSignIn", "   Unknown error: ${e.statusCode}")
                    "Sign-in failed (Code ${e.statusCode}): ${e.message}"
                }
            }

            onFailure(errorMessage)
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "❌ Unexpected exception", e)
            onFailure("Unexpected error: ${e.message}")
        }
    }

    private fun supabaseAuthWithGoogle(
        account: GoogleSignInAccount,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        Log.d("GoogleSignIn", "=== Starting Supabase Authentication ===")

        val idToken = account.idToken

        if (idToken == null) {
            Log.e("GoogleSignIn", "❌ ID Token is null!")
            Log.e("GoogleSignIn", "   Possible causes:")
            Log.e("GoogleSignIn", "   1. Wrong Client ID (should be Android Client ID)")
            Log.e("GoogleSignIn", "   2. SHA-1 not propagated yet (wait 10 minutes)")
            onFailure("Failed to get Google ID token. Please try again in a few minutes.")
            return
        }

        Log.d("GoogleSignIn", "✅ ID Token obtained (length: ${idToken.length})")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("GoogleSignIn", "Calling Supabase signInWith(IDToken)...")

                auth.signInWith(IDToken) {
                    this.idToken = idToken
                    provider = Google
                }

                Log.d("GoogleSignIn", "✅✅✅ Supabase authentication SUCCESSFUL! ✅✅✅")
                Log.d("GoogleSignIn", "   User email: ${account.email}")

                CoroutineScope(Dispatchers.Main).launch {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "❌ Supabase authentication FAILED!")
                Log.e("GoogleSignIn", "   Exception: ${e::class.simpleName}")
                Log.e("GoogleSignIn", "   Message: ${e.message}")
                Log.e("GoogleSignIn", "   Possible causes:")
                Log.e("GoogleSignIn", "   1. Android Client ID not in Supabase 'Authorized Client IDs'")
                Log.e("GoogleSignIn", "   2. Web Client ID/Secret incorrect in Supabase")
                Log.e("GoogleSignIn", "   3. Google provider not enabled in Supabase")
                e.printStackTrace()

                CoroutineScope(Dispatchers.Main).launch {
                    onFailure("Authentication failed: ${e.message}")
                }
            }
        }
    }

    fun signOut(
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        Log.d("GoogleSignIn", "Signing out...")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signOut()
                Log.d("GoogleSignIn", "✅ Signed out from Supabase")

                CoroutineScope(Dispatchers.Main).launch {
                    googleSignInClient.signOut().addOnCompleteListener {
                        Log.d("GoogleSignIn", "✅ Signed out from Google")
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "❌ Sign out failed", e)
                CoroutineScope(Dispatchers.Main).launch {
                    onFailure("Sign out failed: ${e.message}")
                }
            }
        }
    }

    fun isSignedIn(): Boolean {
        val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
        return googleAccount != null
    }

    fun getCurrentAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}