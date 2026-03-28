package com.castor.bookrecorder.core.data.factory

import com.castor.bookrecorder.core.domain.model.MemoryValue

/**
 * Factory Method Pattern — Creator
 *
 * Declares the factory method [create] that returns the appropriate [MemoryValueInserter]
 * based on the runtime type of [MemoryValue]. Concrete creators override this method
 * to instantiate the correct product without exposing the creation logic to callers.
 */
interface MemoryValueInserterFactory {
    fun create(memoryValue: MemoryValue): MemoryValueInserter
}