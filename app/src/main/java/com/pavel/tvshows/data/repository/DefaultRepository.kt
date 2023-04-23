package com.pavel.tvshows.data.repository

import androidx.paging.PagingData
import com.pavel.tvshows.data.local.models.FavoritesTable
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.data.remote.models.SearchMoviesResponse
import com.pavel.tvshows.data.remote.models.SimilarTvShowResponse
import kotlinx.coroutines.flow.Flow

interface DefaultRepository {

    fun getTopTvShows(): Flow<PagingData<TrendingTvShowsTable>>

    fun searchTvShows(tvShowName: String): Flow<PagingData<SearchMoviesResponse.Result>>

    fun getSimilarTvShows(tvShowId: Int): Flow<PagingData<SimilarTvShowResponse.Result>>

    fun getFavoriteShows(): Flow<List<FavoritesTable>>

    suspend fun insertFavoriteTvShow(tvShow: FavoritesTable)

    suspend fun deleteFavoriteTvShow(tvShowId: Int)

    fun checkIfTvShowIsFavorite(showId:Int):Boolean

}