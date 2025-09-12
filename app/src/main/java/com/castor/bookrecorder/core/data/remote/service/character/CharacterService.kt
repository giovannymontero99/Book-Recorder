package com.castor.bookrecorder.core.data.remote.service.character

import com.castor.bookrecorder.core.domain.model.Character

interface CharacterService {

    suspend fun insertCharacter(character: Character): Void
    suspend fun deleteCharacter(idCharacter: Int, idBook: String)
    suspend fun updateCharacter(character: Character, idBook: String)
}