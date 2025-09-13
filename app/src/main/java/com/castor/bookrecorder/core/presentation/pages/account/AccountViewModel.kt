package com.castor.bookrecorder.core.presentation.pages.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.usecase.auth.SignOutUseCase
import com.castor.bookrecorder.core.domain.usecase.sync.SyncDataUseCase
import com.castor.bookrecorder.core.presentation.state.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface AccountEvent {
    data object SignOut: AccountEvent
    data object SyncData: AccountEvent
}

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val syncDataUseCase: SyncDataUseCase
): ViewModel() {

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> = _navigationState.asStateFlow()

    fun accountHandler(event: AccountEvent){
        when(event){
            AccountEvent.SignOut -> signOut()
            AccountEvent.SyncData -> syncData()
        }
    }

    private fun signOut(){
        signOutUseCase()
        this._navigationState.update { NavigationState.NavigateToLogin }
    }

    private fun syncData(){
        viewModelScope.launch {
            syncDataUseCase()
        }
    }
}