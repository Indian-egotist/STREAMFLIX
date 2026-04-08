package com.example.streamflix

import android.app.Application
import android.util.Log
import com.example.streamflix.supabase.SupabaseClient

class StreamFlixApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d("StreamFlixApp", "Application started - Initializing Supabase")

        // Initialize Supabase client
        // This ensures Supabase is ready before any Activity/Fragment uses it
        try {
            SupabaseClient.initialize(this)
            Log.d("StreamFlixApp", "✅ Supabase initialized successfully")
        } catch (e: Exception) {
            Log.e("StreamFlixApp", "❌ Failed to initialize Supabase: ${e.message}", e)
        }
    }
}