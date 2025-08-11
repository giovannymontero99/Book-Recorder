package com.castor.bookrecorder.core.domain.model

data class Book(
    val id: Int = 0,
    val title: String,
    val author: String,
    val genre: String?,
    val startDate: Long?,
    val finishDate: Long?,
    val progress: Int,
    val totalPages: Int?,
    val notes: String?,
    val summary: String?,
    val quotes: String?,
    val coverImageUri: String?,
    val isFinished: Boolean
)
