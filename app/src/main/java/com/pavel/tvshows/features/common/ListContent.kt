package com.pavel.tvshows.features.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.pavel.tvshows.Constants
import com.pavel.tvshows.Constants.DETAIL_ARGUMENT
import com.pavel.tvshows.R
import com.pavel.tvshows.convertToTrendingTvShow
import com.pavel.tvshows.data.local.models.FavoritesTable
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.data.remote.ShowsApi
import com.pavel.tvshows.features.home.HomeViewModel
import com.pavel.tvshows.navigation.Screen
import com.pavel.tvshows.ui.theme.HeartRed

@OptIn(ExperimentalPagingApi::class)
@ExperimentalCoilApi
@Composable
fun ListContentPaged(
    pagedItemsLazy: LazyPagingItems<TrendingTvShowsTable>,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(end = 12.dp, start = 12.dp, top = 12.dp, bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = pagedItemsLazy,
            key = { movie ->
                movie.id
            }
        ) { movieTable ->
            if (movieTable != null) {
                MovieItem(movie = movieTable, navController = navController)
            }
        }

        pagedItemsLazy.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading && loadState.append.endOfPaginationReached -> {
                    item {
                        LoadingItem()
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = pagedItemsLazy.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = pagedItemsLazy.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}


@ExperimentalCoilApi
@Composable
fun ListContent(listMovies: List<FavoritesTable>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(end = 12.dp, start = 12.dp, top = 12.dp, bottom = 60.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = listMovies,
            key = { movie ->
                movie.favoriteId
            }
        ) { favoriteTable ->
            MovieItemFavorite(favoriteTvShow = favoriteTable, navController = navController)
        }
    }
}


@ExperimentalCoilApi
@Composable
fun MovieItemFavorite(favoriteTvShow: FavoritesTable, navController: NavHostController?) {
    val painter =
        rememberImagePainter(data = "${ShowsApi.IMAGE_SOURCE}${favoriteTvShow.backdropPath}") {
            crossfade(durationMillis = 1000)
            error(R.drawable.ic_placeholder)
            placeholder(R.drawable.ic_placeholder)
        }
    Box(
        modifier = Modifier
            .clickable {
                val trendingTvShowsTable = convertToTrendingTvShow(favoriteTvShow = favoriteTvShow)
                navController?.navigate(Screen.Detail.route)
                navController?.currentBackStackEntry?.arguments?.putParcelable(
                    DETAIL_ARGUMENT,
                    trendingTvShowsTable
                )
            }
            .height(300.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = stringResource(R.string.movie_poster),
            contentScale = ContentScale.Crop
        )
        Surface(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .alpha(ContentAlpha.medium),
            color = Color.Black
        ) {}
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                        append(favoriteTvShow.title)
                    }
                },
                color = Color.White,
                fontSize = MaterialTheme.typography.caption.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            HeartIcon(
                modifier = Modifier
                    .weight(3f)
                    .padding(6.dp),
                painter = painterResource(id = R.drawable.full_heart),
            )
        }
    }
}


@OptIn(ExperimentalPagingApi::class)
@ExperimentalCoilApi
@Composable
fun MovieItem(
    movie: TrendingTvShowsTable,
    navController: NavHostController?,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val painter = rememberImagePainter(data = "${ShowsApi.IMAGE_SOURCE}${movie.backdropPath}") {
        crossfade(durationMillis = 1000)
        error(R.drawable.ic_placeholder)
        placeholder(R.drawable.ic_placeholder)
    }
    Box(
        modifier = Modifier
            .clickable {
                navController?.navigate(Screen.Detail.route)
                navController?.currentBackStackEntry?.arguments?.putParcelable(
                    DETAIL_ARGUMENT,
                    movie
                )
            }
            .height(300.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = stringResource(R.string.movie_poster),
            contentScale = ContentScale.Crop
        )
        Surface(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .alpha(ContentAlpha.medium),
            color = Color.Black
        ) {}
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                        append(movie.title)
                    }
                },
                color = Color.White,
                fontSize = MaterialTheme.typography.caption.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            HeartIcon(
                modifier = Modifier
                    .weight(3f)
                    .padding(6.dp),
                painter = painterResource(id = if (viewModel.showExist(movie.id)) R.drawable.full_heart else R.drawable.empty_heart),
            )
        }
    }
}

@Composable
fun HeartIcon(
    modifier: Modifier,
    painter: Painter,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            painter = painter,
            contentDescription = "Heart Icon",
            tint = HeartRed
        )
    }
}


@ExperimentalCoilApi
@Composable
fun ListContentPagedDetail(
    pagedItemsLazy: LazyPagingItems<TrendingTvShowsTable>,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(end = 12.dp, start = 12.dp, top = 12.dp, bottom = 60.dp),
        ) {
            items(
                items = pagedItemsLazy,
                key = { movie ->
                    movie.id
                }
            ) { movieTable ->
                if (movieTable != null) {
                    DetailItem(movie = movieTable, navController = navController)
                }
            }

            pagedItemsLazy.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                    }
                    loadState.append is LoadState.Loading && loadState.append.endOfPaginationReached -> {
                        item {
                            LoadingItem()
                        }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = pagedItemsLazy.loadState.refresh as LoadState.Error
                        item {
                            ErrorItem(
                                message = e.error.localizedMessage!!,
                                modifier = Modifier.fillParentMaxSize(),
                                onClickRetry = { retry() }
                            )
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = pagedItemsLazy.loadState.append as LoadState.Error
                        item {
                            ErrorItem(
                                message = e.error.localizedMessage!!,
                                onClickRetry = { retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun DetailItem(movie: TrendingTvShowsTable, navController: NavHostController?) {
    val painter = rememberImagePainter(data = "${ShowsApi.IMAGE_SOURCE}${movie.backdropPath}") {
        crossfade(durationMillis = 1000)
        error(R.drawable.ic_placeholder)
        placeholder(R.drawable.ic_placeholder)
    }
    Box(
        modifier = Modifier
            .clickable {
                navController?.navigate(Screen.Detail.route)
                navController?.currentBackStackEntry?.arguments?.putParcelable(
                    Constants.DETAIL_ARGUMENT,
                    movie
                )
            }
            .height(350.dp)
            .width(250.dp)
            .padding(5.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp)),
            painter = painter,
            contentDescription = stringResource(R.string.movie_poster),
            contentScale = ContentScale.Crop
        )
    }
}


@OptIn(ExperimentalPagingApi::class)
@ExperimentalCoilApi
@Composable
@Preview
fun MoviePreview() {
    MovieItem(
        movie = TrendingTvShowsTable(
            voteCount = 12,
            voteAverage = 5.2,
            video = false,
            title = "New Movie",
            releaseDate = "",
            posterPath = "https://static.wikia.nocookie.net/punchtimeexplosion/images/c/cd/Johnny_%281%29.png/revision/latest?cb=20200525141809",
            popularity = 50.0,
            overview = "",
            originalTitle = "Somehting new",
            originalName = "sss",
            originalLanguage = "en",
            mediaType = "movie",
            id = 1,
            firstAirDate = "",
            backdropPath = "https://static.wikia.nocookie.net/punchtimeexplosion/images/c/cd/Johnny_%281%29.png/revision/latest?cb=20200525141809",
            adult = false,
            name = "",
            page = 0
        ),
        navController = NavHostController(LocalContext.current),
        viewModel = hiltViewModel()
    )
}