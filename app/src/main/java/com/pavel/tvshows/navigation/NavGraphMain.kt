package com.pavel.tvshows.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.pavel.tvshows.Constants.DETAIL_ARGUMENT
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.features.detail.DetailScreen
import com.pavel.tvshows.features.favorite.FavoriteScreen
import com.pavel.tvshows.features.home.HomeScreen
import com.pavel.tvshows.features.search.SearchScreen

@ExperimentalCoilApi
@ExperimentalPagingApi
@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(route = Screen.Detail.route) {
            it.arguments?.getParcelable<TrendingTvShowsTable>(DETAIL_ARGUMENT)?.let { movie ->
                DetailScreen(movie = movie, navController = navController)
            }
        }
        composable(route = Screen.Favorite.route) {
            FavoriteScreen(navController = navController)
        }
    }
}