package com.castor.bookrecorder.core.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `memory_ints` (
                `id` TEXT NOT NULL,
                `memoryId` TEXT NOT NULL,
                `text` TEXT NOT NULL,
                `value` INTEGER NOT NULL,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`memoryId`) REFERENCES `memories`(`id`) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_ints_memoryId` ON `memory_ints` (`memoryId`)")
    }
}