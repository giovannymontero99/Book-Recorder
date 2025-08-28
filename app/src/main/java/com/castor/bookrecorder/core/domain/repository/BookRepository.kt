package com.castor.bookrecorder.core.domain.repository

import com.castor.bookrecorder.core.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun insertBook(book: Book)
    suspend fun getBookById(id: String): Book

    fun getAllBooks(): Flow<List<Book>>

    suspend fun deleteBookById(id: Int)

    suspend fun syncBooks()

}