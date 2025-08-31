package com.castor.bookrecorder.core.domain.repository

import com.castor.bookrecorder.core.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacterByBookId(bookId: String): Flow<List<Character>>

    suspend fun upsertCharacter(character: Character, bookId: String)

    suspend fun deleteCharacterById(idCharacter: Int, bookId: String)
}