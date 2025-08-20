package com.castor.bookrecorder.core.domain.repository

import com.castor.bookrecorder.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacterByBookId(bookId: Int): Flow<List<Character>>

    suspend fun upsertCharacter(character: Character)

    suspend fun deleteCharacterById(id: Int)
}