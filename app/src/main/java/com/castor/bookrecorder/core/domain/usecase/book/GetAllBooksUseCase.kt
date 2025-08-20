package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.repository.BookRepository
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(
    private val booksRepository: BookRepository
) {
    operator fun invoke() = booksRepository.getAllBooks()
}