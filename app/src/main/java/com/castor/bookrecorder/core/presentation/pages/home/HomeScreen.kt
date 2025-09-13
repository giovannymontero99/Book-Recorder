package com.castor.bookrecorder.core.presentation.pages.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.presentation.component.CardBorder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToBookDetail: (String, String) -> Unit,
    onNavigateToAddBook: () -> Unit,
    onNavigateToEditBook: (String) -> Unit,
    onNavigateToAccount: () -> Unit
) {
    val booksList by viewModel.booksList.collectAsState()
    val onClick = viewModel::onClick
    val auth = Firebase.auth

    // Modal bottom Sheet handler
    val sheetState = rememberModalBottomSheetState()
    var showModal by remember { mutableStateOf(false) }

    // Delete alert handler
    var showDeleteAlert by remember { mutableStateOf(false) }
    var idItemSelected by remember { mutableStateOf<String?>(null) }

    if(showDeleteAlert){
        AlertDialog(
            onDismissRequest = { showDeleteAlert = false },
            title = { Text(stringResource(R.string.delete_character)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete_this_character)) },
            confirmButton = {
                Button(onClick = {
                    onClick(HomeEvent.DeleteBook(idItemSelected!!))
                    showDeleteAlert = false
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteAlert = false
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            textContentColor = MaterialTheme.colorScheme.onBackground,
        )
    }

    if(showModal){
        ModalBottomSheet(
            onDismissRequest = { showModal = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            ListItem(
                modifier = Modifier
                    .clickable{
                        showDeleteAlert = true
                        showModal = false
                    },
                headlineContent = {
                    Text(stringResource(R.string.delete))
                },
                leadingContent = {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    headlineColor = MaterialTheme.colorScheme.onBackground,
                    leadingIconColor = MaterialTheme.colorScheme.onBackground
                )
            )

            ListItem(
                modifier = Modifier
                    .clickable{
                        onNavigateToEditBook(idItemSelected!!)
                        showModal = false
                    },
                headlineContent = {
                    Text(text = stringResource(R.string.edit))
                },
                leadingContent = {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    headlineColor = MaterialTheme.colorScheme.onBackground,
                    leadingIconColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToAddBook() }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Book")
            }
        },
        topBar = {
            TopAppBar(
                actions = {
                    if(auth.currentUser?.photoUrl == null){
                        Image(
                            painter = painterResource(R.drawable.userphoto_desnt_exist),
                            contentDescription = "User profile picture",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(30.dp)
                                .clip(CircleShape)
                                .clickable{ onNavigateToAccount() }
                        )
                    }else{
                        AsyncImage(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(30.dp)
                                .clip(CircleShape)
                                .clickable{ onNavigateToAccount() },
                            model =  auth.currentUser?.photoUrl,
                            contentDescription = "User profile picture"
                        )
                    }

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
                    onClick = {
                        onNavigateToBookDetail(item.id, item.title)
                    },
                    onShowModalOptions = {
                        showModal = true
                        idItemSelected = item.id
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
    onClick: () -> Unit,
    onShowModalOptions: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    CardBorder(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = {
                    onShowModalOptions()
                }
            ),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = author, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

