package com.castor.bookrecorder.core.di.bind

import com.castor.bookrecorder.core.data.local.service.BookService
import com.castor.bookrecorder.core.data.local.service.BookServiceImpl
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
}