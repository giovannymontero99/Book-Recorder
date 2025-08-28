package com.castor.bookrecorder.core.domain.repository

import com.castor.bookrecorder.core.domain.model.Book
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface BookRemoteRepository {
    suspend fun addBook(book: Book)
    suspend fun getBooksByUserID(userID: String)
    suspend fun getBookByID(bookID: String): Book?

    suspend fun removeBook(bookID: String)
}