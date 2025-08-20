package com.castor.bookrecorder.core.presentation.pages.add_book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    modifier: Modifier = Modifier,
    id: Int = 0,
    viewModel: AddBookViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {}
) {


    val state by viewModel.state.collectAsState()
    val handleEvent = viewModel::handleEvent

    LaunchedEffect(id) {
        if(id != 0){
            viewModel.getBookById(id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.add_new_book), color = MaterialTheme.colorScheme.background) })
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AddBookOutlinedTextField(
                value = state.title,
                onValueChange = { handleEvent(AddBookUiState.OnSaveNameChange(it)) },
                labelInt = R.string.title
            )
            AddBookOutlinedTextField(
                value = state.author,
                onValueChange = { handleEvent(AddBookUiState.OnSaveAuthorChange(it)) },
                labelInt = R.string.author
            )
            AddBookOutlinedTextField(
                value = state.genre?: "",
                onValueChange = { handleEvent(AddBookUiState.OnSaveGenreChange(it)) },
                labelInt = R.string.genre
            )
            AddBookOutlinedTextField(
                value = state.progress,
                onValueChange = { handleEvent(AddBookUiState.OnSaveProgressChange(it)) },
                labelInt = R.string.current_page,
                keyboardType = KeyboardType.Number
            )
            AddBookOutlinedTextField(
                value = state.totalPages,
                onValueChange = { handleEvent(AddBookUiState.OnSaveTotalPagesChange(it))},
                labelInt = R.string.total_pages,
                keyboardType = KeyboardType.Number
            )
            AddBookOutlinedTextField(
                value = state.notes?: "",
                onValueChange = { handleEvent(AddBookUiState.OnSaveNotesChange(it)) },
                labelInt = R.string.notes,
            )
            AddBookOutlinedTextField(
                value = state.summary?: "",
                onValueChange = { handleEvent(AddBookUiState.OnSaveSummaryChange(it)) },
                labelInt = R.string.summary,
            )
            AddBookOutlinedTextField(
                value = state.quotes ?: "",
                onValueChange = { handleEvent(AddBookUiState.OnSaveQuotesChange(it)) },
                labelInt = R.string.quotes,
            )

            CheckBoxAddBook(
                isFinished = state.isFinished,
                onCheckedChange = { handleEvent(AddBookUiState.OnSaveIsFinishedChange(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    handleEvent(AddBookUiState.SaveBook)
                    onNavigateToHome()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_book))
            }
        }
    }
}

@Composable
fun AddBookOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelInt: Int,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = {onValueChange(it)},
        label = { Text(stringResource(id = labelInt)) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Sentences
        )
    )
}

@Composable
fun CheckBoxAddBook(
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    isFinished: Boolean = false
) {


    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isFinished,
            onCheckedChange = {
                onCheckedChange(isFinished)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.finished_reading),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}