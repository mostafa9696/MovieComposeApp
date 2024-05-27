package com.example.moviecomposeapp.models

import androidx.compose.runtime.Immutable

@Immutable
data class MovieDetail(
    val id: Int,
    val genres: String,
    val overview: String,
    val posterImageUrlPath: String,
    val backdropImageUrlPath: String,
    val movieName: String,
    val voteAverage: Float,
    val voteCount: Int,
    val releaseDate: String,
    val duration: Int,
    val originalMovieName: String
)