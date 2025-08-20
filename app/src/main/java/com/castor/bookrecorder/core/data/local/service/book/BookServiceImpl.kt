package com.castor.bookrecorder.core.data.local.service.book

import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow
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

    override fun getAllBooks(): Flow<List<BookEntity>> {
        return bookDao.getAllBooks()
    }

    override suspend fun deleteBookById(id: Int) {
        bookDao.deleteBookById(id)
    }

}