package com.example.streamflix.app.data

import com.example.streamflix.model.Movie

object DummyData {
    val movies = listOf(
        Movie(
            id = "1",
            title = "Stranger Things",
            description = "When a young boy vanishes, a small town uncovers a mystery involving secret experiments.",
            thumbnailUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=800",
            releaseYear = 2023,
            durationMinutes = 145,
            rating = 8.7f,
            category = "featured",
            isFeatured = true
        ),
        Movie(
            id = "2",
            title = "The Dark Knight",
            description = "Batman must accept one of the greatest psychological tests.",
            thumbnailUrl = "https://images.unsplash.com/photo-1509347528160-9a9e33742cdb?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1509347528160-9a9e33742cdb?w=800",
            releaseYear = 2023,
            durationMinutes = 152,
            rating = 9.0f,
            category = "popular",
            isFeatured = false
        ),
        Movie(
            id = "3",
            title = "Inception",
            description = "A thief who steals corporate secrets through dream-sharing technology.",
            thumbnailUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=800",
            releaseYear = 2023,
            durationMinutes = 148,
            rating = 8.8f,
            category = "popular",
            isFeatured = false
        ),
        Movie(
            id = "4",
            title = "The Matrix",
            description = "A computer hacker learns about the true nature of reality.",
            thumbnailUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=800",
            releaseYear = 2023,
            durationMinutes = 136,
            rating = 8.7f,
            category = "popular",
            isFeatured = false
        ),
        Movie(
            id = "5",
            title = "Interstellar",
            description = "A team of explorers travel through a wormhole in space.",
            thumbnailUrl = "https://images.unsplash.com/photo-1419242902214-272b3f66ee7a?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1419242902214-272b3f66ee7a?w=800",
            releaseYear = 2023,
            durationMinutes = 169,
            rating = 8.6f,
            category = "popular",
            isFeatured = false
        ),
        Movie(
            id = "6",
            title = "Pulp Fiction",
            description = "The lives of two mob hitmen intertwine in four tales.",
            thumbnailUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800",
            releaseYear = 2023,
            durationMinutes = 154,
            rating = 8.9f,
            category = "popular",
            isFeatured = false
        ),
        Movie(
            id = "7",
            title = "Avatar",
            description = "A paraplegic Marine dispatched to Pandora.",
            thumbnailUrl = "https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=800",
            releaseYear = 2024,
            durationMinutes = 162,
            rating = 7.8f,
            category = "trending",
            isFeatured = false
        ),
        Movie(
            id = "8",
            title = "Money Heist",
            description = "Eight thieves take hostages in the Royal Mint of Spain.",
            thumbnailUrl = "https://images.unsplash.com/photo-1574267432644-f76c9a86c0f6?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1574267432644-f76c9a86c0f6?w=800",
            releaseYear = 2024,
            durationMinutes = 138,
            rating = 8.2f,
            category = "trending",
            isFeatured = false
        ),
        Movie(
            id = "9",
            title = "Breaking Bad",
            description = "A chemistry teacher becomes a methamphetamine producer.",
            thumbnailUrl = "https://images.unsplash.com/photo-1594908900066-3f47337549d8?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1594908900066-3f47337549d8?w=800",
            releaseYear = 2024,
            durationMinutes = 142,
            rating = 9.5f,
            category = "trending",
            isFeatured = false
        ),
        Movie(
            id = "10",
            title = "The Crown",
            description = "Follows the political rivalries and romance of Queen Elizabeth II.",
            thumbnailUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800",
            releaseYear = 2024,
            durationMinutes = 135,
            rating = 8.6f,
            category = "trending",
            isFeatured = false
        ),
        Movie(
            id = "11",
            title = "Squid Game",
            description = "Contestants compete in deadly children's games.",
            thumbnailUrl = "https://images.unsplash.com/photo-1616530940355-351fabd9524b?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1616530940355-351fabd9524b?w=800",
            releaseYear = 2024,
            durationMinutes = 129,
            rating = 8.0f,
            category = "trending",
            isFeatured = false
        ),
        Movie(
            id = "12",
            title = "John Wick",
            description = "An ex-hit-man comes out of retirement.",
            thumbnailUrl = "https://images.unsplash.com/photo-1534809027769-b00d750a6bac?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1534809027769-b00d750a6bac?w=800",
            releaseYear = 2024,
            durationMinutes = 125,
            rating = 7.4f,
            category = "action",
            isFeatured = false
        ),
        Movie(
            id = "13",
            title = "Mad Max",
            description = "In a post-apocalyptic wasteland, a woman rebels.",
            thumbnailUrl = "https://images.unsplash.com/photo-1571847140471-1d7766e825ea?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1571847140471-1d7766e825ea?w=800",
            releaseYear = 2024,
            durationMinutes = 120,
            rating = 8.1f,
            category = "action",
            isFeatured = false
        ),
        Movie(
            id = "14",
            title = "Mission Impossible",
            description = "IMF team races against time after a mission gone wrong.",
            thumbnailUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=800",
            releaseYear = 2024,
            durationMinutes = 147,
            rating = 7.7f,
            category = "action",
            isFeatured = false
        ),
        Movie(
            id = "15",
            title = "Fast & Furious",
            description = "A teenager becomes a major competitor in drift racing.",
            thumbnailUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=400",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            bannerUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800",
            releaseYear = 2024,
            durationMinutes = 118,
            rating = 6.9f,
            category = "action",
            isFeatured = false
        )
    )

    // Categorized lists for easy access
    val featuredMovie = movies.firstOrNull { it.isFeatured }
    val popularMovies = movies.filter { it.category == "popular" }
    val trendingMovies = movies.filter { it.category == "trending" }
    val actionMovies = movies.filter { it.category == "action" }
}