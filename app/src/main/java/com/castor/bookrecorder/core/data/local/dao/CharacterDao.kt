package com.castor.bookrecorder.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.castor.bookrecorder.core.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters WHERE bookId = :bookId")
    fun getCharactersByBookId(bookId: Int): Flow<List<CharacterEntity>>

    @Upsert
    suspend fun upsert(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun delete(id: Int)
}