package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.repository.BookRepository
import com.castor.bookrecorder.core.domain.repository.BookRemoteRepository
import javax.inject.Inject

class InsertBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val bookRemoteRepository: BookRemoteRepository
){
    suspend operator fun invoke(book: Book){
        // Insert the book into the local database
        bookRepository.insertBook(book)
        // Insert the book into the remote database
        //bookRemoteRepository.addBook(book)
    }
}