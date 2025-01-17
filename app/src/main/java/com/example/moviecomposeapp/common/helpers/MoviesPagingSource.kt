package com.example.moviecomposeapp.common.helpers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviecomposeapp.models.NetworkMovie
import com.example.moviecomposeapp.models.NetworkMovieContent

class MoviesPagingSource(
    private val apiCall: suspend (Int) -> NetworkMovie
) : PagingSource<Int, NetworkMovieContent>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkMovieContent> {
        return try {
            val currentPageNumber = params.key ?: 1
            val response = apiCall(currentPageNumber)
            LoadResult.Page(
                data = response.movies,
                prevKey = null,
                nextKey = if (currentPageNumber < response.totalPages) currentPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkMovieContent>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}