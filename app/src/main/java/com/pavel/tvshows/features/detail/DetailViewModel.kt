package com.pavel.tvshows.features.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.pavel.tvshows.convertToFavoriteTable
import com.pavel.tvshows.convertToTrendingTvShowTable
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.data.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DefaultRepository
) : ViewModel() {

    private val _similarTvShows: MutableStateFlow<PagingData<TrendingTvShowsTable>> =
        MutableStateFlow(PagingData.empty())
    val similarTvShows: StateFlow<PagingData<TrendingTvShowsTable>> = _similarTvShows

    fun getSimilarTvShows(tvShowId: Int) {
        viewModelScope.launch {
            repository.getSimilarTvShows(tvShowId = tvShowId).cachedIn(viewModelScope)
                .collect { movies ->
                    _similarTvShows.value = movies.map { tvShowSimilar ->
                        convertToTrendingTvShowTable(tvShowSimilar)
                    }
                }
        }
    }

    private fun insertFavoriteTvShow(tvShow: TrendingTvShowsTable) = viewModelScope.launch {
        repository.insertFavoriteTvShow(convertToFavoriteTable(tvShow))
    }

    private fun deleteFavoriteTvShow(tvShowId: Int) =
        viewModelScope.launch { repository.deleteFavoriteTvShow(tvShowId = tvShowId) }

    fun updateTvShowFavorite(movie: TrendingTvShowsTable) {
        if (showExist(movie.id)) {
            deleteFavoriteTvShow(movie.id)
        } else {
            insertFavoriteTvShow(movie)
        }
    }

    fun showExist(showId: Int) = repository.checkIfTvShowIsFavorite(showId)
}
