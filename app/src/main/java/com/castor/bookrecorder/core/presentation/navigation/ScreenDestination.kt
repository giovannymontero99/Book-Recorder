package com.castor.bookrecorder.core.presentation.navigation

sealed interface ScreenDestination {

    data object AddBook: ScreenDestination
    data object AddMemoryBox: ScreenDestination
    data class BookDetail(val id: String, val title: String): ScreenDestination
    data class EditBook(val id: String): ScreenDestination
    data object Account: ScreenDestination

    data object NewBoxMemory: ScreenDestination
}