package com.castor.bookrecorder.core.data

import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.remote.dto.BookDto
import com.castor.bookrecorder.core.data.remote.service.user.UserService
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.domain.repository.BookRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toBook
import com.castor.bookrecorder.core.domain.repository.mappers.toBookEntity
import com.castor.bookrecorder.core.domain.repository.mappers.toCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val userRemoteService: UserService,
    private val bookService: com.castor.bookrecorder.core.data.remote.service.book.BookService,
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

    override suspend fun syncBooks() {
        val userId = userRemoteService.getCurrentUserId()
        if (userId != null) {
            try {
                val documents = bookService.getBooksByUserID(userId)
                val listCharacter = mutableListOf<Character>()
                for (document in documents) {
                    val bookDto = document.toObject(BookDto::class.java)
                    bookDto.id = document.id
                    bookDto.isFinished = document.getBoolean("isFinished") ?: false
                    insertBook(bookDto.toBook())
                    for (character in bookDto.characters) {
                        listCharacter.add(character.toCharacter())
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}