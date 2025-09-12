package com.castor.bookrecorder.core.presentation.state

sealed interface NavigationState {
    data object NavigateToHome: NavigationState
    data object NavigateToProfile: NavigationState
}