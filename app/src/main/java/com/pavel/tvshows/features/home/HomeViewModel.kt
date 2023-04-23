package com.pavel.tvshows.features.home

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.pavel.tvshows.data.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DefaultRepository
) : ViewModel() {

    val getAllTrendingMovies = repository.getTopTvShows()
    fun showExist(showId: Int): Boolean = repository.checkIfTvShowIsFavorite(showId)

}