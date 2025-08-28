package com.castor.bookrecorder.core.di.bind

import com.castor.bookrecorder.core.data.local.service.character.CharacterService
import com.castor.bookrecorder.core.data.local.service.character.CharacterServiceImpl
import com.castor.bookrecorder.core.data.remote.service.book.BookService
import com.castor.bookrecorder.core.data.remote.service.book.BookServiceImpl
import com.castor.bookrecorder.core.data.remote.service.user.UserRemoteService
import com.castor.bookrecorder.core.data.remote.service.user.UserRemoteServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {


    @Singleton
    @Binds
    abstract fun bindCharacterService(characterServiceImpl: CharacterServiceImpl): CharacterService

    @Singleton
    @Binds
    abstract fun bindBookService(bookRemoteServiceImpl: BookServiceImpl): BookService

    @Singleton
    @Binds
    abstract fun bindUserRemoteService(userRemoteServiceImpl: UserRemoteServiceImpl): UserRemoteService

}