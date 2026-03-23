package com.castor.bookrecorder.core.data.factory

import com.castor.bookrecorder.core.domain.model.MemoryValue

/**
 * Factory Method Pattern — Product
 *
 * Defines the contract that every memory inserter must fulfill.
 * Each concrete implementation knows how to persist a specific type of MemoryValue.
 */
interface MemoryValueInserter {
    suspend fun insert(memoryId: String, memoryValue: MemoryValue)
}