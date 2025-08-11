package com.castor.bookrecorder.core.data.local.repository

import com.castor.bookrecorder.core.data.local.service.BookService
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.repository.BookRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toBook
import com.castor.bookrecorder.core.domain.repository.mappers.toBookEntity
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookService: BookService
): BookRepository {
    override suspend fun insertBook(book: Book) {
        val bookEntity = book.toBookEntity()
        bookService.insertBook(bookEntity)
    }

    override suspend fun getBookById(id: Int): Book {
        val bookEntity = bookService.getBookById(id)
        return bookEntity.toBook()
    }

}