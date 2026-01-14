package com.castor.bookrecorder.core.di

import android.content.Context
import androidx.room.Room
import com.castor.bookrecorder.core.data.local.database.AppDatabase
import com.castor.bookrecorder.core.data.local.migrations.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase( @ApplicationContext context: Context  ): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bookrecorde_db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideBookTitleDao(
        db: AppDatabase
    ) = db.bookTitleDao

    @Provides
    @Singleton
    fun provideBookDao(
        db: AppDatabase
    ) = db.bookDao

    @Provides
    @Singleton
    fun provideCharacterDao(
        db: AppDatabase
    ) = db.characterDao

}