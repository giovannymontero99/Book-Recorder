package com.castor.bookrecorder.core.data.remote.service.character

import com.castor.bookrecorder.core.domain.model.Character

interface CharacterService {

    suspend fun insertCharacter(character: Character): Void
}