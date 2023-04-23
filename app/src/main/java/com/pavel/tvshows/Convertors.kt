package com.pavel.tvshows

import com.pavel.tvshows.data.local.models.FavoritesTable
import com.pavel.tvshows.data.local.models.TrendingTvShowsTable
import com.pavel.tvshows.data.remote.models.SearchMoviesResponse
import com.pavel.tvshows.data.remote.models.SimilarTvShowResponse
import com.pavel.tvshows.data.remote.models.TrendingMoviesResponse

fun convertToTrendingTvShow(favoriteTvShow: FavoritesTable): TrendingTvShowsTable {
    return TrendingTvShowsTable(
        favoriteTvShow.favoriteId,
        favoriteTvShow.adult,
        favoriteTvShow.backdropPath,
        favoriteTvShow.firstAirDate,
        favoriteTvShow.mediaType,
        favoriteTvShow.name,
        favoriteTvShow.originalLanguage,
        favoriteTvShow.originalName,
        favoriteTvShow.originalTitle,
        favoriteTvShow.overview,
        favoriteTvShow.popularity,
        favoriteTvShow.posterPath,
        favoriteTvShow.releaseDate,
        favoriteTvShow.title,
        favoriteTvShow.video,
        favoriteTvShow.voteAverage,
        favoriteTvShow.voteCount,
        favoriteTvShow.page
    )
}

fun convertToTrendingShowFromResponse(
    trendingTvShows: TrendingMoviesResponse.Result?,
    page: Int?
): TrendingTvShowsTable {
    return TrendingTvShowsTable(
        name = trendingTvShows?.name ?: Constants.NO_INFO,
        adult = trendingTvShows?.adult ?: false,
        backdropPath = trendingTvShows?.backdropPath ?: Constants.NO_INFO,
        firstAirDate = trendingTvShows?.firstAirDate ?: Constants.NO_INFO,
        id = trendingTvShows?.id ?: 1,
        mediaType = trendingTvShows?.mediaType ?: Constants.NO_INFO,
        originalLanguage = trendingTvShows?.originalLanguage ?: Constants.NO_INFO,
        originalName = trendingTvShows?.originalName ?: Constants.NO_INFO,
        originalTitle = trendingTvShows?.originalTitle ?: Constants.NO_INFO,
        overview = trendingTvShows?.overview ?: Constants.NO_INFO,
        popularity = trendingTvShows?.popularity ?: 0.0,
        posterPath = trendingTvShows?.posterPath ?: Constants.NO_INFO,
        releaseDate = trendingTvShows?.releaseDate ?: Constants.NO_INFO,
        title = trendingTvShows?.title ?: trendingTvShows?.name ?: Constants.NO_INFO,
        video = trendingTvShows?.video ?: false,
        voteAverage = trendingTvShows?.voteAverage ?: 0.0,
        voteCount = trendingTvShows?.voteCount ?: 0,
        page = page ?: 0,
    )
}

fun convertToTrendingShowFromSearchResponse(searchTvShow: SearchMoviesResponse.Result): TrendingTvShowsTable {
    return TrendingTvShowsTable(
        page = 0,
        name = Constants.NO_INFO,
        adult = searchTvShow.adult ?: false,
        backdropPath = searchTvShow.backdropPath ?: Constants.NO_INFO,
        firstAirDate = Constants.NO_INFO,
        id = searchTvShow.id ?: 0,
        mediaType = Constants.NO_INFO,
        originalLanguage = Constants.NO_INFO,
        originalName = Constants.NO_INFO,
        originalTitle = searchTvShow.originalTitle ?: Constants.NO_INFO,
        overview = searchTvShow.overview ?: Constants.NO_INFO,
        popularity = searchTvShow.popularity ?: 0.0,
        posterPath = searchTvShow.posterPath ?: Constants.NO_INFO,
        releaseDate = searchTvShow.releaseDate ?: Constants.NO_INFO,
        video = searchTvShow.video ?: false,
        voteAverage = searchTvShow.voteAverage ?: 0.0,
        voteCount = searchTvShow.voteCount ?: 0,
        title = searchTvShow.title ?: searchTvShow.originalTitle ?: Constants.NO_INFO
    )
}

fun convertToFavoriteTable(tvShow: TrendingTvShowsTable): FavoritesTable {
    return FavoritesTable(
        tvShow.id,
        tvShow.adult,
        tvShow.backdropPath,
        tvShow.firstAirDate,
        tvShow.mediaType,
        tvShow.name,
        tvShow.originalLanguage,
        tvShow.originalName,
        tvShow.originalTitle,
        tvShow.overview,
        tvShow.popularity,
        tvShow.posterPath,
        tvShow.releaseDate,
        tvShow.title,
        tvShow.video,
        tvShow.voteAverage,
        tvShow.voteCount,
        tvShow.page,
    )
}

fun convertToTrendingTvShowTable(tvShow: SimilarTvShowResponse.Result): TrendingTvShowsTable {
    return TrendingTvShowsTable(
        page = 0,
        name = Constants.NO_INFO,
        adult = tvShow.adult ?: false,
        backdropPath = tvShow.backdropPath ?: Constants.NO_INFO,
        firstAirDate = Constants.NO_INFO,
        id = tvShow.id ?: 0,
        mediaType = Constants.NO_INFO,
        originalLanguage = Constants.NO_INFO,
        originalName = Constants.NO_INFO,
        originalTitle = tvShow.originalTitle ?: Constants.NO_INFO,
        overview = tvShow.overview ?: Constants.NO_INFO,
        popularity = tvShow.popularity ?: 0.0,
        posterPath = tvShow.posterPath ?: Constants.NO_INFO,
        releaseDate = tvShow.releaseDate ?: Constants.NO_INFO,
        video = tvShow.video ?: false,
        voteAverage = tvShow.voteAverage ?: 0.0,
        voteCount = tvShow.voteCount ?: 0,
        title = tvShow.title ?: tvShow.originalTitle ?: Constants.NO_INFO
    )
}