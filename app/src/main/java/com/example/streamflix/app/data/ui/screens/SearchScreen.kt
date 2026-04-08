package com.example.streamflix.app.data.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.streamflix.model.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onMovieClick: (Movie) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    // Recent searches (stored locally - TODO: Save to SharedPreferences)
    var recentSearches by remember {
        mutableStateOf(
            listOf(
                "Stranger Things",
                "Action Movies",
                "Thriller",
                "Christopher Nolan"
            )
        )
    }

    // Popular searches (could fetch from backend)
    val popularSearches = remember {
        listOf(
            "Trending Now",
            "Top Rated",
            "New Releases",
            "Award Winners",
            "Sci-Fi Movies",
            "Comedy Series",
            "Horror Films",
            "Documentaries",
            "Anime",
            "Marvel Movies"
        )
    }

    // Search categories
    val searchCategories = remember {
        listOf(
            SearchCategory("Action", "🎬"),
            SearchCategory("Comedy", "😂"),
            SearchCategory("Drama", "🎭"),
            SearchCategory("Horror", "👻"),
            SearchCategory("Romance", "❤️"),
            SearchCategory("Sci-Fi", "🚀"),
            SearchCategory("Thriller", "🔪"),
            SearchCategory("Animation", "🎨")
        )
    }

    // Auto-focus search field when screen opens
    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    // Simulate search with delay
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            isLoading = true
            delay(500) // Simulate network delay

            // TODO: Replace with actual Supabase query
            // searchResults = searchMoviesInSupabase(searchQuery)

            // Mock search results for now
            searchResults = emptyList()
            isLoading = false
        } else {
            searchResults = emptyList()
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Search Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color(0xFF1A1A1A),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        isSearching = it.isNotEmpty()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    placeholder = {
                        Text(
                            "Search movies, series, actors...",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFFE50914)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            // Add to recent searches
                            if (searchQuery.isNotBlank() && !recentSearches.contains(searchQuery)) {
                                recentSearches = listOf(searchQuery) + recentSearches.take(9)
                            }
                        }
                    )
                )

                // Clear button
                AnimatedVisibility(
                    visible = searchQuery.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        onClick = {
                            searchQuery = ""
                            searchResults = emptyList()
                            isSearching = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Content Area
        if (isSearching) {
            // Show search results or loading
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFE50914),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Searching...",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else if (searchResults.isEmpty()) {
                // No results found
                NoResultsFound(searchQuery = searchQuery)
            } else {
                // Show search results
                SearchResults(
                    results = searchResults,
                    onMovieClick = onMovieClick
                )
            }
        } else {
            // Show discover content (recent searches, popular, categories)
            DiscoverContent(
                recentSearches = recentSearches,
                popularSearches = popularSearches,
                categories = searchCategories,
                onSearchClick = { query ->
                    searchQuery = query
                    isSearching = true
                },
                onClearRecent = { search ->
                    recentSearches = recentSearches.filter { it != search }
                }
            )
        }
    }
}

@Composable
fun SearchResults(
    results: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "${results.size} Results",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(results) { movie ->
            SearchResultItem(
                movie = movie,
                onClick = { onMovieClick(movie) }
            )
        }
    }
}

@Composable
fun SearchResultItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        // Movie Thumbnail
        AsyncImage(
            model = movie.thumbnailUrl,
            contentDescription = movie.title,
            modifier = Modifier
                .width(100.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Movie Info
        Column(
            modifier = Modifier
                .weight(1f)
                .height(140.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.description,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            // Play button
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE50914)
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Play", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun NoResultsFound(searchQuery: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "🔍",
                fontSize = 64.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Results Found",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We couldn't find anything for \"$searchQuery\"",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Try searching for:",
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "• Movie or series titles\n• Actors or directors\n• Genres or categories",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun DiscoverContent(
    recentSearches: List<String>,
    popularSearches: List<String>,
    categories: List<SearchCategory>,
    onSearchClick: (String) -> Unit,
    onClearRecent: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Recent Searches
        if (recentSearches.isNotEmpty()) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Searches",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    recentSearches.forEach { search ->
                        RecentSearchItem(
                            search = search,
                            onClick = { onSearchClick(search) },
                            onClear = { onClearRecent(search) }
                        )
                    }
                }
            }
        }

        // Browse by Category
        item {
            Column {
                Text(
                    text = "Browse by Category",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.chunked(2).forEach { rowCategories ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowCategories.forEach { category ->
                                CategoryCard(
                                    category = category,
                                    onClick = { onSearchClick(category.name) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Fill empty space if odd number
                            if (rowCategories.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // Popular Searches
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Trending",
                        tint = Color(0xFFE50914),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Popular Searches",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                popularSearches.forEach { search ->
                    PopularSearchItem(
                        search = search,
                        onClick = { onSearchClick(search) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecentSearchItem(
    search: String,
    onClick: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = "Recent",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = search,
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onClear,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color.Gray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun PopularSearchItem(
    search: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = search,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}

@Composable
fun CategoryCard(
    category: SearchCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        color = Color(0xFF1A1A1A),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = category.emoji,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Data class for search categories
data class SearchCategory(
    val name: String,
    val emoji: String
)