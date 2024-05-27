package com.example.moviecomposeapp.models

import androidx.compose.runtime.Immutable

data class MovieTrailer(
    val trailers: List<Trailer>
)

@Immutable
data class Trailer(
    val name: String,
    val key: String
)