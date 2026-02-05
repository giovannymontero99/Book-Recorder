package com.castor.bookrecorder.core.presentation.pages.memory_box.new_memory_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.R
import java.util.UUID

// Data class to hold a key-value pair with a unique ID for the list
data class MemoryField(val id: UUID = UUID.randomUUID(), var key: String, var value: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBoxMemoryScreen(
    viewModel: NewBoxMemoryViewModel = hiltViewModel()
) {
    // This list will hold the key-value fields.
    // NOTE: For a complete implementation, this list should be managed in your ViewModel
    // to survive configuration changes and be saved correctly.
    val memoryFields = remember { mutableStateListOf(MemoryField(key = "", value = "")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_box_memory),
                        color = MaterialTheme.colorScheme.background
                    )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(memoryFields) { index, field ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = field.key,
                            onValueChange = { updatedKey ->
                                memoryFields[index] = memoryFields[index].copy(key = updatedKey)
                            },
                            label = { Text("Key") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Sentences
                            )
                        )

                        OutlinedTextField(
                            value = field.value,
                            onValueChange = { updatedValue ->
                                memoryFields[index] = memoryFields[index].copy(value = updatedValue)
                            },
                            label = { Text("Value") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done,
                                capitalization = KeyboardCapitalization.Sentences
                            )
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = { memoryFields.add(MemoryField(key = "", value = "")) }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add another memory field"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // TODO: Call ViewModel to save the `memoryFields` list
                    // Example: viewModel.onEvent(NewBoxMemoryEvent.OnSave(memoryFields))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save Memories")
            }
        }
    }
}
