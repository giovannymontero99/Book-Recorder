package com.castor.bookrecorder.core.presentation.pages.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.domain.usecase.character.DeleteCharacterByIdUseCase
import com.castor.bookrecorder.core.domain.usecase.character.GetCharactersByBookIdUseCase
import com.castor.bookrecorder.core.domain.usecase.character.UpsertCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface BookDetailEvent {
    data class SearchCharactersByBook(val id: Int) : BookDetailEvent
    data class AddCharacter(val character: Character) : BookDetailEvent
    data class DeleteCharacter(val id: Int) : BookDetailEvent
}


@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val getCharactersByBookIdUseCase: GetCharactersByBookIdUseCase,
    private val upsertCharacterUseCase: UpsertCharacterUseCase,
    private val deleteCharacterByIdUseCase: DeleteCharacterByIdUseCase
): ViewModel() {

    private val _charactersList = MutableStateFlow<List<Character>>(emptyList())
    val charactersList: StateFlow<List<Character>> = _charactersList.asStateFlow()

    fun listener(event: BookDetailEvent){
        when(event){
            is BookDetailEvent.SearchCharactersByBook -> {
                /*
                searchCharactersByBook(event.id)

                 */
            }

            is BookDetailEvent.AddCharacter -> {
                addCharacter(event.character)
            }

            is BookDetailEvent.DeleteCharacter -> {
                deleteCharacter(event.id)
            }
        }
    }

    private fun searchCharactersByBook(id: String){
        viewModelScope.launch {
            /*
            getCharactersByBookIdUseCase(id).collectLatest { characters ->
                _charactersList.update { characters }
            }

             */
        }
    }

    private fun addCharacter(character: Character){
        viewModelScope.launch {
            upsertCharacterUseCase(character)
        }
    }

    private fun deleteCharacter(id: Int){
        viewModelScope.launch {
            deleteCharacterByIdUseCase(id)
        }
    }
}