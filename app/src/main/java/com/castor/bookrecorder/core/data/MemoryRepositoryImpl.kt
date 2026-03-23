package com.castor.bookrecorder.core.data

import com.castor.bookrecorder.core.data.factory.MemoryValueInserterFactory
import com.castor.bookrecorder.core.data.local.dao.MemoryDao
import com.castor.bookrecorder.core.domain.model.Memory
import com.castor.bookrecorder.core.domain.repository.MemoryRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toDomain
import com.castor.bookrecorder.core.domain.repository.mappers.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MemoryRepositoryImpl @Inject constructor(
    private val memoryDao: MemoryDao,
    private val inserterFactory: MemoryValueInserterFactory
) : MemoryRepository {

    override suspend fun insertMemory(memory: Memory) {
        memoryDao.insertMemory(memory.toEntity())
        inserterFactory.create(memory.value).insert(memory.id, memory.value)
    }

    override fun getAll(): Flow<List<Memory>> {
        return memoryDao.getAllMemories().map { list ->
            list.mapNotNull { it.toDomain() }
        }
    }

    override suspend fun deleteMemory(id: String) {
        memoryDao.deleteMemoryById(id)
    }
}