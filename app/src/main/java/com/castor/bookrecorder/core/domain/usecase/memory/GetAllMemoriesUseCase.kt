package com.castor.bookrecorder.core.domain.usecase.memory

import com.castor.bookrecorder.core.domain.model.Memory
import com.castor.bookrecorder.core.domain.repository.MemoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMemoriesUseCase @Inject constructor(
    private val memoryRepository: MemoryRepository
) {
    operator fun invoke(): Flow<List<Memory>> = memoryRepository.getAll()
}