package com.castor.bookrecorder.core.data.remote.repository

import android.util.Log
import com.castor.bookrecorder.core.data.remote.service.book.BookRemoteService
import com.castor.bookrecorder.core.data.remote.service.user.UserRemoteService
import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.repository.BookRemoteRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRemoteRepositoryImpl @Inject constructor(
    private val userRemoteService: UserRemoteService,
    private val bookRemoteService: BookRemoteService,
): BookRemoteRepository {
    override suspend fun addBook(book: Book) {

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
    }

    override fun getBooksByUserID(userID: String): Task<QuerySnapshot> {
        return bookRemoteService.getBooksByUserID(userID)
    }

    override suspend fun getBookByID(bookID: String): Book? {
        val snapshot = bookRemoteService.getBookByID(bookID).await()
        val book = snapshot.toObject(Book::class.java)
        book?.id = snapshot.id
        book?.isFinished = snapshot.getBoolean("isFinished")
        return book
    }

    override suspend fun removeBook(bookID: String) {
        bookRemoteService.removeBook(bookID)
    }
}