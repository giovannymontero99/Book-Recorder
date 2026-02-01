package com.castor.bookrecorder.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.castor.bookrecorder.core.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Upsert
    suspend fun insertBook(book: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBookById(id: String)

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity

    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()

    @Query("SELECT * FROM books WHERE userID = :userID")
    fun getBooksByUserID(userID: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id IN (:ids)")
    fun getBooksByIds(ids: List<String>): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("DELETE FROM books WHERE id NOT IN (:ids)")
    suspend fun deleteBooksNotInIds(ids: List<String>)

    @Query("UPDATE books SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateBookFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM books WHERE isFavorite = 1 AND userID = :userID")
    fun getFavoriteBooksByUserId(userID: String): Flow<List<BookEntity>>

    @Transaction
    suspend fun overwriteBooks(bookEntityList: List<BookEntity> ){
        val existingBooks: List<String> = bookEntityList.map { it.id } // Get existing books by their IDs
        deleteBooksNotInIds(existingBooks) // Delete books that are not in the new list
        insertBooks(bookEntityList)
    }

}