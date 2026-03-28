package com.castor.bookrecorder.core.domain.model

/**
 * Factory Method Pattern — Product
 * */
sealed class MemoryValue {
    abstract val id: String
    abstract val text: String
}

data class MemoryStringValue(
    override val id: String,
    override val text: String,
    val value: String
) : MemoryValue()

data class MemoryIntValue(
    override val id: String,
    override val text: String,
    val value: Int
) : MemoryValue()

data class MemoryBooleanValue(
    override val id: String,
    override val text: String,
    val value: Boolean
) : MemoryValue()

data class Memory(
    val id: String,
    val created: Long,
    val value: MemoryValue
)