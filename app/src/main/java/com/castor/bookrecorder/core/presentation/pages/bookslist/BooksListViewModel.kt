package com.castor.bookrecorder.core.presentation.pages.bookslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.usecase.book.AddToFavoriteUseCase
import com.castor.bookrecorder.core.domain.usecase.book.DeleteBookByIdUseCase
import com.castor.bookrecorder.core.domain.usecase.book.GetAllBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface BooksListEvent{
    data class OnDeleteBook(val id: String): BooksListEvent
    data class OnAddToFavorite(val id: String): BooksListEvent
}

@HiltViewModel
class BooksListViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val deleteBookByIdUseCase: DeleteBookByIdUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase,
): ViewModel() {
    private val _booksList: MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())
    val booksList: StateFlow<List<Book>> = _booksList.asStateFlow()


    init {
        viewModelScope.launch {
            getAllBooksUseCase().collectLatest { list ->
                _booksList.update { list }
            }
        }
    }


    fun onEvent(event: BooksListEvent){
        when(event){
            is BooksListEvent.OnDeleteBook -> {
                viewModelScope.launch {
                    try {
                        deleteBookByIdUseCase(event.id)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }

            is BooksListEvent.OnAddToFavorite -> {
                addBookToFavorite(event.id)
            }
        }
    }


    private fun addBookToFavorite(id: String){
        viewModelScope.launch {
            try {
                addToFavoriteUseCase(id)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }


}