package com.example.moviecomposeapp.models

import com.google.gson.annotations.SerializedName

data class NetworkMovie(
    @SerializedName("results")
    val movies: List<NetworkMovieContent>,

    @SerializedName("total_pages")
    val totalPages: Int
)

data class NetworkMovieContent(
    val id: Int,

    @SerializedName("poster_path")
    val posterImagePath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("original_title")
    val movieName: String?,

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("vote_count")
    val voteCount: Int?
)