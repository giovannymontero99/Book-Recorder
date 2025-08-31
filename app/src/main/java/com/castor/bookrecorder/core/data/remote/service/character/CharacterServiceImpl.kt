package com.castor.bookrecorder.core.data.remote.service.character

import com.castor.bookrecorder.core.data.remote.dto.BookDto
import com.castor.bookrecorder.core.domain.model.Character
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CharacterServiceImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
): CharacterService {
    override suspend fun insertCharacter(character: Character): Void {
        val characterMap = mapOf(
            "description" to character.description,
            "firstAppearancePage" to character.firstAppearancePage,
            "id" to character.id,
            "name" to character.name,
        )

        val characterRef = firebaseFirestore
            .collection("books")
            .document(character.bookId)

        return characterRef.update("characters", FieldValue.arrayUnion(characterMap)).await()
    }

    override suspend fun deleteCharacter(idCharacter: Int, idBook: String) {

        try {
            val bookRef = firebaseFirestore
                .collection("books")
                .document(idBook)

            firebaseFirestore.runTransaction { transaction ->
                val bookSnapshot = transaction.get(bookRef)
                val bookDto = bookSnapshot.toObject(BookDto::class.java)
                val characters = bookDto?.characters
                if (characters != null) {
                    val newCharacters = characters.filter { it.id != idCharacter }
                    transaction.update(bookRef, "characters", newCharacters)
                }
            }.await()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun updateCharacter(character: Character, idBook: String) {
        val characterMap = mapOf(
            "description" to character.description,
            "firstAppearancePage" to character.firstAppearancePage,
            "id" to character.id,
            "name" to character.name,
        )

        val characterRef = firebaseFirestore
            .collection("books")
            .document(idBook)

        firebaseFirestore.runTransaction { transaction ->
            val bookSnapshot = transaction.get(characterRef)
            val bookDto = bookSnapshot.toObject(BookDto::class.java)
            val characters = bookDto?.characters
            if (characters != null) {
                val newCharacters = characters.map {
                    if (it.id == character.id) {
                        characterMap
                    } else {
                        it
                    }
                }
                transaction.update(characterRef, "characters", newCharacters)
            }
        }.await()
    }
}