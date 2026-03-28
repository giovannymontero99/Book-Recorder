package com.castor.bookrecorder.core.data.factory

import com.castor.bookrecorder.core.data.local.dao.MemoryDao
import com.castor.bookrecorder.core.domain.model.MemoryIntValue
import com.castor.bookrecorder.core.domain.model.MemoryValue
import com.castor.bookrecorder.core.domain.repository.mappers.toEntity

/**
 * Factory Method Pattern — Concrete Product
 *
 * Handles persistence for [MemoryIntValue] by mapping it to a [MemoryIntEntity]
 * and inserting it into the memory_ints table via [MemoryDao].
 */
class MemoryIntInserter(
    private val memoryDao: MemoryDao
) : MemoryValueInserter {

    override suspend fun insert(memoryId: String, memoryValue: MemoryValue) {
        val value = memoryValue as MemoryIntValue
        memoryDao.insertMemoryInt(value.toEntity(memoryId))
    }
}