package com.castor.bookrecorder.core.data.remote.repository

import android.util.Log
import com.castor.bookrecorder.core.data.remote.service.user.UserRemoteService
import com.castor.bookrecorder.core.domain.model.User
import com.castor.bookrecorder.core.domain.repository.UserRemoteRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UserRemoteRepositoryImpl @Inject constructor(
    private val userRemoteService: UserRemoteService,
    private val firebaseFirestore: FirebaseFirestore
): UserRemoteRepository {
    override fun addUser(user: User): Flow<Boolean> = flow{

        val userRef = userRemoteService.getUserRef(user.id)

        val userMap = hashMapOf(
            "name" to user.name,
            "email" to user.email
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

private fun FirebaseFirestore.runTransaction(updateFunction: (com.google.firebase.firestore.Transaction) -> String?) {}
