package com.castor.bookrecorder.core.domain.model

data class Character(
    val id: Int = 0,
    val bookId: Int,
    val name: String,
    val description: String? = null,
    val firstAppearancePage: Int? = null
)
