package com.castor.bookrecorder.core.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signInWithCredential(idToken: String): FirebaseUser
    fun isUserLoggedIn(): Boolean
    fun signOut()
}