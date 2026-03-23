package com.castor.bookrecorder.core.data.factory

import com.castor.bookrecorder.core.data.local.dao.MemoryDao
import com.castor.bookrecorder.core.domain.model.MemoryBooleanValue
import com.castor.bookrecorder.core.domain.model.MemoryIntValue
import com.castor.bookrecorder.core.domain.model.MemoryStringValue
import com.castor.bookrecorder.core.domain.model.MemoryValue
import javax.inject.Inject

/**
 * Factory Method Pattern — Concrete Creator
 *
 * Implements the factory method by inspecting the type of [MemoryValue] at runtime
 * and returning the matching [MemoryValueInserter]:
 *   - [MemoryStringValue]  → [MemoryStringInserter]  (persists to memory_strings table)
 *   - [MemoryIntValue]     → [MemoryIntInserter]     (persists to memory_ints table)
 *   - [MemoryBooleanValue] → [MemoryBooleanInserter] (persists to memory_booleans table)
 *
 * Adding a new MemoryValue type only requires adding a new branch here and
 * a new Concrete Product — no other class needs to change.
 */
class MemoryValueInserterFactoryImpl @Inject constructor(
    private val memoryDao: MemoryDao
) : MemoryValueInserterFactory {

    override fun create(memoryValue: MemoryValue): MemoryValueInserter = when (memoryValue) {
        is MemoryStringValue -> MemoryStringInserter(memoryDao)
        is MemoryIntValue -> MemoryIntInserter(memoryDao)
        is MemoryBooleanValue -> MemoryBooleanInserter(memoryDao)
    }
}