package com.castor.bookrecorder.core.presentation.pages.bookslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.usecase.book.GetAllBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BooksListViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
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
}