package com.castor.bookrecorder.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.local.dao.BookTitleDao
import com.castor.bookrecorder.core.data.local.dao.CharacterDao
import com.castor.bookrecorder.core.data.local.entity.BookEntity
import com.castor.bookrecorder.core.data.local.entity.BookTitleEntity
import com.castor.bookrecorder.core.data.local.entity.CharacterEntity

@Database(
    entities = [
        BookTitleEntity::class,
        BookEntity::class,
        CharacterEntity::class
        // Agrega más entidades según sea necesario
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract val bookTitleDao: BookTitleDao
    abstract val bookDao: BookDao
    abstract val characterDao: CharacterDao
}