package com.castor.bookrecorder.core.domain.repository

import com.castor.bookrecorder.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun addUser(user: User) : Flow<Boolean>

    fun getCurrentUserId(): String?
}