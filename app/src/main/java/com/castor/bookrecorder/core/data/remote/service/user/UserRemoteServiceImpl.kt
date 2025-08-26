package com.castor.bookrecorder.core.data.remote.service.user

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRemoteServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
): UserRemoteService {
    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override fun <K, V> createUser(user: HashMap<K, V>): Task<DocumentReference> {
        return firebaseFirestore.collection("users").add(user)
    }

    override fun getUserRef(idUser: String): DocumentReference {
        return firebaseFirestore.collection("users").document(idUser)
    }
}