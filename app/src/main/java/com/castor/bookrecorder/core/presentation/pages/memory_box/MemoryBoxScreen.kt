package com.castor.bookrecorder.core.presentation.pages.memory_box

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.R
import kotlinx.coroutines.launch
import com.castor.bookrecorder.core.domain.model.Memory
import com.castor.bookrecorder.core.domain.model.MemoryBooleanValue
import com.castor.bookrecorder.core.domain.model.MemoryIntValue
import com.castor.bookrecorder.core.domain.model.MemoryStringValue
import com.castor.bookrecorder.core.presentation.component.atoms.ConfirmAlertDialog
import com.castor.bookrecorder.core.presentation.component.atoms.ModalBottomSheetOptions
import com.castor.bookrecorder.core.presentation.component.atoms.ModalBottomSheetOptionsItem
import com.castor.bookrecorder.core.presentation.component.atoms.PrimarySubmitButton
import com.castor.bookrecorder.core.presentation.component.atoms.SecondarySubmitButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryBoxScreen(
    viewModel: MemoryBoxViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var selectedMemory by remember { mutableStateOf<Memory?>(null) }
    var memoryToDelete by remember { mutableStateOf<Memory?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val folderRotation = remember { Animatable(0f) }
    var isFolderOpen by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        FolderIcon(
            isOpen = isFolderOpen,
            rotation = folderRotation.value,
            onClick = {
                coroutineScope.launch {
                    if (!isFolderOpen) {
                        folderRotation.animateTo(
                            targetValue = -30f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                    } else {
                        folderRotation.animateTo(
                            targetValue = 0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                    }
                    isFolderOpen = !isFolderOpen
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.memories.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_memories),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
                ) {
                    items(state.memories) { memory ->
                        MemoryItem(
                            memory = memory,
                            onLongClick = { selectedMemory = memory }
                        )
                    }
                }
            }
        }
    } // Column

    if (selectedMemory != null) {
        ModalBottomSheetOptions(
            onDismissRequest = { selectedMemory = null },
            sheetState = sheetState
        ) {
            ModalBottomSheetOptionsItem(
                modifier = Modifier,
                title = stringResource(R.string.delete),
                onClick = {
                    memoryToDelete = selectedMemory
                    selectedMemory = null
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            )
        }
    }

    if (memoryToDelete != null) {
        ConfirmAlertDialog(
            title = stringResource(R.string.delete),
            text = stringResource(R.string.are_you_sure_you_want_to_delete_this_memory),
            onDismissRequest = { memoryToDelete = null },
            confirmButton = {
                PrimarySubmitButton(onClick = {
                    viewModel.handleEvent(MemoryBoxEvent.OnDelete(memoryToDelete!!.id))
                    memoryToDelete = null
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                SecondarySubmitButton(onClick = { memoryToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun FolderIcon(
    isOpen: Boolean,
    rotation: Float,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = if (isOpen) Icons.Default.FolderOpen else Icons.Default.Folder,
            contentDescription = stringResource(R.string.app_name),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    rotationZ = rotation
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f)
                }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isOpen) stringResource(R.string.app_name) else stringResource(R.string.app_name),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryItem(memory: Memory, onLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = memory.value.text,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = when (val v = memory.value) {
                        is MemoryStringValue -> v.value
                        is MemoryIntValue -> v.value.toString()
                        is MemoryBooleanValue -> if (v.value) "True" else "False"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (memory.value) {
                        is MemoryStringValue -> Icons.Default.TextFields
                        is MemoryIntValue -> Icons.Default.Numbers
                        is MemoryBooleanValue -> Icons.Default.ToggleOn
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
