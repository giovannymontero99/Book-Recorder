package com.castor.bookrecorder.core.data.remote.service.user

import com.castor.bookrecorder.core.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

interface UserRemoteService {
    fun getCurrentUserId() : String?

    fun<K,V> createUser(user: HashMap<K,V>): Task<DocumentReference>

    fun getUserRef(idUser: String): DocumentReference

}