package com.castor.bookrecorder.core.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `memory_booleans` (
                `id` TEXT NOT NULL,
                `memoryId` TEXT NOT NULL,
                `text` TEXT NOT NULL,
                `value` INTEGER NOT NULL,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`memoryId`) REFERENCES `memories`(`id`) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_booleans_memoryId` ON `memory_booleans` (`memoryId`)")
    }
}