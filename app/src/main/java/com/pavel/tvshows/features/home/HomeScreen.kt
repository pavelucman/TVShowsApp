package com.pavel.tvshows.features.home

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.pavel.tvshows.features.common.BottomNavigation
import com.pavel.tvshows.features.common.ListContentPaged
import com.pavel.tvshows.navigation.Screen

@ExperimentalCoilApi
@ExperimentalPagingApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val getAllMovies = viewModel.getAllTrendingMovies.collectAsLazyPagingItems()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }, topBar = {
            HomeTopBar(
                onSearchClicked = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }, content = {
            ListContentPaged(pagedItemsLazy = getAllMovies, navController = navController)
        })
}