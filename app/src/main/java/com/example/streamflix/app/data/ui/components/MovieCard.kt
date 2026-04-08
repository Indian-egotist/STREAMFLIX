package com.example.streamflix.app.data.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation. clickable
import androidx.compose. foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx. compose.ui.Modifier
import androidx.compose.ui. layout.ContentScale
import androidx. compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.streamflix.model. Movie

@Composable
fun MovieCard(
    movie: Movie,
    onClick: (Movie) -> Unit
) {
    Card(
        modifier = Modifier
            . width(120.dp)
            .height(180.dp)
            .clickable { onClick(movie) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(movie.thumbnailUrl),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}