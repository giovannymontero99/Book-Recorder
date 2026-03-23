package com.castor.bookrecorder.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Delete
import com.castor.bookrecorder.core.data.local.entity.MemoryBooleanEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryIntEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryStringEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryString(memoryString: MemoryStringEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryInt(memoryInt: MemoryIntEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemoryBoolean(memoryBoolean: MemoryBooleanEntity)

    @Transaction
    @Query("SELECT * FROM memories ORDER BY created DESC")
    fun getAllMemories(): Flow<List<MemoryWithRelations>>

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemoryById(id: String)
}