package com.castor.bookrecorder.core.domain.usecase.user

import com.castor.bookrecorder.core.domain.repository.UserRemoteRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) {
    operator fun invoke(): String? = userRemoteRepository.getCurrentUserId()
}