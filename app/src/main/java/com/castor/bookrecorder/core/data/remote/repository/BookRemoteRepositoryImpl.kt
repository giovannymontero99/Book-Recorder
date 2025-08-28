package com.castor.bookrecorder.core.data.remote.repository

import android.util.Log
import com.castor.bookrecorder.core.data.remote.dto.BookDto
import com.castor.bookrecorder.core.data.remote.service.book.BookService
import com.castor.bookrecorder.core.data.remote.service.user.UserRemoteService
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.domain.repository.BookRemoteRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toBook
import com.castor.bookrecorder.core.domain.repository.mappers.toCharacter
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRemoteRepositoryImpl @Inject constructor(
    private val userRemoteService: UserRemoteService,
    private val bookRemoteService: BookService,
): BookRemoteRepository {
    override suspend fun addBook(book: Book) {
        /*

        val userId = userRemoteService.getCurrentUserId()

        if(book.id.isEmpty()){
            val bookMap = hashMapOf(
                "userId" to userId,
                "title" to book.title,
                "author" to book.author,
                "genre" to book.genre,
                "progress" to book.progress,
                "totalPages" to book.totalPages,
                "notes" to book.notes,
                "summary" to book.summary,
                "quotes" to book.quotes,
                "isFinished" to book.isFinished
            )
            bookRemoteService.addBook(bookMap)
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    Log.d("BookRemoteRepository", "Error adding book: $exception")
                }
        }else{

            val updates = hashMapOf(
                "userId" to userId,
                "title" to book.title,
                "author" to book.author,
                "genre" to book.genre,
                "progress" to book.progress,
                "totalPages" to book.totalPages,
                "notes" to book.notes,
                "summary" to book.summary,
                "quotes" to book.quotes,
                "isFinished" to book.isFinished
            )

            bookRemoteService.setBookByID(book.id, updates as Map<String, Any>)
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    Log.d("BookRemoteRepository", "Error adding book: $exception")
                }
        }

         */
    }

    override suspend fun getBooksByUserID(userID: String) {

        try {
            val documents = bookRemoteService.getBooksByUserID(userID)
            val listBook = mutableListOf<Book>()
            val listCharacter = mutableListOf<Character>()

            for (document in documents) {
                val bookDto = document.toObject(BookDto::class.java)
                bookDto.id = document.id
                bookDto.isFinished = document.getBoolean("isFinished") ?: false
                listBook.add(bookDto.toBook())
                for (character in bookDto.characters) {
                    listCharacter.add(character.toCharacter())
                }
            }
        }catch (e: Exception){
            Log.d("BookRemoteRepository", "getBooksByUserID: ${e.message}")
            throw e
        }
    }

    override suspend fun getBookByID(bookID: String): Book? {
        val snapshot = bookRemoteService.getBookByID(bookID).await()
        val book = snapshot.toObject(Book::class.java)
        book?.id = snapshot.id
        book?.isFinished = snapshot.getBoolean("isFinished") ?: false
        return book
    }

    override suspend fun removeBook(bookID: String) {
        bookRemoteService.removeBook(bookID)
    }
}