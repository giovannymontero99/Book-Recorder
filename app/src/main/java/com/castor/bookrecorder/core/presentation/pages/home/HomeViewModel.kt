package com.castor.bookrecorder.core.presentation.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.local.entity.BookEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface HomeEvent {
    data class DeleteBook(val id: Int): HomeEvent
}



@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookDao: BookDao
): ViewModel() {


    private val _books: MutableStateFlow<List<BookEntity>> = MutableStateFlow(emptyList())
    val books: StateFlow<List<BookEntity>> = _books.asStateFlow()


    init {
        viewModelScope.launch {
            bookDao.getAllBooks().collectLatest { books ->
                _books.update { books }
            }
        }
    }

    fun onClick(event: HomeEvent){
        when(event){
            is HomeEvent.DeleteBook -> {
                deleteBook(event.id)
            }
        }
    }

    private fun deleteBook(id: Int){
        viewModelScope.launch {
            bookDao.deleteBook(id)
        }

    }

    private fun editBook(id: Int){
        viewModelScope.launch {

        }
    }

}