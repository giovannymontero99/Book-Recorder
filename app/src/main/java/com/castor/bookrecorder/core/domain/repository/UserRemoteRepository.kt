package com.castor.bookrecorder.core.domain.repository

import com.castor.bookrecorder.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRemoteRepository {

    fun addUser(user: User) : Flow<Boolean>

    fun getCurrentUserId(): String?
}