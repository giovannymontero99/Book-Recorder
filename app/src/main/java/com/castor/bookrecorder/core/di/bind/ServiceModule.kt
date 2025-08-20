package com.castor.bookrecorder.core.di.bind

import com.castor.bookrecorder.core.data.local.service.book.BookService
import com.castor.bookrecorder.core.data.local.service.book.BookServiceImpl
import com.castor.bookrecorder.core.data.local.service.character.CharacterService
import com.castor.bookrecorder.core.data.local.service.character.CharacterServiceImpl
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
    abstract fun bindBookService(bookServiceImpl: BookServiceImpl): BookService

    @Singleton
    @Binds
    abstract fun bindCharacterService(characterServiceImpl: CharacterServiceImpl): CharacterService

}