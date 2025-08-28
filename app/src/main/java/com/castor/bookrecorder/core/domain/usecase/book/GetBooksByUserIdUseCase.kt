package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.repository.BookRemoteRepository
import javax.inject.Inject

class GetBooksByUserIdUseCase @Inject constructor(
    private val bookRemoteRepository: BookRemoteRepository
) {
    suspend operator fun invoke(userID: String) = bookRemoteRepository.getBooksByUserID(userID)
}