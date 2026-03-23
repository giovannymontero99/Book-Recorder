package com.castor.bookrecorder.core.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `memories` (
                `id` TEXT NOT NULL,
                `created` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `memory_strings` (
                `id` TEXT NOT NULL,
                `memoryId` TEXT NOT NULL,
                `text` TEXT NOT NULL,
                `value` TEXT NOT NULL,
                PRIMARY KEY(`id`),
                FOREIGN KEY(`memoryId`) REFERENCES `memories`(`id`) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_memory_strings_memoryId` ON `memory_strings` (`memoryId`)")
    }
}