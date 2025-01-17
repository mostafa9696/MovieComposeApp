package com.example.moviecomposeapp.models

import androidx.compose.runtime.Immutable

data class MovieCredit(
    val cast: List<Cast>,
    val directorName: String
)

@Immutable
data class Cast(
    val id: Int,
    val name: String,
    val characterName: String,
    val imageUrlPath: String
)