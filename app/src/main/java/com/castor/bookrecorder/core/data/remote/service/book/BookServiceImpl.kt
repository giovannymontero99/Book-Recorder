package com.castor.bookrecorder.core.data.remote.service.book

import com.castor.bookrecorder.core.domain.model.Book
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
): BookService {

    override suspend fun addBook(book: Book): DocumentReference {

        val bookMap = hashMapOf(
            "title" to book.title,
            "author" to book.author,
            "genre" to book.genre,
            "progress" to book.progress,
            "totalPages" to book.totalPages,
            "notes" to book.notes,
            "summary" to book.summary,
            "quotes" to book.quotes,
            "isFinished" to book.isFinished,
            "startDate" to book.startDate,
            "finishDate" to book.finishDate,
            "coverImageUri" to book.coverImageUri,
            "userID" to book.userID
        )

        return firebaseFirestore
            .collection("books")
            .add(bookMap)
            .await()
    }

    override suspend fun getBooksByUserID(userID: String): QuerySnapshot {
        return firebaseFirestore
            .collection("books")
            .whereEqualTo("userID", userID)
            .get()
            .await()
    }

    override fun getBookByID(bookID: String): Task<DocumentSnapshot> {
        return firebaseFirestore
            .collection("books")
            .document(bookID)
            .get()
    }

    override suspend fun setBookByID(bookID: String, book: Book): Void {

        val bookMap = hashMapOf(
            "title" to book.title,
            "author" to book.author,
            "genre" to book.genre,
            "progress" to book.progress,
            "totalPages" to book.totalPages,
            "notes" to book.notes,
            "summary" to book.summary,
            "quotes" to book.quotes,
            "isFinished" to book.isFinished,
            "startDate" to book.startDate,
            "finishDate" to book.finishDate,
            "coverImageUri" to book.coverImageUri,
            "userID" to book.userID
        )

        return firebaseFirestore
            .collection("books")
            .document(bookID)
            .set(bookMap, SetOptions.merge())
            .await()
    }

    override fun removeBook(bookID: String): Task<Void> {
        return firebaseFirestore
            .collection("books")
            .document(bookID)
            .delete()
    }

    override suspend fun addToFavorite(bookID: String, isFavorite: Boolean): Void {
        return firebaseFirestore
            .collection("books")
            .document(bookID)
            .set(hashMapOf("isFavorite" to isFavorite), SetOptions.merge())
            .await()
    }
}