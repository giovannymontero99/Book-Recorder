package com.castor.bookrecorder.core.data.remote.service.character

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
}