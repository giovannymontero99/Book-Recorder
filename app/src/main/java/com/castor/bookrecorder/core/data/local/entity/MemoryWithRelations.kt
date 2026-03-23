package com.castor.bookrecorder.core.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MemoryWithRelations(
    @Embedded val memory: MemoryEntity,

    @Relation(parentColumn = "id", entityColumn = "memoryId")
    val strings: List<MemoryStringEntity>,

    @Relation(parentColumn = "id", entityColumn = "memoryId")
    val ints: List<MemoryIntEntity>,

    @Relation(parentColumn = "id", entityColumn = "memoryId")
    val booleans: List<MemoryBooleanEntity>
)