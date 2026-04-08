import java.lang.module.ModuleFinder.compose

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.streamflix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.streamflix"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("${System.getProperty("user.home")}/.android/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
}

// ⚠️ Task MUST be OUTSIDE android {} block
tasks.register("getDebugSha1") {
    doLast {
        val signingConfig = android.signingConfigs.getByName("debug")
        val keystore = signingConfig.storeFile

        println("=".repeat(50))
        println("Keystore path: ${keystore?.absolutePath}")
        println("Keystore exists: ${keystore?.exists()}")
        println("=".repeat(50))

        if (keystore?.exists() == true) {
            try {
                val command = listOf(
                    "keytool",
                    "-list",
                    "-v",
                    "-keystore", keystore.absolutePath,
                    "-alias", "androiddebugkey",
                    "-storepass", "android",
                    "-keypass", "android"
                )

                val process = ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start()

                val output = process.inputStream.bufferedReader().readText()
                process.waitFor()

                println(output)
            } catch (e: Exception) {
                println("ERROR running keytool: ${e.message}")
                e.printStackTrace()
            }
        } else {
            println("ERROR: Debug keystore not found!")
            println("Expected location: ${keystore?.absolutePath}")
            println("")
            println("Solution: Build the app first to generate the keystore:")
            println("  ./gradlew assembleDebug")
        }
    }
}

dependencies {
    //Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM and UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Extended icons
    implementation("androidx.compose.material:material-icons-extended")

    // Splash Screen API
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ExoPlayer for video playback
    implementation("androidx.media3:media3-exoplayer:1.9.0")
    implementation("androidx.media3:media3-ui:1.9.0")
    implementation("androidx.media3:media3-common:1.9.0")

    //YOUTUBE PLAYER
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.5.0")

    // Credentials
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.2")

    // Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.2"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")

    // Ktor client for Supabase
    implementation("io.ktor:ktor-client-android:3.0.1")
    implementation("io.ktor:ktor-client-core:3.0.1")
    implementation("io.ktor:ktor-utils:3.0.1")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}