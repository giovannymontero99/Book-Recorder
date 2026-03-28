package com.castor.bookrecorder.core.domain.repository

import com.castor.bookrecorder.core.domain.model.Memory
import kotlinx.coroutines.flow.Flow

interface MemoryRepository {
    suspend fun insertMemory(memory: Memory)
    fun getAll(): Flow<List<Memory>>
    suspend fun deleteMemory(id: String)
}