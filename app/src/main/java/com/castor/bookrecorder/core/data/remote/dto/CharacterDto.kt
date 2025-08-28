package com.castor.bookrecorder.core.data.remote.dto


data class CharacterDto(
    val id: Int = 0,
    val bookId: Int,
    val name: String,
    val description: String? = null,
    val firstAppearancePage: Int? = null
)
