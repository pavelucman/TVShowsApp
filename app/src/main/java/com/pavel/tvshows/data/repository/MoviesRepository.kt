package com.pavel.tvshows.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pavel.tvshows.data.local.dao.TvShowDatabase
import com.pavel.tvshows.data.local.models.FavoritesTable
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.data.paging.SearchPagingSource
import com.pavel.tvshows.data.paging.SimilarTvShowsPagingSource
import com.pavel.tvshows.data.paging.TvShowsRemoteMediator
import com.pavel.tvshows.data.remote.ShowsApi
import com.pavel.tvshows.data.remote.models.SearchMoviesResponse
import com.pavel.tvshows.data.remote.models.SimilarTvShowResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val showsApi: ShowsApi,
    private val tvShowDatabase: TvShowDatabase
) : DefaultRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getTopTvShows(): Flow<PagingData<TrendingTvShowsTable>> {
        val pagingSourceFactory = { tvShowDatabase.trendingTvShowsDao().getAllTvShowsPaged() }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            remoteMediator = TvShowsRemoteMediator(
                showsApi = showsApi,
                tvShowDatabase = tvShowDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun searchTvShows(tvShowName: String): Flow<PagingData<SearchMoviesResponse.Result>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchPagingSource(showsApi = showsApi, query = tvShowName)
            }
        ).flow
    }

    override fun getSimilarTvShows(tvShowId: Int): Flow<PagingData<SimilarTvShowResponse.Result>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SimilarTvShowsPagingSource(showsApi = showsApi, similarTvShowId = tvShowId)
            }
        ).flow
    }

    override fun getFavoriteShows(): Flow<List<FavoritesTable>> {
        return tvShowDatabase.favoriteShowsDao().getFavoriteTvShow()
    }

    override suspend fun insertFavoriteTvShow(tvShow: FavoritesTable) {
        tvShowDatabase.favoriteShowsDao().insertFavoriteTvShow(tvShow)
    }

    override suspend fun deleteFavoriteTvShow(tvShowId: Int) {
        tvShowDatabase.favoriteShowsDao().deleteFavoriteTvShow(tvShowId)
    }

    override fun checkIfTvShowIsFavorite(showId: Int): Boolean {
        return tvShowDatabase.favoriteShowsDao().checkIfTvShowExists(showId = showId)
    }
}