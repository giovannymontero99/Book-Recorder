package com.castor.bookrecorder.core.presentation.pages.memory_box.new_memory_box

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject



data class NewBoxMemoryState(
    val key: String = "",
    val value: String = "",
    val typeOfStorage: Byte = 0 // 0 String, 2 Boolean, 3 Int (First implementation)
)

sealed interface NewBoxMemoryEvent {
    data class OnKeyChange(val key: String) : NewBoxMemoryEvent
    data class OnValueChange(val value: String) : NewBoxMemoryEvent
}


@HiltViewModel
class NewBoxMemoryViewModel @Inject constructor(

): ViewModel() {


    private val _state = MutableStateFlow(NewBoxMemoryState())
    val state = _state.asStateFlow()


    fun onEvent(event: NewBoxMemoryEvent) {
        when(event){
            is NewBoxMemoryEvent.OnKeyChange -> handleOnKeyChange(event.key)
            is NewBoxMemoryEvent.OnValueChange -> handleOnValueChange(event.value)
        }
    }


    private fun handleOnKeyChange(value: String){
        _state.update { it.copy(key = value) }
    }

    private fun handleOnValueChange(value: String){
        _state.update { it.copy(value = value) }
    }

}