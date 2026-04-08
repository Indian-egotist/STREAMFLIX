package com.example.streamflix.app.data.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.streamflix.R
import com.example.streamflix.app.data.ui.components.*
import com.example.streamflix.app.data.ui.theme.NetflixBlack
import com.example.streamflix.model.Movie
import com.example.streamflix.viewmodel.HomeViewModel

@Composable
fun MovieSection(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Text(
                text = stringResource(R.string.see_all),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = onMovieClick
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (Movie) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // ViewModel
    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NetflixBlack)
    ) {
        when {
            uiState.isLoading -> {
                // Loading state
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFE50914)
                )
            }

            uiState.error != null -> {
                // Error state
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error loading content",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.error ?: "Unknown error",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.retry() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE50914)
                        )
                    ) {
                        Text("Retry")
                    }
                }
            }

            else -> {
                // Content loaded successfully
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Top App Bar (without sign out - that's in Account screen now)
                    item {
                        TopAppBar()
                    }

                    // Tab Row
                    item {
                        TabRow(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                    }

                    // Featured Movie Banner
                    uiState.featuredMovie?.let { movie ->
                        item {
                        val featuredPatched: Movie = movie.copy(
                            videoUrl = "https://www.youtube.com/watch?v=aU4f_KJiUu4",
                            thumbnailUrl = "https://img.youtube.com/vi/aU4f_KJiUu4/hqdefault.jpg",
                            bannerUrl = "https://img.freepik.com/free-photo/lavender-field-sunset-near-valensole_268835-3910.jpg?semt=ais_incoming&w=740&q=80",
                            title = if (movie.title.isBlank()) "Featured STREAMFLIX Video" else movie.title
                        )

                            FeaturedMovieBanner(
                                movie = movie,
                                onClick = onMovieClick
                            )
                        }
                    }

                    // Popular on Netflix Section
                    if (uiState.popularMovies.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            MovieSection(
                                title = stringResource(R.string.popular_on_netflix),
                                movies = uiState.popularMovies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }

                    // Trending Now Section
                    if (uiState.trendingMovies.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            MovieSection(
                                title = stringResource(R.string.trending_now),
                                movies = uiState.trendingMovies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }

                    // Action Section
                    if (uiState.actionMovies.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            MovieSection(
                                title = stringResource(R.string.action),
                                movies = uiState.actionMovies,
                                onMovieClick = onMovieClick
                            )
                        }
                    }

                    // Bottom Spacing for Navigation Bar
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}