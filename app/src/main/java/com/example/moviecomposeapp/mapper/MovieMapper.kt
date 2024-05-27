package com.example.moviecomposeapp.mapper

import com.ahmetocak.network.model.movie_detail.NetworkMovieCredit
import com.ahmetocak.network.model.movie_detail.NetworkMovieDetail
import com.ahmetocak.network.model.movie_detail.NetworkMovieTrailer
import com.example.moviecomposeapp.models.ActorDetails
import com.example.moviecomposeapp.models.ActorMovies
import com.example.moviecomposeapp.models.ActorMoviesContent
import com.example.moviecomposeapp.models.Cast
import com.example.moviecomposeapp.models.Movie
import com.example.moviecomposeapp.models.MovieContent
import com.example.moviecomposeapp.models.MovieCredit
import com.example.moviecomposeapp.models.MovieDetail
import com.example.moviecomposeapp.models.MovieTrailer
import com.example.moviecomposeapp.models.NetworkActorDetails
import com.example.moviecomposeapp.models.NetworkActorMovies
import com.example.moviecomposeapp.models.NetworkActorMoviesContent
import com.example.moviecomposeapp.models.NetworkMovie
import com.example.moviecomposeapp.models.NetworkMovieContent
import com.example.moviecomposeapp.models.NetworkRecommendedMovieContent
import com.example.moviecomposeapp.models.NetworkUserReviewResults
import com.example.moviecomposeapp.models.RecommendedMovieContent
import com.example.moviecomposeapp.models.Trailer
import com.example.moviecomposeapp.models.UserReviewAuthorDetails
import com.example.moviecomposeapp.models.UserReviewResults

internal fun NetworkMovie.toMovie(): Movie {
    return Movie(
        movies = movies.map { it.toMovieContent() },
        totalPages = totalPages
    )
}

internal fun NetworkMovieContent.toMovieContent(): MovieContent {
    return MovieContent(
        id = id,
        posterImagePath = posterImagePath,
        releaseDate = releaseDate ?: "",
        movieName = movieName ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0
    )
}

internal fun NetworkMovieDetail.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        genres = genres.joinToString { it.name },
        overview = overview ?: "",
        posterImageUrlPath = posterImageUrlPath ?: "",
        backdropImageUrlPath = backdropImageUrlPath ?: "",
        movieName = movieName ?: "",
        voteAverage = voteAverage?.toFloat() ?: 0f,
        voteCount = voteCount ?: 0,
        releaseDate = releaseDate ?: "",
        duration = duration ?: 0,
        originalMovieName = originalMovieName ?: ""
    )
}

internal fun NetworkMovieCredit.toMovieCredit(): MovieCredit {
    return MovieCredit(
        cast = cast.filter {
            it.imageUrlPath != null
        }.map { castDto ->
            Cast(
                id = castDto.id,
                name = castDto.name ?: "",
                characterName = castDto.characterName ?: "",
                imageUrlPath = castDto.imageUrlPath ?: ""
            )
        },
        directorName = crew.firstOrNull { crewDto ->
            crewDto.job == "Director"
        }?.name ?: ""
    )
}

internal fun NetworkMovieTrailer.toMovieTrailer(): MovieTrailer {
    return MovieTrailer(
        trailers = trailers.map {
            Trailer(
                name = it.name,
                key = it.key
            )
        }
    )
}

internal fun NetworkActorDetails.toActorDetails(): ActorDetails {
    return ActorDetails(
        id = id,
        biography = biography ?: "",
        birthday = birthday ?: "",
        homepage = homepage,
        name = name ?: "",
        deathDay = deathDay,
        placeOfBirth = placeOfBirth ?: "",
        profileImagePath = profileImagePath ?: ""
    )
}

internal fun NetworkActorMovies.toActorMovies(): ActorMovies {
    return ActorMovies(movies = this.movies.map { it.toActorMoviesContent() })
}

private fun NetworkActorMoviesContent.toActorMoviesContent(): ActorMoviesContent {
    return ActorMoviesContent(
        id = id,
        movieName = movieName ?: "",
        posterImagePath = posterImagePath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0
    )
}

internal fun NetworkUserReviewResults.toUserReviewResults(): UserReviewResults {
    return UserReviewResults(
        id = id,
        author = author ?: "anonymous",
        content = content ?: "",
        createdAt = createdAt,
        updatedAt = updatedAt ?: "",
        authorDetails = UserReviewAuthorDetails(
            rating = authorDetails.rating,
            avatarPath = authorDetails.avatarPath
        )
    )
}

internal fun NetworkRecommendedMovieContent.toRecommendedMovieContent(): RecommendedMovieContent {
    return RecommendedMovieContent(
        id = id,
        movieName = movieName ?: "",
        image = image ?: "",
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage ?: 0.0,
        voteCount = voteCount ?: 0
    )
}