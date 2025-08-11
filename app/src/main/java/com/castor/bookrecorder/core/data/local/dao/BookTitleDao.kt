package com.castor.bookrecorder.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.castor.bookrecorder.core.data.local.entity.BookTitleEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BookTitleDao {

    @Query("SELECT * FROM BookTitleEntity")
    fun allBookTitle(): Flow<List<BookTitleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookTitle(bookTitleEntity: BookTitleEntity)

    @Query("DELETE FROM BookTitleEntity WHERE idBookTitle = :id")
    suspend fun deleteBookTitle(id: Int)


}