package com.example.streamflix.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    @SerialName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerialName("video_url")
    val videoUrl: String = "",
    @SerialName("banner_url")
    val bannerUrl: String = "",
    @SerialName("release_year")
    val releaseYear: Int? = null,
    @SerialName("duration_minutes")
    val durationMinutes: Int? = null,
    val rating: Float? = null,
    val category: String = "",
    @SerialName("is_featured")
    val isFeatured: Boolean = false
)

@Serializable
data class Series(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    @SerialName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerialName("banner_url")
    val bannerUrl: String = "",
    @SerialName("total_seasons")
    val totalSeasons: Int = 0,
    @SerialName("release_year")
    val releaseYear: Int? = null,
    val rating: Float? = null,
    @SerialName("is_featured")
    val isFeatured: Boolean = false
)

@Serializable
data class Episode(
    val id: String = "",
    @SerialName("series_id")
    val seriesId: String = "",
    @SerialName("season_number")
    val seasonNumber: Int = 0,
    @SerialName("episode_number")
    val episodeNumber: Int = 0,
    val title: String = "",
    val description: String = "",
    @SerialName("video_url")
    val videoUrl: String = "",
    @SerialName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerialName("duration_minutes")
    val durationMinutes: Int? = null
)

@Serializable
data class Category(
    val id: String = "",
    val name: String = "",
    @SerialName("display_order")
    val displayOrder: Int = 0
)