package com.castor.bookrecorder.core.domain.usecase.book

import com.castor.bookrecorder.core.domain.repository.BookRepository
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookID: String) = bookRepository.addToFavorite(bookID)
}