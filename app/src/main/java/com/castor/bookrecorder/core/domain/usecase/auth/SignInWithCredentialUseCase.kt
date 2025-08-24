package com.castor.bookrecorder.core.domain.usecase.auth

import com.castor.bookrecorder.core.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = authRepository.signInWithCredential(idToken)
}