package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.repository.BookRepository
import javax.inject.Inject

class GetFavoriteBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    operator fun invoke() = bookRepository.getFavoriteBooks()
}