package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.repository.BookRepository
import javax.inject.Inject

class DeleteBookByIdUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(id: Int) {
        bookRepository.deleteBookById(id)
    }

}