package com.castor.bookrecorder.core.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signInWithCredential(idToken: String): FirebaseUser
    fun isUserLoggedIn(): Boolean
    fun signOut()
    suspend fun signUpWithEmailAndPassword(email: String, password: String): FirebaseUser
    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser
}