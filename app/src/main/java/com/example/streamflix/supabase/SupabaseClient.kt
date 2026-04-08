package com.example.streamflix.supabase

import android.content.Context
import android.util.Log
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

object SupabaseClient {

    private const val SUPABASE_URL = "https://pxgbfohiprahsbsjoyiz.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB4Z2Jmb2hpcHJhaHNic2pveWl6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjkzNDQwNTIsImV4cCI6MjA4NDkyMDA1Mn0.rR9MMvm3LC5kWArb0nbKAjAIdeRUUOIV8EzWb_La0Tc"

    // Lazy initialization - created only when first accessed
    val client by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
        }
    }

    // Auth client shortcut
    val auth get() = client.auth

    // Postgrest (Database) client shortcut
    val database get() = client.postgrest

    // Storage client shortcut
    val storage get() = client.storage

    /**
     * Initialize Supabase client (called from Application class)
     * This ensures Supabase is ready before any Activity uses it
     */
    fun initialize(context: Context) {
        Log.d("SupabaseClient", "Initializing Supabase client...")
        // Access client to trigger lazy initialization
        client
        Log.d("SupabaseClient", "Supabase client initialized with URL: $SUPABASE_URL")
    }

    /**
     * Check if client is initialized
     */
    fun isInitialized(): Boolean {
        return try {
            client
            true
        } catch (e: Exception) {
            Log.e("SupabaseClient", "Supabase not initialized: ${e.message}")
            false
        }
    }
}