package com.castor.bookrecorder.core.domain.repository

import com.castor.bookrecorder.core.domain.model.Book

interface BookRepository {
    suspend fun insertBook(book: Book)
    suspend fun getBookById(id: Int): Book

}