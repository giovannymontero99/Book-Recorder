package com.castor.bookrecorder.core.domain.usecase.character

import com.castor.bookrecorder.core.domain.repository.CharacterRepository
import javax.inject.Inject

class DeleteCharacterByIdUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(idCharacter: Int, idBook: String) {
        characterRepository.deleteCharacterById(idCharacter, idBook)
    }
}