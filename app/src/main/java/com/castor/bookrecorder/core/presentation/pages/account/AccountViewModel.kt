package com.castor.bookrecorder.core.presentation.pages.account

import androidx.lifecycle.ViewModel
import com.castor.bookrecorder.core.domain.usecase.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
): ViewModel() {

    fun signOut(){
        signOutUseCase()
    }
}