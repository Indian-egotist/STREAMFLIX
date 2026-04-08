package com.example.streamflix.app.data.ui.screens

import androidx.compose.runtime.Composable

@Composable
fun PlayerScreen(
    videoUrl: String,
    title: String,
    onBackClick: () -> Unit
) {
    val isYouTube = videoUrl.contains("youtube.com", ignoreCase = true) ||
            videoUrl.contains("youtu.be", ignoreCase = true)

    if (isYouTube) {
        YouTubePlayerScreen(
            youtubeUrlOrId = videoUrl,
            title = title,
            onBackClick = onBackClick
        )
    } else {
        VideoPlayerScreen(
            videoUrl = videoUrl,
            title = title,
            onBackClick = onBackClick
        )
    }
}