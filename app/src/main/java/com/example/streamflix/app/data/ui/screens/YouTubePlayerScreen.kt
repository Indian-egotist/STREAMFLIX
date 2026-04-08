package com.example.streamflix.app.data.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateOf
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants

@Composable
fun YouTubePlayerScreen(
    youtubeUrlOrId: String,
    title: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val videoId = remember(youtubeUrlOrId) { extractYouTubeVideoId(youtubeUrlOrId) }

    val hasError = remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .align(Alignment.TopCenter),
            factory = { ctx ->
                YouTubePlayerView(ctx).apply {
                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            if (videoId.isNotBlank()) {
                                youTubePlayer.loadVideo(videoId, 0f)
                            } else {
                                hasError.value = true
                            }
                        }

                        override fun onError(
                            youTubePlayer: YouTubePlayer,
                            error: PlayerConstants.PlayerError
                        ) {
                            hasError.value = true
                        }
                    })
                }
            }
        )

        if (hasError.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Can’t play this video inside the app.", color = Color.White)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        val url =
                            if (youtubeUrlOrId.startsWith("http", ignoreCase = true)) youtubeUrlOrId
                            else "https://www.youtube.com/watch?v=$videoId"

                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                ) {
                    Text("Watch on YouTube")
                }
            }
        }
    }
}

/**
 * Supports:
 * - "dQw4w9WgXcQ" (id)
 * - https://youtu.be/dQw4w9WgXcQ
 * - https://www.youtube.com/watch?v=dQw4w9WgXcQ
 * - https://www.youtube.com/embed/dQw4w9WgXcQ
 */
fun extractYouTubeVideoId(input: String): String {
    val s = input.trim()

    // Already looks like a video id (basic heuristic)
    val idRegex = Regex("^[a-zA-Z0-9_-]{11}$")
    if (idRegex.matches(s)) return s

    // watch?v=
    Regex("[?&]v=([a-zA-Z0-9_-]{11})").find(s)?.groupValues?.getOrNull(1)?.let { return it }

    // youtu.be/
    Regex("youtu\\.be/([a-zA-Z0-9_-]{11})").find(s)?.groupValues?.getOrNull(1)?.let { return it }

    // /embed/
    Regex("youtube\\.com/embed/([a-zA-Z0-9_-]{11})").find(s)?.groupValues?.getOrNull(1)?.let { return it }

    return ""
}