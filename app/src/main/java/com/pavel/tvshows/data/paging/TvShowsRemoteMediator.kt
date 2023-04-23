package com.pavel.tvshows.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.pavel.tvshows.BuildConfig
import com.pavel.tvshows.Constants.NO_INFO
import com.pavel.tvshows.convertToTrendingShowFromResponse
import com.pavel.tvshows.data.local.dao.TvShowDatabase
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.data.local.models.TvShowsRemoteKeysTable
import com.pavel.tvshows.data.remote.ShowsApi

@ExperimentalPagingApi
class TvShowsRemoteMediator(
    private val showsApi: ShowsApi,
    private val tvShowDatabase: TvShowDatabase,
) : RemoteMediator<Int, TrendingTvShowsTable>() {

    private val trendingMoviesDao = tvShowDatabase.trendingTvShowsDao()
    private val moviesRemoteKeysDao = tvShowDatabase.tvShowsRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TrendingTvShowsTable>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = showsApi.getTrendingTvShows(
                apiKey = BuildConfig.MOVIES_ACCESS_KEY,
                language = "en",
                page = currentPage
            )
            val endOfPaginationReached = currentPage == response.totalPages

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            tvShowDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    trendingMoviesDao.deleteAllTvShows()
                    moviesRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.results.map { movie ->
                    TvShowsRemoteKeysTable(
                        id = movie?.originalTitle ?: NO_INFO,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                moviesRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                response.results.let { movies ->
                    trendingMoviesDao.addTvShow(movies = movies.map { trendingTvShows ->
                        convertToTrendingShowFromResponse(trendingTvShows, response.page)

                    }
                    )
                }
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, TrendingTvShowsTable>
    ): TvShowsRemoteKeysTable? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.originalTitle?.let { id ->
                moviesRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, TrendingTvShowsTable>
    ): TvShowsRemoteKeysTable? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                moviesRemoteKeysDao.getRemoteKeys(id = movie.originalTitle)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, TrendingTvShowsTable>
    ): TvShowsRemoteKeysTable? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                moviesRemoteKeysDao.getRemoteKeys(id = movie.originalTitle)
            }
    }
}