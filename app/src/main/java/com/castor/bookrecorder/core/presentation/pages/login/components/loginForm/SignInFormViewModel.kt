package com.castor.bookrecorder.core.presentation.pages.login.components.loginForm

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.domain.model.User
import com.castor.bookrecorder.core.domain.usecase.auth.SignInWithCredentialUseCase
import com.castor.bookrecorder.core.domain.usecase.auth.SignInWithEmailAndPasswordUseCase
import com.castor.bookrecorder.core.domain.usecase.sync.SyncDataUseCase
import com.castor.bookrecorder.core.domain.usecase.user.CreateUserUseCase
import com.castor.bookrecorder.core.presentation.state.NavigationState
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginState(
    val email: String = "",
    val password: String = "",
    val submittingForm: Boolean = false
)


sealed interface LoginEvent{
    data class OnEmailChange(val email: String): LoginEvent
    data class OnPasswordChange(val password: String): LoginEvent
    data object OnLogin: LoginEvent
    data class OnSignInWithGoogle(val data: Credential): LoginEvent
}



@HiltViewModel
class SignInFormViewModel @Inject constructor(
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val syncDataUseCase: SyncDataUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase
): ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> = _navigationState.asStateFlow()



    fun onLoginEvent(
        event: LoginEvent,
        onErrorCode: ((Int) -> Unit)? = null
    ){
        when(event){
            is LoginEvent.OnEmailChange -> _loginState.update { it.copy(email = event.email) }
            is LoginEvent.OnPasswordChange -> {
                _loginState.update { it.copy(password = event.password) }
            }
            LoginEvent.OnLogin -> {
                if(isFormValid()){
                    viewModelScope.launch {
                        try {
                            _loginState.update { it.copy(submittingForm = true) }
                            val userFirebase = signInWithEmailAndPasswordUseCase(loginState.value.email, loginState.value.password)
                            handleUserCreationAndNavigation(userFirebase)
                        }catch (e: Exception) {
                            e.printStackTrace()
                            onErrorCode?.invoke(R.string.invalid_email)
                        } finally {
                            _loginState.update { it.copy(submittingForm = false) }
                        }
                    }
                }else{
                    onErrorCode?.invoke(R.string.invalid_email)
                }
            }

            is LoginEvent.OnSignInWithGoogle -> {
                onSignInWithGoogle(event.data)
            }
        }
    }

    private fun isFormValid(): Boolean {
        return loginState.value.email.isNotEmpty() && loginState.value.password.isNotEmpty()
    }


    private suspend fun handleUserCreationAndNavigation(userFirebase: FirebaseUser) {
        // Create the user
        val user = User(
            id = userFirebase.uid,
            name = userFirebase.displayName ?: "",
            email = userFirebase.email ?: "",
            photoUrl = userFirebase.photoUrl,
            phoneNumber = userFirebase.phoneNumber
        )
        createUserUseCase(user).collectLatest { isSuccessful ->
            if (isSuccessful) {
                // Sync data
                syncDataUseCase()
                // Navigate to home screen
                _navigationState.update { NavigationState.NavigateToHome }
            }
        }
    }


    private fun onSignInWithGoogle(googleCredential: Credential){
        viewModelScope.launch {
            try {

                _loginState.update { it.copy(submittingForm = true) }

                if(googleCredential is CustomCredential && googleCredential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(googleCredential.data)
                    val idToken = googleIdTokenCredential.idToken
                    val credential = signInWithCredentialUseCase(idToken)
                    handleUserCreationAndNavigation(credential)
                }
            } catch (e: Exception){
                // Error occurred during sign-in
                val message = e.message ?: "Unknown error"
                // Show error message
                e.printStackTrace()
                Log.e("LoginViewModel", "signInWithGoogle: $message")
                // Update the login result
            } finally {
                _loginState.update { it.copy(submittingForm = false) }
            }
        }
    }

}