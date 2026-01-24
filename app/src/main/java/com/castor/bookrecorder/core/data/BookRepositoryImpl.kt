package com.castor.bookrecorder.core.data

import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.remote.service.book.BookService
import com.castor.bookrecorder.core.data.remote.service.user.UserService
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.repository.BookRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toBook
import com.castor.bookrecorder.core.domain.repository.mappers.toBookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val userRemoteService: UserService,
    private val bookService: BookService,
    private val bookDao: BookDao
): BookRepository {
    override suspend fun insertBook(book: Book) {
        try {
            // Convert book to book entity
            val bookEntity = book.toBookEntity()
            // Check if book is new or not
            if(book.id.isEmpty()){
                val snapshot = bookService.addBook(book) // Add book to firebase
                bookEntity.id = snapshot.id // Set id of book entity to id of book in firebase
                bookDao.insertBook(bookEntity) // Insert book entity to local database
            }else{
                bookDao.insertBook(bookEntity) // Insert book entity to local database
                bookService.setBookByID(book.id, book) // Update book in firebase
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getBookById(id: String): Book {
        val bookEntity = bookDao.getBookById(id)
        return bookEntity.toBook()
    }

    override fun getAllBooks(): Flow<List<Book>> {
        val userId = userRemoteService.getCurrentUserId()
        if (userId != null) {
            return bookDao.getBooksByUserID(userId)
                .map {
                    list -> list.map { entity -> entity.toBook() }
                }
        }
        throw Exception("User not logged in")
    }

    override suspend fun deleteBookById(id: String) {
        try {
            bookDao.deleteBookById(id)
            bookService.removeBook(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun addToFavorite(bookID: String) {
        try {
            val bookEntity = bookDao.getBookById(bookID)
            bookDao.updateBookFavoriteStatus(bookID, !bookEntity.isFavorite)
            bookService.addToFavorite(bookID, !bookEntity.isFavorite)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        val favoriteBooks = bookDao.getFavoriteBooks()
            .map {
                    list -> list.map { entity -> entity.toBook() }
            }

        return favoriteBooks
    }
}