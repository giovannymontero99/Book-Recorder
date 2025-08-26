package com.castor.bookrecorder.core.data.remote.service.book

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface BookRemoteService {
    fun<K,V> addBook(book: HashMap<K,V>): Task<DocumentReference>

    fun getBooksByUserID(userID: String): Task<QuerySnapshot>

    fun getBookByID(bookID: String): Task<DocumentSnapshot>
    fun setBookByID(bookID: String, book: Map<String, Any>): Task<Void>

    fun removeBook(bookID: String) : Task<Void>

}