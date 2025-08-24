package com.castor.bookrecorder.core.domain.usecase.auth

import com.castor.bookrecorder.core.domain.repository.AuthRepository
import javax.inject.Inject

class CheckUserLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}