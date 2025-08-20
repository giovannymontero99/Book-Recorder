package com.castor.bookrecorder.core.presentation.pages.add_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.usecase.book.GetBookByIdUseCase
import com.castor.bookrecorder.core.domain.usecase.book.InsertBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AddBookUiState {
    data object SaveBook: AddBookUiState
    data class OnSaveNameChange(val name: String): AddBookUiState
    data class OnSaveAuthorChange(val author: String): AddBookUiState
    data class OnSaveGenreChange(val genre: String): AddBookUiState
    data class OnSaveProgressChange(val progress: String): AddBookUiState
    data class OnSaveTotalPagesChange(val totalPages: String?): AddBookUiState
    data class OnSaveNotesChange(val notes: String): AddBookUiState
    data class OnSaveSummaryChange(val summary: String): AddBookUiState
    data class OnSaveQuotesChange(val quotes: String): AddBookUiState
    data class OnSaveIsFinishedChange(val isFinished: Boolean): AddBookUiState
}

data class BookState(
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val genre: String? = null,
    val progress: String = "",
    val totalPages: String = "",
    val notes: String? = null,
    val summary: String? = null,
    val quotes: String? = null,
    val isFinished: Boolean = false
)

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val insertBookUseCase: InsertBookUseCase,
    private val getBookByIdUseCase: GetBookByIdUseCase
): ViewModel() {

    private val _state = MutableStateFlow(BookState())
    val state = _state.asStateFlow()

    /**
     * Handle the events from the UI
     * */
    fun handleEvent(event: AddBookUiState){
        when(event){
            is AddBookUiState.SaveBook -> {
                try {
                    val book = Book(
                        id = state.value.id,
                        title = state.value.title,
                        author = state.value.author,
                        genre = state.value.genre,
                        progress = if (state.value.progress.isEmpty()) 0 else state.value.progress.toInt(),
                        totalPages = if (state.value.totalPages.isEmpty()) 0 else state.value.totalPages.toInt(),
                        notes = state.value.notes,
                        summary = state.value.summary,
                        quotes = state.value.quotes,
                        isFinished = state.value.isFinished,
                        startDate = null,
                        finishDate = null,
                        coverImageUri = null
                    )
                    this.saveBook(book)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
            is AddBookUiState.OnSaveNameChange -> {
                _state.update { it.copy(title = event.name) }
            }
            is AddBookUiState.OnSaveAuthorChange -> {
                _state.update { it.copy(author = event.author) }
            }
            is AddBookUiState.OnSaveGenreChange -> {
                _state.update { it.copy(genre = event.genre) }
            }
            is AddBookUiState.OnSaveIsFinishedChange -> {
                _state.update { it.copy(isFinished = !event.isFinished) }
            }
            is AddBookUiState.OnSaveNotesChange -> {
                _state.update { it.copy(notes = event.notes) }
            }
            is AddBookUiState.OnSaveProgressChange -> {
                _state.update { it.copy(progress = event.progress) }
            }
            is AddBookUiState.OnSaveQuotesChange -> {
                _state.update { it.copy(quotes = event.quotes) }
            }
            is AddBookUiState.OnSaveSummaryChange -> {
                _state.update { it.copy(summary = event.summary) }
            }
            is AddBookUiState.OnSaveTotalPagesChange -> {
                _state.update { it.copy(totalPages = event.totalPages?: "0") }
            }
        }
    }

    /**
     * Get the book by id from the database
     * */
    fun getBookById(id: Int){
        viewModelScope.launch {
            val book = getBookByIdUseCase(id)
            _state.update {
                it.copy(
                    id = book.id,
                    title = book.title,
                    author = book.author,
                    genre = book.genre,
                    progress = if(book.progress == 0) "" else book.progress.toString(),
                    totalPages = book.totalPages?.let { totalPages -> if(totalPages == 0) "" else totalPages.toString() } ?: "",
                    notes = book.notes,
                    summary = book.summary,
                    quotes = book.quotes,
                    isFinished = book.isFinished
                )
            }
        }
    }

    /**
     * Save the book to the database
     * */
    private fun saveBook(book: Book){
        viewModelScope.launch {
            insertBookUseCase(book)
        }
    }

}