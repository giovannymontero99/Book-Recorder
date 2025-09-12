package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.repository.BookRemoteRepository
import com.castor.bookrecorder.core.domain.repository.BookRepository
import javax.inject.Inject

class DeleteRemoteBookByIdUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookID: String) {
        bookRepository.deleteBookById(bookID)
    }
}