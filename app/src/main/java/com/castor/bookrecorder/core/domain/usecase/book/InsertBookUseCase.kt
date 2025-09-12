package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.repository.BookRepository
import javax.inject.Inject

class InsertBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
){
    suspend operator fun invoke(book: Book){
        // Insert the book into the local database
        bookRepository.insertBook(book)
    }
}