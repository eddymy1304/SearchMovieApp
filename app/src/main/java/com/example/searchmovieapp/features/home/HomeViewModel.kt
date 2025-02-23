package com.example.searchmovieapp.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchmovieapp.data.MovieRepository
import com.example.searchmovieapp.ui.model.HomeUiState
import com.example.searchmovieapp.ui.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _pageIndex = MutableStateFlow(1)

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _favoritesId = repository
        .getFavoriteMovies()
        .map { list ->
            Log.d("HomeViewModel", "favorite movies list: $list")
            list.map { it.id }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            listOf()
        )

    private var _currentList = MutableStateFlow<List<MovieModel>>(listOf())

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> =
        combine(
            _query
                .debounce(1_000L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    Log.d("HomeViewModel", "search movie query: $query")
                    resetPageIndex()
                    searchMovie(query, _pageIndex.value)
                },
            _pageIndex
                .flatMapLatest { page ->
                    Log.d("HomeViewModel", "search movie page: $page")
                    if (page > 1) searchMovie(_query.value, page)
                    else flow { emit(HomeUiState.Success()) }
                },
            _favoritesId
        ) { uiStateByQuery, uiStateByPage, favoriteMoviesId ->

            val uiStateByQueryMovies = (uiStateByQuery as? HomeUiState.Success)?.movies ?: listOf()
            val uiStateByPageMovies = (uiStateByPage as? HomeUiState.Success)?.movies ?: listOf()

            val movies = (_currentList.value + uiStateByQueryMovies + uiStateByPageMovies)
                .distinctBy { it.id }

            _currentList.value = movies
            Log.d(
                "HomeViewModel", """
                currentList Size: ${_currentList.value.size}
                uiStateByQueryMovies Size: ${uiStateByQueryMovies.size}
                uiStateByPageMovies Size: ${uiStateByPageMovies.size}
                favoriteMoviesId Size: ${favoriteMoviesId.size}
            """.trimIndent()
            )

            when {
                uiStateByQuery is HomeUiState.Error -> {
                    HomeUiState.Error(uiStateByQuery.error)
                }

                uiStateByPage is HomeUiState.Error -> {
                    HomeUiState.Error(uiStateByPage.error)
                }

                uiStateByQuery is HomeUiState.Loading -> {
                    HomeUiState.Loading
                }

                uiStateByPage is HomeUiState.Loading -> {
                    HomeUiState.Loading
                }

                else -> HomeUiState.Success(
                    movies = movies.map {
                        val isFavorite = it.id in favoriteMoviesId
                        it.copy(isFavorite = isFavorite)
                    }
                )
            }

        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = HomeUiState.Success()
            )

    private fun searchMovie(query: String, page: Int): Flow<HomeUiState> {
        return flow {
            if (query.length < 3) return@flow
            emit(HomeUiState.Loading)
            repository.searchMovie(query, page)
                .onSuccess { result ->
                    emit(HomeUiState.Success(result))
                }
                .onFailure { failure ->
                    emit(HomeUiState.Error(failure.message))
                }
        }
    }

    fun onQueryChanged(query: String) {
        if (query.length > 30) return
        _query.update { query }
    }

    fun incrementPageIndex() {
        _pageIndex.value++
    }

    private fun resetPageIndex() {
        _currentList.value = listOf()
        _pageIndex.update { 1 }
    }

    private fun addFavoriteMovie(movie: MovieModel) {
        viewModelScope.launch {
            repository.addFavoriteMovie(movie)
                .onSuccess {
                }
                .onFailure {
                }
        }
    }

    private fun removeFavoriteMovie(movie: MovieModel) {
        viewModelScope.launch {
            repository.removeFavoriteMovie(movie)
                .onSuccess {
                }
                .onFailure {
                }
        }
    }

    fun toggleFavoriteMovie(movie: MovieModel) {
        if (movie.isFavorite) removeFavoriteMovie(movie)
        else addFavoriteMovie(movie)
    }
}