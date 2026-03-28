package com.castor.bookrecorder.core.presentation.pages.memory_box.new_memory_box

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.domain.model.Memory
import com.castor.bookrecorder.core.domain.model.MemoryBooleanValue
import com.castor.bookrecorder.core.domain.model.MemoryIntValue
import com.castor.bookrecorder.core.domain.model.MemoryStringValue
import com.castor.bookrecorder.core.domain.usecase.memory.InsertMemoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

enum class MemoryValueType { TEXT, NUMBER, BOOLEAN }

data class NewBoxMemoryState(
    val text: String = "",
    val value: String = "",
    val booleanValue: Boolean = false,
    val type: MemoryValueType = MemoryValueType.TEXT,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    @StringRes val valueError: Int? = null
)

sealed interface NewBoxMemoryEvent {
    data class OnTextChange(val text: String) : NewBoxMemoryEvent
    data class OnValueChange(val value: String) : NewBoxMemoryEvent
    data class OnBooleanValueChange(val value: Boolean) : NewBoxMemoryEvent
    data class OnTypeChange(val type: MemoryValueType) : NewBoxMemoryEvent
    data object OnSave : NewBoxMemoryEvent
}

@HiltViewModel
class NewBoxMemoryViewModel @Inject constructor(
    private val insertMemoryUseCase: InsertMemoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewBoxMemoryState())
    val state = _state.asStateFlow()

    fun handleEvent(event: NewBoxMemoryEvent) {
        when (event) {
            is NewBoxMemoryEvent.OnTextChange -> _state.update { it.copy(text = event.text) }
            is NewBoxMemoryEvent.OnValueChange -> _state.update { it.copy(value = event.value, valueError = null) }
            is NewBoxMemoryEvent.OnBooleanValueChange -> _state.update { it.copy(booleanValue = event.value) }
            is NewBoxMemoryEvent.OnTypeChange -> _state.update {
                it.copy(type = event.type, value = "", booleanValue = false, valueError = null)
            }
            is NewBoxMemoryEvent.OnSave -> saveMemory()
        }
    }

    private fun saveMemory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            if (state.value.type == MemoryValueType.NUMBER) {
                val rawValue = state.value.value
                if (rawValue.contains('.') || rawValue.contains(',')) {
                    _state.update { it.copy(isLoading = false, valueError = R.string.error_number_decimal) }
                    return@launch
                }
                if (rawValue.toIntOrNull() == null) {
                    _state.update { it.copy(isLoading = false, valueError = R.string.error_number_out_of_range) }
                    return@launch
                }
            }

            val memoryId = UUID.randomUUID().toString()
            val memoryValueId = UUID.randomUUID().toString()
            val memoryValue = when (state.value.type) {
                MemoryValueType.TEXT -> MemoryStringValue(
                    id = memoryValueId,
                    text = state.value.text,
                    value = state.value.value
                )
                MemoryValueType.NUMBER -> MemoryIntValue(
                    id = memoryValueId,
                    text = state.value.text,
                    value = state.value.value.toInt()
                )
                MemoryValueType.BOOLEAN -> MemoryBooleanValue(
                    id = memoryValueId,
                    text = state.value.text,
                    value = state.value.booleanValue
                )
            }
            insertMemoryUseCase(Memory(id = memoryId, created = System.currentTimeMillis(), value = memoryValue))
            _state.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}