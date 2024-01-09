package com.zivkesten.searchmovies.domain.mapper

import com.zivkesten.searchmovies.data.api.model.MovieDto
import com.zivkesten.searchmovies.data.local.MovieEntity
import com.zivkesten.searchmovies.domain.model.Movie


fun MovieDto.toDomain() = Movie(
    imdbID = this.imdbID,
    title = this.title,
    year = this.year,
    type = this.type,
    poster = this.poster
)

fun List<Movie>.toEntity() = map {
    MovieEntity(
        id = it.imdbID.hashCode(),
        title = it.title,
        year = it.year,
        type = it.type,
        poster = it.poster
    )
}

fun MovieDto.toEntity() = MovieEntity(
    id = imdbID.hashCode(),
    title = this.title,
    year = this.year,
    type = this.type,
    poster = this.poster
)

fun List<MovieEntity>.toDomainMedia() = map {
    Movie(
        title = it.title,
        year = it.year,
        imdbID = it.imdbID,
        poster = it.poster,
        type = it.type
    )
}
