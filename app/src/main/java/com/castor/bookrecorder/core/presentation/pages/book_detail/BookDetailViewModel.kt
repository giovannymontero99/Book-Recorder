package com.castor.bookrecorder.core.presentation.pages.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.data.local.dao.CharacterDao
import com.castor.bookrecorder.core.data.local.entity.CharacterEntity
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
    data class AddCharacter(val character: CharacterEntity) : BookDetailEvent

    data class DeleteCharacter(val id: Int) : BookDetailEvent
}


@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val characterDao: CharacterDao
): ViewModel() {

    private val _characters: MutableStateFlow<List<CharacterEntity>> = MutableStateFlow(emptyList())
    val characters: StateFlow<List<CharacterEntity>> = _characters.asStateFlow()

    fun listener(event: BookDetailEvent){
        when(event){
            is BookDetailEvent.SearchCharactersByBook -> {
                searchCharactersByBook(event.id)
            }

            is BookDetailEvent.AddCharacter -> {
                addCharacter(event.character)
            }

            is BookDetailEvent.DeleteCharacter -> {
                deleteCharacter(event.id)
            }
        }
    }

    private fun searchCharactersByBook(id: Int){
        viewModelScope.launch {
            characterDao.getCharactersByBookId(id).collectLatest { characters ->
                _characters.update { characters }
            }
        }
    }

    private fun addCharacter(character: CharacterEntity){
        viewModelScope.launch {
            characterDao.upsert(character)
        }
    }

    private fun deleteCharacter(id: Int){
        viewModelScope.launch {
            characterDao.delete(id)
        }
    }
}