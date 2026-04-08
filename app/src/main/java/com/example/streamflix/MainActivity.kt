package com.example.streamflix

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.streamflix.app.data.ui.components.BottomNavigationBar
import com.example.streamflix.app.data.ui.screens.AccountScreen
import com.example.streamflix.app.data.ui.screens.FavoritesScreen
import com.example.streamflix.app.data.ui.screens.HomeScreen
import com.example.streamflix.app.data.ui.screens.MovieDetailScreen
import com.example.streamflix.app.data.ui.screens.SearchScreen
import com.example.streamflix.app.data.ui.screens.VideoPlayerScreen
import com.example.streamflix.app.data.ui.screens.YouTubePlayerScreen
import com.example.streamflix.app.data.ui.theme.StreamFlixTheme
import com.example.streamflix.model.Movie
import com.example.streamflix.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if user is authenticated
        checkAuthenticationStatus()

        setContent {
            StreamFlixTheme {
                MainScreen()
            }
        }
    }

    private fun checkAuthenticationStatus() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val currentUser = SupabaseClient.auth.currentUserOrNull()

                if (currentUser == null) {
                    navigateToLogin()
                }
            } catch (e: Exception) {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        checkAuthenticationStatus()
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Account : Screen("account")
    object MovieDetail : Screen("movie_detail/{movieId}?videoUrl={videoUrl}") {
        fun createRoute(movieId: String, videoUrl: String) =
            "movie_detail/$movieId?videoUrl=${Uri.encode(videoUrl)}"
    }
    object VideoPlayer : Screen("video_player/{movieId}") {
        fun createRoute(movieId: String) = "video_player/$movieId"
    }
    object YouTubePlayer : Screen("youtube_player/{movieId}?videoUrl={videoUrl}&title={title}") {
        fun createRoute(movieId: String, videoUrl: String, title: String) =
            "youtube_player/$movieId?videoUrl=${Uri.encode(videoUrl)}&title=${Uri.encode(title)}"
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Determine selected bottom nav item
    val selectedItem = when {
        currentRoute?.startsWith("home") == true -> 0
        currentRoute?.startsWith("search") == true -> 1
        currentRoute?.startsWith("favorites") == true -> 2
        currentRoute?.startsWith("account") == true -> 3
        else -> 0
    }

    // Show bottom bar only on main screens
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Favorites.route,
        Screen.Account.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = selectedItem,
                    onItemSelected = { index ->
                        val route = when (index) {
                            0 -> Screen.Home.route
                            1 -> Screen.Search.route
                            2 -> Screen.Favorites.route
                            3 -> Screen.Account.route
                            else -> Screen.Home.route
                        }
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Home Screen
            composable(Screen.Home.route) {
                HomeScreen(
                    onMovieClick = { movie ->
                        navController.navigate(Screen.MovieDetail.createRoute(movie.id,movie.videoUrl))
                    }
                )
            }

            // Search Screen
            composable(Screen.Search.route) {
                SearchScreen(
                    onMovieClick = { movie ->
                        navController.navigate(Screen.MovieDetail.createRoute(movie.id,movie.videoUrl))
                    }
                )
            }

            // Favorites Screen
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onMovieClick = { movie ->
                        navController.navigate(Screen.MovieDetail.createRoute(movie.id,movie.videoUrl))
                    }
                )
            }

            // Account Screen
            composable(Screen.Account.route) {
                AccountScreen()
            }

            // Movie Detail Screen
            composable(
                route = Screen.MovieDetail.route,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.StringType },
                    navArgument("videoUrl") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""

                val sampleMovie = Movie(
                    id = movieId,
                    title = "Sample Movie",
                    description = "Replace with Supabase",
                    thumbnailUrl = "https://img.youtube.com/vi/aU4f_KJiUu4/hqdefault.jpg",
                    bannerUrl = "https://img.youtube.com/vi/aU4f_KJiUu4/maxresdefault.jpg",
                    videoUrl = videoUrl, // ✅ USE THE PASSED URL
                    releaseYear = 2024,
                    durationMinutes = 150,
                    rating = 8.5f,
                    category = "Action",
                    isFeatured = true
                )

                MovieDetailScreen(
                    movie = sampleMovie,
                    onPlayClick = { m ->
                        val isYouTube = m.videoUrl.contains("youtube.com", ignoreCase = true) ||
                                m.videoUrl.contains("youtu.be", ignoreCase = true)

                        if (isYouTube) {
                            navController.navigate(
                                Screen.YouTubePlayer.createRoute(
                                    movieId = m.id,
                                    videoUrl = m.videoUrl,
                                    title = m.title
                                )
                            )
                        } else {
                            navController.navigate(Screen.VideoPlayer.createRoute(m.id))
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Video Player Screen
            composable(
                route = Screen.VideoPlayer.route,
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""

                VideoPlayerScreen(
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    title = "Sample Movie",
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            //YouTube Video player Screen
            composable(
                route = Screen.YouTubePlayer.route,
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""

                // TODO: Replace this with real fetch by movieId (Supabase / ViewModel)
                // For now reuse the same sampleMovie pattern you used elsewhere:
                val movie = Movie(
                    id = movieId,
                    title = "JB's video",
                    description = "Sample description",
                    thumbnailUrl = "https://img.youtube.com/vi/aU4f_KJiUu4/hqdefault.jpg",
                    bannerUrl = "https://media.licdn.com/dms/image/v2/D5622AQFrzeFbcsQlRw/feedshare-shrink_800/B56ZbzKV3SHUAk-/0/1747836297874?e=2147483647&v=beta&t=FROkfrsRAK65WD6xcUHEgihqsWF-Kzs9Ho4rdtDPo7I",
                    videoUrl = "https://www.youtube.com/watch?v=aU4f_KJiUu4"
                )

                YouTubePlayerScreen(
                    youtubeUrlOrId = movie.videoUrl,
                    title = movie.title,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}