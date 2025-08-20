package com.castor.bookrecorder.core.data.local.service.character

import com.castor.bookrecorder.core.data.local.dao.CharacterDao
import com.castor.bookrecorder.core.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterServiceImpl @Inject constructor(
    private val characterDao: CharacterDao
): CharacterService{
    override fun getCharactersByBookId(bookId: Int): Flow<List<CharacterEntity>> {
        return characterDao.getCharactersByBookId(bookId)
    }

    override suspend fun upsertCharacter(characterEntity: CharacterEntity){
        characterDao.upsert(characterEntity)
    }

    override suspend fun deleteCharacterById(characterId: Int) {
        characterDao.delete(characterId)
    }

}