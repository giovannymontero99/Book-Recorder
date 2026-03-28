package com.castor.bookrecorder.core.presentation.pages.memory_box

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.model.Memory
import com.castor.bookrecorder.core.domain.usecase.memory.DeleteMemoryUseCase
import com.castor.bookrecorder.core.domain.usecase.memory.GetAllMemoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemoryBoxState(
    val memories: List<Memory> = emptyList(),
    val isLoading: Boolean = true
)

sealed interface MemoryBoxEvent {
    data class OnDelete(val id: String) : MemoryBoxEvent
}

@HiltViewModel
class MemoryBoxViewModel @Inject constructor(
    getAllMemoriesUseCase: GetAllMemoriesUseCase,
    private val deleteMemoryUseCase: DeleteMemoryUseCase
) : ViewModel() {

    val state: StateFlow<MemoryBoxState> = getAllMemoriesUseCase()
        .map { MemoryBoxState(memories = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MemoryBoxState()
        )

    fun handleEvent(event: MemoryBoxEvent) {
        when (event) {
            is MemoryBoxEvent.OnDelete -> viewModelScope.launch {
                deleteMemoryUseCase(event.id)
            }
        }
    }
}
