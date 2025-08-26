package com.castor.bookrecorder.core.data.remote.service.book

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class BookRemoteServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
): BookRemoteService {
    override fun <K, V> addBook(book: HashMap<K, V>): Task<DocumentReference> {
        return firebaseFirestore
            .collection("books")
            .add(book)
    }

    override fun getBooksByUserID(userID: String): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("books")
            .whereEqualTo("userId", userID)
            .get()
    }

    override fun getBookByID(bookID: String): Task<DocumentSnapshot> {
        return firebaseFirestore
            .collection("books")
            .document(bookID)
            .get()
    }

    override fun setBookByID(bookID: String, book: Map<String, Any>): Task<Void> {
        return firebaseFirestore
            .collection("books")
            .document(bookID)
            .set(book, SetOptions.merge())
    }

    override fun removeBook(bookID: String): Task<Void> {
        return firebaseFirestore
            .collection("books")
            .document(bookID)
            .delete()
    }
}