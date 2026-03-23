package com.castor.bookrecorder.core.presentation.pages.memory_box.new_memory_box

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBoxMemoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: NewBoxMemoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val handleEvent = viewModel::handleEvent
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onNavigateBack()
        }
    }

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.text,
                onValueChange = { handleEvent(NewBoxMemoryEvent.OnTextChange(it)) },
                label = { Text(stringResource(R.string.memory_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            MemoryTypeSelector(
                selectedType = state.type,
                onTypeSelected = { handleEvent(NewBoxMemoryEvent.OnTypeChange(it)) }
            )

            if (state.type == MemoryValueType.BOOLEAN) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.memory_value),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Switch(
                        checked = state.booleanValue,
                        onCheckedChange = { handleEvent(NewBoxMemoryEvent.OnBooleanValueChange(it)) }
                    )
                }
            } else {
                OutlinedTextField(
                    value = state.value,
                    onValueChange = { handleEvent(NewBoxMemoryEvent.OnValueChange(it)) },
                    label = { Text(stringResource(R.string.memory_value)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.valueError != null,
                    supportingText = state.valueError?.let { errorRes ->
                        { Text(stringResource(errorRes)) }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (state.type == MemoryValueType.NUMBER)
                            KeyboardType.Number else KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = if (state.type == MemoryValueType.TEXT)
                            KeyboardCapitalization.Sentences else KeyboardCapitalization.None
                    )
                )
            }

            Button(
                onClick = {
                    keyboardController?.hide()
                    handleEvent(NewBoxMemoryEvent.OnSave)
                },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_memory))
            }
        }
    }
}

@Composable
private fun MemoryTypeSelector(
    selectedType: MemoryValueType,
    onTypeSelected: (MemoryValueType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.memory_type),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        MemoryTypeIconButton(
            isSelected = selectedType == MemoryValueType.TEXT,
            onClick = { onTypeSelected(MemoryValueType.TEXT) },
            icon = {
                Icon(
                    imageVector = Icons.Default.TextFields,
                    contentDescription = stringResource(R.string.memory_type_text)
                )
            }
        )

        MemoryTypeIconButton(
            isSelected = selectedType == MemoryValueType.NUMBER,
            onClick = { onTypeSelected(MemoryValueType.NUMBER) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Numbers,
                    contentDescription = stringResource(R.string.memory_type_number)
                )
            }
        )

        MemoryTypeIconButton(
            isSelected = selectedType == MemoryValueType.BOOLEAN,
            onClick = { onTypeSelected(MemoryValueType.BOOLEAN) },
            icon = {
                Icon(
                    imageVector = Icons.Default.ToggleOn,
                    contentDescription = stringResource(R.string.memory_type_boolean)
                )
            }
        )
    }
}

@Composable
private fun MemoryTypeIconButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    val borderColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outline

    val tint = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    IconButton(
        onClick = onClick,
        modifier = Modifier.border(
            width = 2.dp,
            color = borderColor,
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        CompositionLocalProvider(LocalContentColor provides tint, content = icon)
    }
}
