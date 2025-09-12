package com.castor.bookrecorder.core.domain.usecase.sync

import com.castor.bookrecorder.core.domain.repository.SyncRepository
import javax.inject.Inject

class SyncDataUseCase @Inject constructor(
    private val syncRepository: SyncRepository
) {
    suspend operator fun invoke() = syncRepository.syncData()
}