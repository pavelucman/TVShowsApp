package com.pavel.tvshows.features.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.pavel.tvshows.R
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.data.remote.ShowsApi
import com.pavel.tvshows.features.common.HeartIcon
import com.pavel.tvshows.features.common.ListContentPagedDetail

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalCoilApi::class)
@Composable
fun DetailScreen(
    movie: TrendingTvShowsTable,
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    var thumbIconLiked by remember {
        mutableStateOf(viewModel.showExist(movie.id))
    }
    viewModel.getSimilarTvShows(movie.id)

    val searchedMovies = viewModel.similarTvShows.collectAsLazyPagingItems()

    Scaffold(
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                val painter = rememberImagePainter(data = "${ShowsApi.IMAGE_SOURCE}${movie.backdropPath}") {
                    crossfade(durationMillis = 1000)
                    error(R.drawable.ic_placeholder)
                    placeholder(R.drawable.ic_placeholder)
                }
                Box(
                    modifier = Modifier
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
                                .padding(6.dp)
                                .clickable {
                                    thumbIconLiked = !thumbIconLiked
                                    viewModel.updateTvShowFavorite(movie)
                                },
                            painter = painterResource(id = if (thumbIconLiked) R.drawable.full_heart else R.drawable.empty_heart),
                        )
                    }
                }

                Text(text = movie.overview,
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.h6,
                    fontFamily = FontFamily.Serif)

                Text(text = stringResource(R.string.similar),
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.h5,
                    fontFamily = FontFamily.Serif)

                ListContentPagedDetail(pagedItemsLazy = searchedMovies, navController = navController)

            }
        }
    )
}

