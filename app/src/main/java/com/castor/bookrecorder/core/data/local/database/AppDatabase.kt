package com.castor.bookrecorder.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.local.dao.BookTitleDao
import com.castor.bookrecorder.core.data.local.dao.CharacterDao
import com.castor.bookrecorder.core.data.local.dao.MemoryDao
import com.castor.bookrecorder.core.data.local.entity.BookEntity
import com.castor.bookrecorder.core.data.local.entity.BookTitleEntity
import com.castor.bookrecorder.core.data.local.entity.CharacterEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryBooleanEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryIntEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryStringEntity

@Database(
    entities = [
        BookTitleEntity::class,
        BookEntity::class,
        CharacterEntity::class,
        MemoryStringEntity::class,
        MemoryEntity::class,
        MemoryIntEntity::class,
        MemoryBooleanEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract val bookTitleDao: BookTitleDao
    abstract val bookDao: BookDao
    abstract val characterDao: CharacterDao
    abstract val memoryDao: MemoryDao
}