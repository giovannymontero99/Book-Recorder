package com.castor.bookrecorder.core.data

import android.util.Log
import com.castor.bookrecorder.core.data.remote.service.user.UserService
import com.castor.bookrecorder.core.domain.model.User
import com.castor.bookrecorder.core.domain.repository.UserRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteService: UserService,
    private val firebaseFirestore: FirebaseFirestore
): UserRepository {
    override fun addUser(user: User): Flow<Boolean> = flow{

        val userRef = userRemoteService.getUserRef(user.id)

        val userMap = hashMapOf(
            "name" to user.name,
            "email" to user.email,
            "photoUrl" to user.photoUrl,
            "phoneNumber" to user.phoneNumber
        )
        try {
            firebaseFirestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef) // Read the document into the transaction
                // Validate if user already exists
                if(snapshot.exists()){
                    transaction.update(userRef, "timestampLogin", FieldValue.serverTimestamp())
                    return@runTransaction "EXIST"
                } else {
                    transaction.set(userRef, userMap)
                    transaction.update(userRef, "timestampCreated", FieldValue.serverTimestamp())
                    return@runTransaction "CREATED"
                }
            }.await()
            emit(true)
        } catch (e: Exception) {
            Log.d("UserRemoteRepository", "addUser: ${e.message}")
            emit(false)
        }
    }

    override fun getCurrentUserId(): String? {
        return userRemoteService.getCurrentUserId()
    }
}

private fun FirebaseFirestore.runTransaction(updateFunction: (Transaction) -> String?) {}
