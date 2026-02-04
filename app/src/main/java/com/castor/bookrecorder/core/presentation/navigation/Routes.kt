package com.castor.bookrecorder.core.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute


@Serializable
object AddBookRoute

@Serializable
data class BookDetailRoute(
    val id: String,
    val title: String
)

@Serializable
data class EditBookRoute(
    val id: String
)

@Serializable
object LoginRoute

@Serializable
object AccountRoute

@Serializable
object BooksListRoute

@Serializable
object FavoritesRoute

@Serializable
object MemoryBoxRoute

@Serializable
object NewBoxMemoryRoute
