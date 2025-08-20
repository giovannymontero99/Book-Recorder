package com.castor.bookrecorder.core.domain.usecase.character

import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.domain.repository.CharacterRepository
import javax.inject.Inject


class UpsertCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(character: Character) {
        characterRepository.upsertCharacter(character)
    }
}