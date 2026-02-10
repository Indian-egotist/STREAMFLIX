package com.example.streamflix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamflix.model.Movie
import com.example.streamflix.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val featuredMovie: Movie? = null,
    val popularMovies: List<Movie> = emptyList(),
    val trendingMovies: List<Movie> = emptyList(),
    val actionMovies: List<Movie> = emptyList(),
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Fetch featured movie
                val featuredResult = repository.getFeaturedMovie()
                val featured = featuredResult.getOrNull()

                // Fetch all movies and categorize
                val allMoviesResult = repository.getAllMovies()
                val allMovies = allMoviesResult.getOrNull() ?: emptyList()

                // Categorize movies
                val popular = allMovies.filter { it.category == "popular" }
                val trending = allMovies.filter { it.category == "trending" }
                val action = allMovies.filter { it.category == "action" }

                _uiState.value = HomeUiState(
                    isLoading = false,
                    featuredMovie = featured,
                    popularMovies = popular.ifEmpty { allMovies.take(5) },
                    trendingMovies = trending.ifEmpty { allMovies.drop(5).take(5) },
                    actionMovies = action.ifEmpty { allMovies.drop(10).take(5) },
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun retry() {
        loadMovies()
    }
}