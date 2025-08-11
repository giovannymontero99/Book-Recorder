package com.castor.bookrecorder.core.data.local.service

import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.local.entity.BookEntity
import javax.inject.Inject

class BookServiceImpl @Inject constructor(
    private val bookDao: BookDao
) : BookService {

    override suspend fun insertBook(bookEntity: BookEntity) {
        bookDao.insertBook(bookEntity)
    }

    override suspend fun getBookById(id: Int): BookEntity {
        return bookDao.getBookById(id)
    }
}