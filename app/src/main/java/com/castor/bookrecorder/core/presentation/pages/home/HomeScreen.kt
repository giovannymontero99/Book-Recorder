package com.castor.bookrecorder.core.presentation.pages.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.presentation.component.CardBorder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToBookDetail: (Int, String) -> Unit,
    onNavigateToAddBook: () -> Unit,
    onNavigateToEditBook: (String) -> Unit,
    onNavigateToAccount: () -> Unit
) {
    val booksList by viewModel.booksList.collectAsState()
    val onClick = viewModel::onClick
    val auth = Firebase.auth



    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToAddBook() }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Book")
            }
        },
        topBar = {
            TopAppBar(
                actions = {
                    AsyncImage(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable{ onNavigateToAccount() },
                        model =  auth.currentUser?.photoUrl,
                        contentDescription = "User profile picture"
                    )
                },

                title = {
                    Text(text = stringResource(R.string.library), color = MaterialTheme.colorScheme.background)
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(booksList){ item ->
                BookTitleItem(
                    title = item.title,
                    author = item.author,
                    onDelete = {
                        onClick(HomeEvent.DeleteBook(item.id))
                    },
                    onClick = {
                        /*
                        onNavigateToBookDetail(item.id, item.title)

                         */
                    },
                    onEdit = {
                        onNavigateToEditBook(item.id)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookTitleItem(
    modifier: Modifier = Modifier,
    title: String,
    author: String = "",
    onDelete: () -> Unit,
    onEdit: () -> Unit = {},
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    var itemSelected by remember { mutableStateOf(false) }

    CardBorder(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = {
                    itemSelected = true
                }
            ),
        border = if (itemSelected) BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary) else BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))

                IconButton(onClick = { itemSelected = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Delete book")
                    if(itemSelected){
                        DropdownMenu(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            expanded = true,
                            onDismissRequest = { itemSelected = false }
                        ) {
                            DropdownMenuItem(
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.background,
                                    leadingIconColor = MaterialTheme.colorScheme.background
                                ),
                                text = { Text("Delete") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete"
                                    )
                                },
                                onClick = {
                                    onDelete()
                                    itemSelected = false
                                }
                            )
                            DropdownMenuItem(
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.background,
                                    leadingIconColor = MaterialTheme.colorScheme.background
                                ),
                                text = { Text("Edit") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Create,
                                        contentDescription = "Edit"
                                    )
                                },
                                onClick = {
                                    onEdit()
                                    itemSelected = false
                                }
                            )

                        }
                    }
                }
            }

            Text(text = author, style = MaterialTheme.typography.bodyMedium)
        }



    }
}

