package com.castor.bookrecorder.core.data.factory

import com.castor.bookrecorder.core.data.local.dao.MemoryDao
import com.castor.bookrecorder.core.domain.model.MemoryBooleanValue
import com.castor.bookrecorder.core.domain.model.MemoryValue
import com.castor.bookrecorder.core.domain.repository.mappers.toEntity

/**
 * Factory Method Pattern — Concrete Product
 *
 * Handles persistence for [MemoryBooleanValue] by mapping it to a [MemoryBooleanEntity]
 * and inserting it into the memory_booleans table via [MemoryDao].
 */
class MemoryBooleanInserter(
    private val memoryDao: MemoryDao
) : MemoryValueInserter {

    override suspend fun insert(memoryId: String, memoryValue: MemoryValue) {
        val value = memoryValue as MemoryBooleanValue
        memoryDao.insertMemoryBoolean(value.toEntity(memoryId))
    }
}