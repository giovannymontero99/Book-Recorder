package com.castor.bookrecorder.core.domain.repository.mappers

import com.castor.bookrecorder.core.data.local.entity.BookEntity
import com.castor.bookrecorder.core.domain.model.Book

fun Book.toBookEntity(): BookEntity = BookEntity(
    id = this.id,
    title = this.title,
    author = this.author,
    genre = this.genre,
    startDate = this.startDate,
    finishDate = this.finishDate,
    progress = this.progress,
    totalPages = this.totalPages,
    notes = this.notes,
    summary = this.summary,
    quotes = this.quotes,
    coverImageUri = this.coverImageUri,
    isFinished = this.isFinished
)

fun BookEntity.toBook(): Book = Book(
    id = this.id,
    title = this.title,
    author = this.author,
    genre = this.genre,
    startDate = this.startDate,
    finishDate = this.finishDate,
    progress = this.progress,
    totalPages = this.totalPages,
    notes = this.notes,
    summary = this.summary,
    quotes = this.quotes,
    coverImageUri = this.coverImageUri,
    isFinished = this.isFinished
)