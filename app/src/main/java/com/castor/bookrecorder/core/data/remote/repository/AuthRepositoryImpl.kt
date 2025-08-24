package com.castor.bookrecorder.core.data.remote.repository

import com.castor.bookrecorder.core.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(

): AuthRepository {
    override suspend fun signInWithCredential(idToken: String): FirebaseUser {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = Firebase.auth.signInWithCredential(credential).await()
            authResult.user ?: throw Exception("Firebase user not found")
        }catch ( e: Exception){
            throw e
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}