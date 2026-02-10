package com.example.streamflix.repository

import android.util.Log
import com.example.streamflix.model.Movie
import com.example.streamflix.model.Series
import com.example.streamflix.model.Episode
import com.example.streamflix.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class MovieRepository {

    private val supabase = SupabaseClient.client

    // Fetch all movies
    suspend fun getAllMovies(): Result<List<Movie>> {
        return try {
            val movies = supabase
                .from("movies")
                .select()
                .decodeList<Movie>()

            Log.d("MovieRepository", "Fetched ${movies.size} movies")
            Result.success(movies)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error fetching movies", e)
            Result.failure(e)
        }
    }

    // Fetch featured movie (for banner)
    suspend fun getFeaturedMovie(): Result<Movie?> {
        return try {
            val movies = supabase
                .from("movies")
                .select {
                    filter {
                        eq("is_featured", true)
                    }
                    limit(1)
                }
                .decodeList<Movie>()

            Result.success(movies.firstOrNull())
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error fetching featured movie", e)
            Result.failure(e)
        }
    }

    // Fetch movies by category
    suspend fun getMoviesByCategory(category: String): Result<List<Movie>> {
        return try {
            val movies = supabase
                .from("movies")
                .select {
                    filter {
                        eq("category", category)
                    }
                }
                .decodeList<Movie>()

            Log.d("MovieRepository", "Fetched ${movies.size} movies for category: $category")
            Result.success(movies)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error fetching movies by category", e)
            Result.failure(e)
        }
    }

    // Fetch all series
    suspend fun getAllSeries(): Result<List<Series>> {
        return try {
            val series = supabase
                .from("series")
                .select()
                .decodeList<Series>()

            Log.d("MovieRepository", "Fetched ${series.size} series")
            Result.success(series)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error fetching series", e)
            Result.failure(e)
        }
    }

    // Fetch episodes for a series
    suspend fun getEpisodesForSeries(seriesId: String): Result<List<Episode>> {
        return try {
            val episodes = supabase
                .from("episodes")
                .select {
                    filter {
                        eq("series_id", seriesId)
                    }
                }
                .decodeList<Episode>()

            Log.d("MovieRepository", "Fetched ${episodes.size} episodes for series: $seriesId")
            Result.success(episodes)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error fetching episodes", e)
            Result.failure(e)
        }
    }

    // Search movies
    suspend fun searchMovies(query: String): Result<List<Movie>> {
        return try {
            val movies = supabase
                .from("movies")
                .select {
                    filter {
                        ilike("title", "%$query%")
                    }
                }
                .decodeList<Movie>()

            Log.d("MovieRepository", "Search found ${movies.size} movies")
            Result.success(movies)
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error searching movies", e)
            Result.failure(e)
        }
    }
}