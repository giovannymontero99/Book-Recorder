package com.castor.bookrecorder.core.domain.usecase.memory

import com.castor.bookrecorder.core.domain.model.Memory
import com.castor.bookrecorder.core.domain.repository.MemoryRepository
import javax.inject.Inject

class InsertMemoryUseCase @Inject constructor(
    private val memoryRepository: MemoryRepository
) {
    suspend operator fun invoke(memory: Memory) {
        memoryRepository.insertMemory(memory)
    }
}
