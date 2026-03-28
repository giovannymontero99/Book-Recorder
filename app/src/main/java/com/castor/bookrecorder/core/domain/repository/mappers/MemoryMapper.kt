package com.castor.bookrecorder.core.domain.repository.mappers

import com.castor.bookrecorder.core.data.local.entity.MemoryBooleanEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryIntEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryStringEntity
import com.castor.bookrecorder.core.data.local.entity.MemoryWithRelations
import com.castor.bookrecorder.core.domain.model.Memory
import com.castor.bookrecorder.core.domain.model.MemoryBooleanValue
import com.castor.bookrecorder.core.domain.model.MemoryIntValue
import com.castor.bookrecorder.core.domain.model.MemoryStringValue
import com.castor.bookrecorder.core.domain.model.MemoryValue

fun MemoryStringValue.toEntity(memoryId: String): MemoryStringEntity {
    return MemoryStringEntity(
        id = this.id,
        memoryId = memoryId,
        text = this.text,
        value = this.value
    )
}

fun MemoryIntValue.toEntity(memoryId: String): MemoryIntEntity {
    return MemoryIntEntity(
        id = this.id,
        memoryId = memoryId,
        text = this.text,
        value = this.value
    )
}

fun MemoryBooleanValue.toEntity(memoryId: String): MemoryBooleanEntity {
    return MemoryBooleanEntity(
        id = this.id,
        memoryId = memoryId,
        text = this.text,
        value = this.value
    )
}

fun Memory.toEntity(): MemoryEntity {
    return MemoryEntity(
        id = this.id,
        created = this.created
    )
}

fun MemoryWithRelations.toDomain(): Memory? {
    val memoryValue: MemoryValue = when {
        strings.isNotEmpty() -> strings.first().let {
            MemoryStringValue(id = it.id, text = it.text, value = it.value)
        }
        ints.isNotEmpty() -> ints.first().let {
            MemoryIntValue(id = it.id, text = it.text, value = it.value)
        }
        booleans.isNotEmpty() -> booleans.first().let {
            MemoryBooleanValue(id = it.id, text = it.text, value = it.value)
        }
        else -> return null
    }
    return Memory(id = memory.id, created = memory.created, value = memoryValue)
}