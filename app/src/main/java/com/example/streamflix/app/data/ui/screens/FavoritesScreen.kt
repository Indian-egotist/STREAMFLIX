@file:Suppress("DEPRECATION")

package com.example.streamflix.app.data.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.streamflix.model.Movie

@Composable
fun FavoritesScreen(
    onMovieClick: (Movie) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("My List", "Downloads")

    // Sample data - TODO: Replace with actual Supabase data
    val favorites = remember {
        mutableStateListOf<Movie>()
    }

    val downloads = remember {
        mutableStateListOf<Movie>()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Header
        Text(
            text = "My StreamFlix",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Black,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFFE50914)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            title,
                            fontSize = 16.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> FavoritesList(
                favorites = favorites,
                onMovieClick = onMovieClick,
                onRemove = { movie ->
                    favorites.remove(movie)
                }
            )
            1 -> DownloadsList(
                downloads = downloads,
                onMovieClick = onMovieClick,
                onDelete = { movie ->
                    downloads.remove(movie)
                }
            )
        }
    }
}

@Composable
fun FavoritesList(
    favorites: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onRemove: (Movie) -> Unit
) {
    if (favorites.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Favorite,
            title = "No Favorites Yet",
            message = "Movies and series you add to your list will appear here.\nStart exploring and add your favorites!"
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favorites) { movie ->
                FavoriteMovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie) },
                    onRemove = { onRemove(movie) }
                )
            }
        }
    }
}

@Composable
fun DownloadsList(
    downloads: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onDelete: (Movie) -> Unit
) {
    if (downloads.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Download,
            title = "No Downloads",
            message = "Downloaded movies and series will appear here.\nDownload content to watch offline!"
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(downloads) { movie ->
                DownloadMovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie) },
                    onDelete = { onDelete(movie) }
                )
            }
        }
    }
}

@Composable
fun FavoriteMovieCard(
    movie: Movie,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            // Movie Thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            ) {
                AsyncImage(
                    model = movie.thumbnailUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                // Menu button
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Dropdown menu
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color(0xFF1A1A1A))
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Remove from List", color = Color.White)
                            }
                        },
                        onClick = {
                            showMenu = false
                            onRemove()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Movie Title
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun DownloadMovieCard(
    movie: Movie,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            // Movie Thumbnail with download indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            ) {
                AsyncImage(
                    model = movie.thumbnailUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                // Download badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    color = Color(0xFFE50914),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Downloaded",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Downloaded",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Menu button
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Dropdown menu
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color(0xFF1A1A1A))
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFE50914),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Delete Download", color = Color.White)
                            }
                        },
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Movie Title
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                lineHeight = 18.sp
            )

            // File size (mock data)
            Text(
                text = "850 MB",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Delete Download?",
                    color = Color.White
                )
            },
            text = {
                Text(
                    "This will delete '${movie.title}' from your device. You can download it again later.",
                    color = Color.Gray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Delete", color = Color(0xFFE50914))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color.White)
                }
            },
            containerColor = Color(0xFF1A1A1A)
        )
    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // Icon
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Message
            Text(
                text = message,
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}