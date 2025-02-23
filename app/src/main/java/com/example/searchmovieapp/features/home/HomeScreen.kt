package com.example.searchmovieapp.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.searchmovieapp.R
import com.example.searchmovieapp.ui.model.HomeUiState
import com.example.searchmovieapp.ui.model.MovieModel
import com.example.searchmovieapp.ui.model.TypeMovie
import com.example.searchmovieapp.ui.theme.SearchMovieAppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onSnackBar: (message: String) -> Unit
) {

    val query by viewModel.query.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        modifier = modifier,
        query = query,
        uiState = uiState,
        incrementPageIndex = viewModel::incrementPageIndex,
        onClickFavoriteItem = viewModel::toggleFavoriteMovie,
        onSnackBar = onSnackBar,
        onQueryChanged = viewModel::onQueryChanged
    )

}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    query: String,
    uiState: HomeUiState,
    incrementPageIndex: () -> Unit,
    onClickFavoriteItem: (MovieModel) -> Unit,
    onSnackBar: (message: String) -> Unit,
    onQueryChanged: (String) -> Unit
) {

    val listState = rememberLazyListState()
    val listState2 = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            )
    ) {

        val threadHold = 10

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            value = query,
            onValueChange = onQueryChanged,
            singleLine = true,
            placeholder = {
                Text(text = stringResource(R.string.search))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search, contentDescription = null
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Outlined.Close, contentDescription = null
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.surface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.surface,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedLeadingIconColor = MaterialTheme.colorScheme.surface,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                focusedTrailingIconColor = MaterialTheme.colorScheme.surface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface
            )
        )

        if (uiState is HomeUiState.Success) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(8.dp),
                state = listState2
            ) {
                itemsIndexed(items = uiState.movies, key = { _, item -> item.id }) { index, item ->
                    if ((index + threadHold) >= uiState.movies.lastIndex) incrementPageIndex()
                    CardMovie(
                        modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                        movie = item,
                        onClickFavorite = { onClickFavoriteItem(item) }
                    )
                }
            }

            LaunchedEffect(listState2) {
                snapshotFlow { listState2.layoutInfo.visibleItemsInfo }.collect { visibleItems ->

                        val lastItem = visibleItems.lastOrNull()
                        val totalItems = uiState.movies.size

                        if (lastItem != null && lastItem.index == totalItems - 1) incrementPageIndex()
                    }
            }

        }

        if (uiState is HomeUiState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState is HomeUiState.Error) {
            uiState.error?.let {
                onSnackBar(it)
            }
        }
    }
}

@Composable
fun CardMovie(
    modifier: Modifier = Modifier, movie: MovieModel, onClickFavorite: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp), shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = movie.poster,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, Color.Transparent, Color.Black
                            )
                        )
                    )
            )

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd), onClick = onClickFavorite
            ) {
                Icon(
                    imageVector = if (movie.isFavorite) Icons.Outlined.Star else Icons.Outlined.StarOutline,
                    contentDescription = null,
                    tint = if (movie.isFavorite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = movie.title,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                maxLines = 2,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.primaryContainer
            )
        }
    }
}

@Composable
@Preview
fun CardMoviePreview() {
    SearchMovieAppTheme {
        CardMovie(
            movie = MovieModel(
                id = "",
                page = 1,
                title = "The Avengers",
                year = 2012,
                poster = "https://m.media-amazon.com/images/M/MV5BNGE0YTVjNzUtNzJjOS00NGNlLTgxMzctZTY4YTE1Y2Y1ZTU4XkEyXkFqcGc@._V1_SX300.jpg",
                type = TypeMovie.MOVIE
            )
        ) {

        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun HomeScreenPreview() {
    SearchMovieAppTheme(darkTheme = true) {
        HomeScreen(query = "",
            uiState = HomeUiState.Success(),
            onClickFavoriteItem = {},
            incrementPageIndex = {},
            onSnackBar = {},
            onQueryChanged = {})
    }
}