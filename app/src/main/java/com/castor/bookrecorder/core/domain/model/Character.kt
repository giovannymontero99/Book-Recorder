package com.castor.bookrecorder.core.domain.model

data class Character(
    val id: Int = 0,
    val bookId: String,
    val name: String,
    val description: String? = null,
    val firstAppearancePage: Int? = null
)
