package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.repository.BookRemoteRepository
import javax.inject.Inject

class DeleteRemoteBookByIdUseCase @Inject constructor(
    private val bookRemoteRepository: BookRemoteRepository
) {
    suspend operator fun invoke(bookID: String) {
        bookRemoteRepository.removeBook(bookID)
    }
}