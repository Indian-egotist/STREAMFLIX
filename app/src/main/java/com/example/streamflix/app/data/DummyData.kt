package com.example.streamflix.app.data

import com.example.streamflix.model.Movie

object DummyData {

    // Sample video URLs (using free sample videos)
    private const val SAMPLE_VIDEO_1 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    private const val SAMPLE_VIDEO_2 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    private const val SAMPLE_VIDEO_3 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
    private const val SAMPLE_VIDEO_4 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"

    val featuredMovie = Movie(
        id = 0,
        title = "Shangri-La Frontier",
        "https://static1.srcdn.com/wordpress/wp-content/uploads/2024/09/shangri-la-frontier.jpg",
        videoUrl = SAMPLE_VIDEO_1,
        category = "Featured",
        isFeatured = true
    )
//https://upload.wikimedia.org/wikipedia/en/7/79/Shangri-La_Frontier_anime_key_visual.png
    val popularMovies = listOf(
        Movie(
            id = 1,
            title = "Money Heist",
            imageUrl = "https://www.baltana.com/files/wallpapers-21/TV-Series-Money-Heist-High-Definition-Wallpaper-53054.png",
            videoUrl = SAMPLE_VIDEO_1,
            category = "Popular"
        ),
        Movie(
            id = 2,
            title = "Stranger Things",
            imageUrl = "https://deadline.com/wp-content/uploads/2025/11/Stranger-Things-5_33a02d.jpg?w=681&h=383&crop=1",
            videoUrl = SAMPLE_VIDEO_2,
            category = "Popular"
        ),
        Movie(
            id = 3,
            title = "The Walking Dead",
            imageUrl = "https://picsum.photos/seed/walkingdead/300/400",
            videoUrl = SAMPLE_VIDEO_3,
            category = "Popular"
        ),
        Movie(
            id = 4,
            title = "Breaking Bad",
            imageUrl = "https://picsum.photos/seed/breakingbad/300/400",
            videoUrl = SAMPLE_VIDEO_4,
            category = "Popular"
        )
    )

    val trendingMovies = listOf(
        Movie(
            id = 5,
            title = "Squid Game",
            imageUrl = "https://picsum.photos/seed/squid/300/400",
            videoUrl = SAMPLE_VIDEO_1,
            category = "Trending"
        ),
        Movie(
            id = 6,
            title = "House of Secrets",
            imageUrl = "https://picsum.photos/seed/housesecrets/300/402",
            videoUrl = SAMPLE_VIDEO_2,
            category = "Trending"
        ),
        Movie(
            id = 7,
            title = "Alive",
            imageUrl = "https://picsum.photos/seed/alive/300/400",
            videoUrl = SAMPLE_VIDEO_3,
            category = "Trending"
        ),
        Movie(
            id = 8,
            title = "The Witcher",
            imageUrl = "https://picsum.photos/seed/witcher/300/400",
            videoUrl = SAMPLE_VIDEO_4,
            category = "Trending"
        )
    )

    val actionMovies = listOf(
        Movie(
            id = 9,
            title = "Red Notice",
            imageUrl = "https://picsum.photos/seed/rednotice/300/400",
            videoUrl = SAMPLE_VIDEO_1,
            category = "Action"
        ),
        Movie(
            id = 10,
            title = "Extraction",
            imageUrl = "https://picsum.photos/seed/extraction/300/400",
            videoUrl = SAMPLE_VIDEO_2,
            category = "Action"
        ),
        Movie(
            id = 11,
            title = "The Gray Man",
            imageUrl = "https://picsum.photos/seed/grayman/300/400",
            videoUrl = SAMPLE_VIDEO_3,
            category = "Action"
        ),
        Movie(
            id = 12,
            title = "Army of the Dead",
            imageUrl = "https://picsum.photos/seed/armydead/300/400",
            videoUrl = SAMPLE_VIDEO_4,
            category = "Action"
        )
    )
}