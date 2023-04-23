package com.pavel.tvshows.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pavel.tvshows.BuildConfig
import com.pavel.tvshows.data.remote.ShowsApi
import com.pavel.tvshows.data.remote.models.SimilarTvShowResponse
import retrofit2.HttpException
import java.io.IOException

class SimilarTvShowsPagingSource(
    private val showsApi: ShowsApi,
    private val similarTvShowId: Int
) : PagingSource<Int, SimilarTvShowResponse.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SimilarTvShowResponse.Result> {
        return try {
            // get the current key
            val currentLoadingPageKey = params.key ?: 1

            //start the http call with page 1
            val response = showsApi.similarTvShows(
                apiKey = BuildConfig.MOVIES_ACCESS_KEY,
                language = "en",
                page = currentLoadingPageKey,
                tvShow = similarTvShowId
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

    override fun getRefreshKey(state: PagingState<Int, SimilarTvShowResponse.Result>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}