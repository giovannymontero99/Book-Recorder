package com.castor.bookrecorder.core.presentation.pages.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.presentation.ui.theme.PurpleGrey40

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {

    val books by viewModel.books.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(books, key = { it.id }) { book ->
            BookItem(
                book = book,
                onFavoriteClick = {
                    // viewModel.onRemoveFavorite(book)
                }
            )
        }
    }
}

@Composable
fun BookItem(
    modifier: Modifier = Modifier,
    book: Book,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .background(Color(0xFFEEEEEE))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*
            AsyncImage(
                model = book.coverUrl,
                contentDescription = "Book cover of ${book.title}",
                modifier = Modifier
                    .size(width = 70.dp, height = 100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

             */

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove from favorites",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}