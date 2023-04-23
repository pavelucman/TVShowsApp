package com.pavel.tvshows.features.favorite

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.pavel.tvshows.data.local.models.FavoritesTable
import com.pavel.tvshows.features.common.BottomNavigation
import com.pavel.tvshows.features.common.ListContent

@OptIn(ExperimentalCoilApi::class)
@Composable
fun FavoriteScreen(
    navController: NavHostController,
    viewModel: FavoriteViewModel = hiltViewModel()
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val trendingMoviesFavoriteFlowLifecycleAware =
        remember(viewModel.getFavoriteMovie(), lifecycleOwner) {
            viewModel.getFavoriteMovie()
                .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
        }
    val getFavoriteMovies: List<FavoritesTable> by trendingMoviesFavoriteFlowLifecycleAware.collectAsState(
        initial = emptyList()
    )

    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }, content = {
            ListContent(listMovies = getFavoriteMovies, navController = navController)
        })
}


