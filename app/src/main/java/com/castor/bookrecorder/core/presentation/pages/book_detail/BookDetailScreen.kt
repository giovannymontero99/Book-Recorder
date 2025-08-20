package com.castor.bookrecorder.core.presentation.pages.book_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
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
    id: Int,
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

    LaunchedEffect(id) {
        listener(BookDetailEvent.SearchCharactersByBook(id))
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
                                    bookId = id,
                                    name = characterName,
                                    description = characterDescription.ifEmpty { null },
                                    firstAppearancePage = characterFirstAppearancePage.toIntOrNull()
                                )
                            ))
                            showCharacterForm = false
                        }, enabled = characterName.isNotBlank()) {
                            Text(stringResource(R.string.save_character))
                        }
                    }
                }
            }
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
                    onDeleteCharacter = { character ->
                        listener(BookDetailEvent.DeleteCharacter(character.id))
                    },
                    onEditCharacter = { character ->
                        characterId = character.id
                        characterName = character.name
                        characterDescription = character.description ?: ""
                        characterFirstAppearancePage = character.firstAppearancePage?.toString() ?: ""
                        showCharacterForm = true
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
    onDeleteCharacter: (Character) -> Unit,
    onEditCharacter: (Character) -> Unit
) {
    CardBorder(
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clickable{ onEditCharacter(character) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(character.name, style = MaterialTheme.typography.titleMedium)
                IconButton(
                    onClick = { onDeleteCharacter(character)  }//listener(BookDetailEvent.DeleteCharacter(it.id)) }
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Delete character")
                }
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