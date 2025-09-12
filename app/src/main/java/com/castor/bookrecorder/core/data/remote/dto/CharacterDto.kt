package com.castor.bookrecorder.core.data.remote.dto


data class CharacterDto(
    val id: Int = 0,
    val bookId: String = "",
    val name: String = "",
    val description: String? = null,
    val firstAppearancePage: Int? = null
)
