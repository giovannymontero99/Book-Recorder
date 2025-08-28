package com.castor.bookrecorder.core.di.bind

import com.castor.bookrecorder.core.data.BookRepositoryImpl
import com.castor.bookrecorder.core.data.CharacterRepositoryImpl
import com.castor.bookrecorder.core.data.remote.repository.AuthRepositoryImpl
import com.castor.bookrecorder.core.data.remote.repository.BookRemoteRepositoryImpl
import com.castor.bookrecorder.core.data.remote.repository.UserRemoteRepositoryImpl
import com.castor.bookrecorder.core.domain.repository.AuthRepository
import com.castor.bookrecorder.core.domain.repository.BookRepository
import com.castor.bookrecorder.core.domain.repository.CharacterRepository
import com.castor.bookrecorder.core.domain.repository.BookRemoteRepository
import com.castor.bookrecorder.core.domain.repository.UserRemoteRepository
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

    @Singleton
    @Binds
    abstract fun bindFirebaseFirestoreRepository(firebaseFirestoreRepositoryImpl: BookRemoteRepositoryImpl): BookRemoteRepository

    @Singleton
    @Binds
    abstract fun bindUserRemoteRepository(userRemoteRepositoryImpl: UserRemoteRepositoryImpl): UserRemoteRepository
}