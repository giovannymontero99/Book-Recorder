package com.castor.bookrecorder.core.presentation.navigation

import androidx.lifecycle.ViewModel
import com.castor.bookrecorder.core.domain.usecase.auth.CheckUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class NavigationWrapperViewModel @Inject constructor(
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase
): ViewModel() {

    private val _isLoggedState = MutableStateFlow<Boolean?>(null)
    val isLoggedState: StateFlow<Boolean?> = _isLoggedState.asStateFlow()

    init {
        validateUserLogged()
    }

    private fun validateUserLogged(){
        _isLoggedState.update { checkUserLoggedInUseCase() }
    }
}