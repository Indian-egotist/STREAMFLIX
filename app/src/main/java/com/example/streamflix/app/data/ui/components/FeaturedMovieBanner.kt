package com.example.streamflix.app.data.ui.components

import androidx.compose.foundation. Image
import androidx.compose.foundation. background
import androidx.compose.foundation. clickable
import androidx.compose. foundation.layout.*
import androidx. compose.material3.Text
import androidx.compose.runtime. Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics. Brush
import androidx.compose.ui.graphics.Color
import androidx. compose.ui.layout.ContentScale
import androidx.compose.ui. text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose. ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com. example.streamflix.model.Movie

@Composable
fun FeaturedMovieBanner(
    movie: Movie,
    onClick: (Movie) -> Unit  // ← ADD THIS
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .clickable { onClick(movie) }  // ← ADD CLICK HANDLER
    ) {
        // Background Image
        Image(
            painter = rememberAsyncImagePainter(model = movie.bannerUrl.ifEmpty { movie.thumbnailUrl } ),
            contentDescription = movie.title,
            modifier = Modifier. fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color. Transparent,
                            Color. Black.copy(alpha = 0.7f),
                            Color.Black
                        ),
                        startY = 300f
                    )
                )
        )

        // Title at Bottom
        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}