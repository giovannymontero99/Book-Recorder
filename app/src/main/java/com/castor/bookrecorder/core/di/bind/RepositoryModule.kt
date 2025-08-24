package com.castor.bookrecorder.core.di.bind

import com.castor.bookrecorder.core.data.local.repository.BookRepositoryImpl
import com.castor.bookrecorder.core.data.local.repository.CharacterRepositoryImpl
import com.castor.bookrecorder.core.data.remote.repository.AuthRepositoryImpl
import com.castor.bookrecorder.core.domain.repository.AuthRepository
import com.castor.bookrecorder.core.domain.repository.BookRepository
import com.castor.bookrecorder.core.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindBookRepository(bookRepositoryImpl: BookRepositoryImpl): BookRepository

    @Singleton
    @Binds
    abstract fun bindCharacterRepository(characterRepositoryImpl: CharacterRepositoryImpl): CharacterRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository


}