package com.castor.bookrecorder.core.data.local.repository

import com.castor.bookrecorder.core.data.local.service.BookService
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.repository.BookRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toBook
import com.castor.bookrecorder.core.domain.repository.mappers.toBookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getAllBooks(): Flow<List<Book>> {
        val bookEntities = bookService.getAllBooks()
        return bookEntities.map { list -> list.map { entity -> entity.toBook() } }
    }

    override suspend fun deleteBookById(id: Int) {
        bookService.deleteBookById(id)
    }

}