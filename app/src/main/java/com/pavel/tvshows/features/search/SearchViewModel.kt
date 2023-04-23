package com.pavel.tvshows.features.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.pavel.tvshows.convertToTrendingShowFromSearchResponse
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.data.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DefaultRepository
) : ViewModel() {

    private val _searchQuery: MutableState<String> = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _searchedTvShows: MutableStateFlow<PagingData<TrendingTvShowsTable>> =
        MutableStateFlow(PagingData.empty())
    val searchedTvShows: StateFlow<PagingData<TrendingTvShowsTable>> = _searchedTvShows

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchHeroes(query: String) {
        viewModelScope.launch {
            repository.searchTvShows(tvShowName = query).cachedIn(viewModelScope)
                .collect { movies ->
                    _searchedTvShows.value = movies.map { searchedTvShow ->
                        convertToTrendingShowFromSearchResponse(searchedTvShow)
                    }
                }
        }
    }
}