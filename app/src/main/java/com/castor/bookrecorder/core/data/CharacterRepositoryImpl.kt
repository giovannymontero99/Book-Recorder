package com.castor.bookrecorder.core.data

import com.castor.bookrecorder.core.data.local.dao.CharacterDao
import com.castor.bookrecorder.core.data.remote.service.character.CharacterService
import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.domain.repository.CharacterRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toEntity
import com.castor.bookrecorder.core.domain.repository.mappers.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao,
    private val characterService: CharacterService
): CharacterRepository {
    override fun getCharacterByBookId(bookId: String): Flow<List<Character>> {
        val characterEntities = characterDao.getCharactersByBookId(bookId)
        return characterEntities.map { list -> list.map { entity -> entity.toModel() } }
    }

    override suspend fun upsertCharacter(character: Character) {
        if (character.id != 0) {
            characterDao.upsert(character.toEntity())
        } else {

            try {
                val newCharacter = characterDao.insert(character.toEntity())

                val characterForService = Character(
                    id = newCharacter.toInt(),
                    name = character.name,
                    bookId = character.bookId,
                    description = character.description,
                    firstAppearancePage = character.firstAppearancePage
                )
                characterService.insertCharacter(characterForService)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteCharacterById(idCharacter: Int, bookId: String) {
        characterDao.delete(idCharacter)
        characterService.deleteCharacter(idCharacter, bookId)
    }

}