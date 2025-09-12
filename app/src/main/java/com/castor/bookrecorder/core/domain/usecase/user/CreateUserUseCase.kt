package com.castor.bookrecorder.core.domain.usecase.user

import com.castor.bookrecorder.core.domain.model.User
import com.castor.bookrecorder.core.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRemoteRepository: UserRepository
) {
    operator fun invoke(user: User) = userRemoteRepository.addUser(user)
}