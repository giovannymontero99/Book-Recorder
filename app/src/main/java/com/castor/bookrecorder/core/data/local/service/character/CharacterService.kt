package com.castor.bookrecorder.core.data.local.service.character

import com.castor.bookrecorder.core.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface CharacterService {
    fun getCharactersByBookId(bookId: Int): Flow<List<CharacterEntity>>

    suspend fun upsertCharacter(characterEntity: CharacterEntity)

    suspend fun deleteCharacterById(characterId: Int)
}