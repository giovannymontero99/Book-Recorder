package com.castor.bookrecorder.core.domain.usecase.user

import com.castor.bookrecorder.core.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val userRemoteRepository: UserRepository
) {
    operator fun invoke(): String? = userRemoteRepository.getCurrentUserId()
}