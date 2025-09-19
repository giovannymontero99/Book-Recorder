package com.castor.bookrecorder.core.presentation.pages.bookslist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.presentation.component.CardBorder
import com.castor.bookrecorder.core.presentation.component.molecules.ModalBottomSheetOptionsItem
import com.castor.bookrecorder.core.presentation.component.molecules.PrimarySubmitButton
import com.castor.bookrecorder.core.presentation.component.molecules.SecondarySubmitButton
import com.castor.bookrecorder.core.presentation.component.organisms.ConfirmAlertDialog
import com.castor.bookrecorder.core.presentation.component.organisms.ModalBottomSheetOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksListScreen(
    modifier: Modifier = Modifier,
    onNavigateToBookDetail: (String, String) -> Unit,
    onNavigateToEditBook: (String) -> Unit = {},
    viewModel: BooksListViewModel = hiltViewModel()
) {

    // Observe books list
    val booksList by viewModel.booksList.collectAsState()

    // Handle events
    val onEvent = viewModel::onEvent

    // Handle Modal
    var showModal by remember { mutableStateOf(false) }
    var idItemSelected by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()

    // Delete alert handler
    var showDeleteAlert by remember { mutableStateOf(false) }

    if(showDeleteAlert){
        ConfirmAlertDialog(
            onDismissRequest = { showDeleteAlert = false },
            confirmButton = {
                PrimarySubmitButton(
                    onClick = {
                        showDeleteAlert = false
                        onEvent(BooksListEvent.OnDeleteBook(idItemSelected!!))
                    }
                ){
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                SecondarySubmitButton(
                    onClick = {
                        showDeleteAlert = false
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = stringResource(R.string.delete_character),
            text = stringResource(R.string.are_you_sure_you_want_to_delete_this_character)
        )
    }

    if(showModal){

        ModalBottomSheetOptions(
            onDismissRequest = { showModal = false },
            sheetState = sheetState
        ){
            ModalBottomSheetOptionsItem(
                onClick = {
                    showDeleteAlert = true // Show delete alert
                    showModal = false // Hide actual modal
                },
                title = stringResource(R.string.delete),
                icon = {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                }
            )

            ModalBottomSheetOptionsItem(
                onClick = {
                    onNavigateToEditBook(idItemSelected!!)
                    showModal = false
                },
                title = stringResource(R.string.edit),
                icon = {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            )
        }
    }

    LazyColumn(
        modifier = modifier
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