package com.example.streamflix

import android.app.Application
//import android.util.log
import com.google.firebase.FirebaseApp

class StreamFlixApp : Application() {

    override fun onCreate() {
        super.onCreate()
        //log.d("StreamFlixApp", "StreamFlixApp.onCreate() CALLED") <-- use for filter Logcat by name StreamFlixApp


        // GUARANTEES Firebase is initialized before any UI or Auth call
        FirebaseApp.initializeApp(this)
    }
}
//[CONFIGURATION_NOT_FOUND] bug was because Firebase didn't fully initialize at run-time