package com.castor.bookrecorder.core.presentation.pages.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.usecase.book.DeleteBookByIdUseCase
import com.castor.bookrecorder.core.domain.usecase.book.GetAllBooksUseCase
import com.castor.bookrecorder.core.presentation.state.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface HomeEvent {
    data class DeleteBook(val id: String): HomeEvent
}



@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val deleteBookByIdUseCase: DeleteBookByIdUseCase,
): ViewModel() {


    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> = _navigationState.asStateFlow()

    private val _booksList: MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())
    val booksList: StateFlow<List<Book>> = _booksList.asStateFlow()


    init {
        viewModelScope.launch {
            getAllBooksUseCase().collectLatest { list ->
                _booksList.update { list }
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

    private fun removeBookById(id: String){
        viewModelScope.launch {
            try {
                deleteBookByIdUseCase(id)
            }catch (e: Exception){
                Log.d("HomeViewModel", "removeBookById: ${e.message}")
            }
        }
    }

}