package com.castor.bookrecorder.core.data

import com.castor.bookrecorder.core.data.local.service.character.CharacterService
import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.domain.repository.CharacterRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toEntity
import com.castor.bookrecorder.core.domain.repository.mappers.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterService: CharacterService
): CharacterRepository {
    override fun getCharacterByBookId(bookId: Int): Flow<List<Character>> {
        val characterEntities = characterService.getCharactersByBookId(bookId)
        return characterEntities.map { list -> list.map { entity -> entity.toModel() } }
    }

    override suspend fun upsertCharacter(character: Character) {
        characterService.upsertCharacter(character.toEntity())
    }

    override suspend fun deleteCharacterById(id: Int) {
        characterService.deleteCharacterById(id)
    }

}