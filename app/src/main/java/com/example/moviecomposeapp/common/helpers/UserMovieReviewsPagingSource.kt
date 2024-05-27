package com.example.moviecomposeapp.common.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviecomposeapp.datasource.MovieApi
import com.example.moviecomposeapp.models.NetworkUserReviewResults

class UserMovieReviewsPagingSource(
    private val movieId: Int,
    private val api: MovieApi
): PagingSource<Int, NetworkUserReviewResults>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkUserReviewResults> {
        return try {
            val currentPageNumber = params.key ?: 1
            val response = api.getUserMovieReviews(movieId = movieId, page = currentPageNumber)
            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = if (currentPageNumber < response.totalPages) currentPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkUserReviewResults>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
