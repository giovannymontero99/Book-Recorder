package com.castor.bookrecorder.core.data.local.service

import com.castor.bookrecorder.core.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

interface BookService {
    suspend fun insertBook(bookEntity: BookEntity)
    suspend fun getBookById(id: Int): BookEntity
    fun getAllBooks(): Flow<List<BookEntity>>

    suspend fun deleteBookById(id: Int)

}