package com.castor.bookrecorder.core.data.local.service

import com.castor.bookrecorder.core.data.local.entity.BookEntity

interface BookService {
    suspend fun insertBook(bookEntity: BookEntity)
    suspend fun getBookById(id: Int): BookEntity
}