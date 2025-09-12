package com.castor.bookrecorder.core.domain.repository

interface SyncRepository {

    suspend fun syncData()
}