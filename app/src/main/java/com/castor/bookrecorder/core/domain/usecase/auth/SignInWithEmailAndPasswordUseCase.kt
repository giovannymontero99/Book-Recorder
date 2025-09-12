package com.castor.bookrecorder.core.domain.usecase.auth

import com.castor.bookrecorder.core.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = authRepository.signInWithEmailAndPassword(email, password)
}