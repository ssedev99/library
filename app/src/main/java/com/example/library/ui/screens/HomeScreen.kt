package com.example.library.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.library.R
import com.example.library.model.BookItem
import com.example.library.ui.theme.LibraryTheme

@Composable
fun HomeScreen(
    libraryUiState: LibraryUiState,
    retryAction: () -> Unit,
    loadMore: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (libraryUiState) {
        is LibraryUiState.Loading -> LoadingScreen(
            modifier = modifier.fillMaxSize()
        )

        is LibraryUiState.Success -> BooksGridScreen(
            books = libraryUiState.books,
            modifier = modifier.fillMaxSize(),
            loadMore = loadMore,
            contentPadding = contentPadding,
        )

        is LibraryUiState.Error -> ErrorScreen(
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.refresh_48dp),
            contentDescription = null,
            modifier = Modifier
        )
    }

}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun BookCard(
    book: BookItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
//            .fillMaxHeight()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.volumeInfo.imageLinks.thumbnail)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            error = painterResource(R.drawable.sync_problem_48dp),
            placeholder = painterResource(R.drawable.refresh_48dp),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun BooksGridScreen(
    books: List<BookItem>,
    loadMore: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = books, key = { book -> book.id }) { book ->
            BookCard(
                book,
                modifier = modifier
                    .padding(4.dp)
                    .height(280.dp)
            )

        }
    }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.canScrollForward }
            .collect { canScrollForward ->
                if (!canScrollForward) {
                    loadMore()
                }
            }
    }
}


@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LibraryTheme {
        LoadingScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    LibraryTheme {
        ErrorScreen(
            retryAction = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}