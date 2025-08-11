package com.castor.bookrecorder.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookTitleEntity(
    @PrimaryKey(autoGenerate = true)
    val idBookTitle: Int,
    val title: String,
)
