package com.castor.bookrecorder.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.castor.bookrecorder.core.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters WHERE bookId = :bookId")
    fun getCharactersByBookId(bookId: String): Flow<List<CharacterEntity>>

    @Upsert
    suspend fun upsert(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun delete(id: Int)

    @Insert
    suspend fun insert(character: CharacterEntity): Long

    @Query("DELETE FROM characters WHERE bookId NOT IN (:ids)")
    suspend fun deleteCharactersNotInIds(ids: List<Int>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Transaction
    suspend fun cleanAndUpsertCharacter(list: List<CharacterEntity>) {
        val ids = list.map { it.id }
        deleteCharactersNotInIds(ids)
        insertAll(list)
    }

}