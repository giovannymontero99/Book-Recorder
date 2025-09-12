package com.castor.bookrecorder.core.domain.model

data class Book(
    var id: String = "",
    val title: String = "",
    val author: String = "",
    val genre: String? = null,
    val startDate: Long? = null,
    val finishDate: Long? = null,
    val progress: Int = 0,
    val totalPages: Int? = null,
    val notes: String? = null,
    val summary: String? = null,
    val quotes: String? = null,
    val coverImageUri: String? = null,
    var isFinished: Boolean = false,
    val userID: String = ""
)