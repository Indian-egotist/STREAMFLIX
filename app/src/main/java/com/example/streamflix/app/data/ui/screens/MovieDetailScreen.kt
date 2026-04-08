package com.example.streamflix.app.data.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.streamflix.model.Movie

@Composable
fun MovieDetailScreen(
    movie: Movie,
    onPlayClick: (Movie) -> Unit,   // ✅ UPDATED
    onBackClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }

    val canPlay = movie.videoUrl.isNotBlank() // ✅ OPTIONAL SAFETY

    val duration = movie.durationMinutes?.let { minutes ->
        val hours = minutes / 60
        val mins = minutes % 60
        if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
    } ?: "N/A"

    val ratingText = movie.rating?.let {
        String.format("%.1f/10", it)
    } ?: "N/A"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            ) {
                AsyncImage(
                    model = movie.bannerUrl.ifEmpty { movie.thumbnailUrl },
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(15.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.7f),
                                    Color.Black
                                ),
                                startY = 0f,
                                endY = 1500f
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 80.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AsyncImage(
                        model = movie.thumbnailUrl,
                        contentDescription = movie.title,
                        modifier = Modifier
                            .width(200.dp)
                            .height(300.dp)
                            .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                if (movie.isFeatured) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 40.dp),
                        color = Color(0xFFE50914),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = "Featured",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "FEATURED",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (movie.releaseYear != null) {
                        Text(
                            text = movie.releaseYear.toString(),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text("•", color = Color.Gray, fontSize = 14.sp)
                    }

                    if (movie.rating != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = ratingText,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text("•", color = Color.Gray, fontSize = 14.sp)
                    }

                    Text(
                        text = duration,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                if (movie.category.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFF1A1A1A),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = movie.category,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onPlayClick(movie) }, // ✅ UPDATED
                        enabled = movie.videoUrl.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE50914)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (canPlay) "Play Now" else "No Video",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { /* TODO: Download */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download", fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SecondaryActionButton(
                        icon = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        label = if (isFavorite) "In My List" else "My List",
                        iconTint = if (isFavorite) Color(0xFFE50914) else Color.White,
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.weight(1f)
                    )

                    SecondaryActionButton(
                        icon = Icons.Default.Share,
                        label = "Share",
                        onClick = { showShareDialog = true },
                        modifier = Modifier.weight(1f)
                    )

                    SecondaryActionButton(
                        icon = Icons.Default.Info,
                        label = "Info",
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Overview",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = movie.description.ifEmpty { "No description available for this title." },
                    color = Color.Gray,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            title = { Text("Share Movie", color = Color.White) },
            text = { Text("Share '${movie.title}' with your friends!", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Share", color = Color(0xFFE50914))
                }
            },
            dismissButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Cancel", color = Color.White)
                }
            },
            containerColor = Color(0xFF1A1A1A)
        )
    }
}


@Composable
fun SecondaryActionButton(
    icon: ImageVector,
    label: String,
    iconTint: Color = Color.White,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // ✅ REQUIRED so Modifier.weight(...) works
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier, // ✅ apply incoming modifier
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFF1A1A1A),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}