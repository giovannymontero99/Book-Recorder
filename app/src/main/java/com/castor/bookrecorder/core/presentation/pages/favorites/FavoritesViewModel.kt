package com.castor.bookrecorder.core.presentation.pages.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.usecase.book.GetFavoriteBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteBooksUseCase: GetFavoriteBooksUseCase
): ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books = _books.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteBooksUseCase().collectLatest { books ->
                _books.update { books }
            }
        }
    }
}