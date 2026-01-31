package com.castor.bookrecorder.core.data.remote.service.book

import com.castor.bookrecorder.core.domain.model.Book
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface BookService {
    suspend fun addBook(book: Book): DocumentReference

    suspend fun getBooksByUserID(userID: String): QuerySnapshot

    fun getBookByID(bookID: String): Task<DocumentSnapshot>
    suspend fun setBookByID(bookID: String, book: Book): Void

    fun removeBook(bookID: String) : Task<Void>

    suspend fun addToFavorite(bookID: String, isFavorite: Boolean) : Void

}