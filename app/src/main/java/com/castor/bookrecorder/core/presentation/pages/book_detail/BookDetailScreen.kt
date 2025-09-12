package com.castor.bookrecorder.core.presentation.pages.book_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.presentation.component.CardBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    id: String?,
    title: String,
    viewModel: BookDetailViewModel = hiltViewModel()
) {

    val listener = viewModel::listener

    val charactersList by viewModel.charactersList.collectAsState()

    var showCharacterForm by remember { mutableStateOf(false) }

    var characterName by remember { mutableStateOf("") }
    var characterDescription by remember { mutableStateOf("") }
    var characterFirstAppearancePage by remember { mutableStateOf("") }
    var characterId by remember { mutableIntStateOf(0) }

    // Allow see the modal bottom sheet
    var showModalBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    // Handle alert to delete character
    var showDeleteAlert by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        if (id != null) listener(BookDetailEvent.SearchCharactersByBook(id))
    }
    LaunchedEffect(showCharacterForm) {
        if(!showCharacterForm){
            characterId = 0
            characterName = ""
            characterDescription = ""
            characterFirstAppearancePage = ""
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar( title = { Text(text = title, color = MaterialTheme.colorScheme.background) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCharacterForm = !showCharacterForm }) {
                Icon(Icons.Filled.Add, "Add Character")
            }
        }
    ) { innerPadding ->

        val commonModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

        if (showCharacterForm) {

            Dialog(
                onDismissRequest = { showCharacterForm = false },
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End,
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.add_character),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            IconButton(
                                onClick = { showCharacterForm = false },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = "Close form")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = characterName,
                            onValueChange = { characterName = it },
                            label = { Text(stringResource(R.string.character_name)) },
                            modifier = commonModifier,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Sentences
                            )
                        )
                        OutlinedTextField(
                            value = characterDescription,
                            onValueChange = { characterDescription = it },
                            label = { Text(stringResource(R.string.description)) },
                            modifier = commonModifier,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Sentences
                            )
                        )
                        OutlinedTextField(
                            value = characterFirstAppearancePage,
                            onValueChange = { characterFirstAppearancePage = it.filter { char -> char.isDigit() } },
                            label = { Text(stringResource(R.string.page_first_appearance)) },
                            modifier = commonModifier,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done,
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {

                            listener(BookDetailEvent.AddCharacter(
                                Character(
                                    id = characterId,
                                    bookId = id ?: "",
                                    name = characterName,
                                    description = characterDescription,
                                    firstAppearancePage = characterFirstAppearancePage.toIntOrNull()
                                ),
                                id!!
                            ))

                            showCharacterForm = false
                        }, enabled = characterName.isNotBlank()) {
                            Text(stringResource(R.string.save_character))
                        }
                    }
                }
            }
        }

        if(showModalBottomSheet){
            ModalBottomSheet(
                onDismissRequest = {
                    showModalBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                ListItem(
                    modifier = Modifier
                        .clickable{
                            showDeleteAlert = true
                            showModalBottomSheet = false
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
                            showCharacterForm = true
                            showModalBottomSheet = false
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

        if(showDeleteAlert){
            AlertDialog(
                onDismissRequest = { showDeleteAlert = false },
                title = { Text(stringResource(R.string.delete_character)) },
                text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete_this_character)) },
                confirmButton = {
                    Button(onClick = {
                        listener(BookDetailEvent.DeleteCharacter(characterId, id!!))
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



        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
        ) {
            items(charactersList){

                CharacterCard(
                    character = it,
                    onShowActionsSheet = { character ->
                        showModalBottomSheet = true
                        characterId = character.id
                        characterName = character.name
                        characterDescription = character.description ?: ""
                        characterFirstAppearancePage = character.firstAppearancePage?.toString() ?: ""
                    }
                )


            }
        }
    }
}

@Composable
fun CharacterCard(
    modifier: Modifier = Modifier,
    character: Character,
    onShowActionsSheet: (Character) -> Unit
) {
    CardBorder(
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clickable{ onShowActionsSheet(character) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(character.name, style = MaterialTheme.typography.titleMedium)
            }

            character.description?.let { description ->
                Text(description, style = MaterialTheme.typography.bodyMedium)
            }
            character.firstAppearancePage?.let { page ->
                Text("${stringResource(R.string.first_appearance)}: $page", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}