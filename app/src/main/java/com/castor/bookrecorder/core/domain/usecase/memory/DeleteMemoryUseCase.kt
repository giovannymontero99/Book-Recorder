package com.castor.bookrecorder.core.domain.usecase.memory

import com.castor.bookrecorder.core.domain.repository.MemoryRepository
import javax.inject.Inject

class DeleteMemoryUseCase @Inject constructor(
    private val memoryRepository: MemoryRepository
) {
    suspend operator fun invoke(id: String) {
        memoryRepository.deleteMemory(id)
    }
}