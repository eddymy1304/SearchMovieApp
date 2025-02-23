package com.example.searchmovieapp.data.database

import com.example.searchmovieapp.core.IdNotFound
import com.example.searchmovieapp.core.validateNull
import com.example.searchmovieapp.data.database.entities.MovieEntity
import com.example.searchmovieapp.data.remote.response.SearchMovieResponse
import com.example.searchmovieapp.ui.model.MovieModel
import com.example.searchmovieapp.ui.model.TypeMovie

object MovieMapper : EntityToDomainMapper<MovieEntity, MovieModel>,
    ResponseToEntityMapper<SearchMovieResponse.Search, MovieEntity>,
    ResponseToDomainMapper<SearchMovieResponse.Search, MovieModel> {

    override fun asEntityFromRemote(response: SearchMovieResponse.Search): MovieEntity {
        return MovieEntity(
            id = response.imdbID.validateNull() ?: throw IdNotFound(),
            title = response.title,
            poster = response.poster,
            year = try {
                response.year?.toInt()
            } catch (e: Exception) {
                null
            },
            type = response.type,
            page = 1
        )
    }

    override fun asDomainFromDb(entity: MovieEntity): MovieModel {
        return MovieModel(
            id = entity.id,
            title = entity.title.orEmpty(),
            poster = entity.poster.orEmpty(),
            year = try {
                entity.year ?: 0
            } catch (e: Exception) {
                0
            },
            type = TypeMovie.entries.find { it.value == entity.type } ?: TypeMovie.MOVIE
        )
    }

    override fun asEntity(domain: MovieModel): MovieEntity {
        return MovieEntity(
            id = domain.id,
            title = domain.title,
            poster = domain.poster,
            year = domain.year,
            type = domain.type.value,
            isFavorite = if (domain.isFavorite) 1 else 0
        )
    }

    override fun asDomainFromRemote(response: SearchMovieResponse.Search): MovieModel {
        return MovieModel(
            id = response.imdbID.validateNull() ?: throw IdNotFound(),
            title = response.title.orEmpty(),
            poster = response.poster.orEmpty(),
            year = try {
                response.year?.toInt() ?: -1
            } catch (e: Exception) {
                -1
            },
            type = TypeMovie.entries.find { it.value == response.type } ?: TypeMovie.MOVIE
        )
    }

}

fun SearchMovieResponse.Search.asEntityFromRemote() = MovieMapper.asEntityFromRemote(this)
fun List<SearchMovieResponse.Search>.asEntityFromRemote() = map { it.asEntityFromRemote() }

fun SearchMovieResponse.Search.asDomainFromRemote() = MovieMapper.asDomainFromRemote(this)
fun List<SearchMovieResponse.Search>.asDomainFromRemote() = map { it.asDomainFromRemote() }

fun MovieModel.asEntity() = MovieMapper.asEntity(this)
fun List<MovieModel>.asEntity() = map { it.asEntity() }

fun MovieEntity.asDomainFromDb() = MovieMapper.asDomainFromDb(this)
fun List<MovieEntity>.asDomainFromDb() = map { it.asDomainFromDb() }


