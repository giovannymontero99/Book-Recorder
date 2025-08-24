package com.castor.bookrecorder.core.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute


@Serializable
object AddBookRoute

@Serializable
data class BookDetailRoute(
    val id: Int,
    val title: String
)

@Serializable
data class EditBookRoute(
    val id: Int
)

@Serializable
object LoginRoute

@Serializable
object AccountRoute