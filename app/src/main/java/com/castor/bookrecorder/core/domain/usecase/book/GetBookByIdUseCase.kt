package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.model.Book
import com.castor.bookrecorder.core.domain.repository.BookRemoteRepository
import com.castor.bookrecorder.core.domain.repository.BookRepository
import javax.inject.Inject

class GetBookByIdUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val bookRemoteRepository: BookRemoteRepository
) {
    suspend operator fun invoke(id: String): Book? {
        return bookRepository.getBookById(id);
        //return bookRemoteRepository.getBookByID(id)
    }

}