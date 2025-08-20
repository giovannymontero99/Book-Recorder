package com.castor.bookrecorder.core.domain.usecase.character

import com.castor.bookrecorder.core.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersByBookIdUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(bookId: Int) = characterRepository.getCharacterByBookId(bookId)

}