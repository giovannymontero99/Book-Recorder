package com.castor.bookrecorder.core.data.factory

import com.castor.bookrecorder.core.data.local.dao.MemoryDao
import com.castor.bookrecorder.core.domain.model.MemoryStringValue
import com.castor.bookrecorder.core.domain.model.MemoryValue
import com.castor.bookrecorder.core.domain.repository.mappers.toEntity

/**
 * Factory Method Pattern — Concrete Product
 *
 * Handles persistence for [MemoryStringValue] by mapping it to a [MemoryStringEntity]
 * and inserting it into the memory_strings table via [MemoryDao].
 */
class MemoryStringInserter(
    private val memoryDao: MemoryDao
) : MemoryValueInserter {

    override suspend fun insert(memoryId: String, memoryValue: MemoryValue) {
        val value = memoryValue as MemoryStringValue
        memoryDao.insertMemoryString(value.toEntity(memoryId))
    }
}