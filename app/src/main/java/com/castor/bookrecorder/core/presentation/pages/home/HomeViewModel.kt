package com.castor.bookrecorder.core.presentation.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.model.Book
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


sealed interface HomeEvent {
    data class DeleteBook(val id: Int): HomeEvent
}



@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val deleteBookByIdUseCase: DeleteBookByIdUseCase
): ViewModel() {


    private val _booksList: MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())
    val booksList: StateFlow<List<Book>> = _booksList.asStateFlow()


    init {
        getAllBooks()
    }

    private fun getAllBooks(){
        viewModelScope.launch {
            getAllBooksUseCase().collectLatest { books ->
                _booksList.update { books }
            }
        }
    }

    fun onClick(event: HomeEvent){
        when(event){
            is HomeEvent.DeleteBook -> {
                removeBookById(event.id)
            }
        }
    }

    private fun removeBookById(id: Int){
        viewModelScope.launch {
            deleteBookByIdUseCase(id)
        }
    }

}