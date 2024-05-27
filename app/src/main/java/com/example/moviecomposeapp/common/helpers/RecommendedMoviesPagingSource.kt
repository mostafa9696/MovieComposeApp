package com.example.moviecomposeapp.common.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviecomposeapp.datasource.MovieApi
import com.example.moviecomposeapp.models.NetworkRecommendedMovieContent

class RecommendedMoviesPagingSource(
    private val movieId: Int,
    private val api: MovieApi
): PagingSource<Int, NetworkRecommendedMovieContent>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkRecommendedMovieContent> {
        return try {
            val currentPageNumber = params.key ?: 1
            val response = api.getMovieRecommendations(movieId = movieId, page = currentPageNumber)
            LoadResult.Page(
                data = response.recommendations.filter { it.image != null },
                prevKey = null,
                nextKey = if (currentPageNumber < response.totalPages) currentPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkRecommendedMovieContent>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}