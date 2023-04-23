package com.pavel.tvshows.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pavel.tvshows.BuildConfig
import com.pavel.tvshows.data.remote.ShowsApi
import com.pavel.tvshows.data.remote.models.SearchMoviesResponse
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(
    private val showsApi: ShowsApi,
    private val query: String
) : PagingSource<Int, SearchMoviesResponse.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchMoviesResponse.Result> {
        return try {
            // get the current key
            val currentLoadingPageKey = params.key ?: 1
            //start the http call with page 1
            val response = showsApi.searchTvShows(
                apiKey = BuildConfig.MOVIES_ACCESS_KEY,
                language = "en",
                page = currentLoadingPageKey,
                query = query
            ).results
            //get the previous key and set it -1 from the current
            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            //every end of page add plus one to the current loading page key
            LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchMoviesResponse.Result>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}