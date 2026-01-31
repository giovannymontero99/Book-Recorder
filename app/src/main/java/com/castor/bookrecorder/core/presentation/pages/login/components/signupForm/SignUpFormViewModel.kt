package com.castor.bookrecorder.core.presentation.pages.login.components.signupForm

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.domain.model.User
import com.castor.bookrecorder.core.domain.usecase.auth.SignInWithCredentialUseCase
import com.castor.bookrecorder.core.domain.usecase.auth.SignUpWithEmailAndPasswordUseCase
import com.castor.bookrecorder.core.domain.usecase.sync.SyncDataUseCase
import com.castor.bookrecorder.core.domain.usecase.user.CreateUserUseCase
import com.castor.bookrecorder.core.presentation.state.NavigationState
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginFormState(
    val firstPassword: String = "",
    val secondPassword: String = "",
    val email: String = "",
    val submittingForm: Boolean = false
)

sealed interface LoginFormEvent {
    data class OnEmailChange(val email: String) : LoginFormEvent
    data class OnFirstPasswordChange(val firstPassword: String) : LoginFormEvent
    data class OnSecondPasswordChange(val secondPassword: String) : LoginFormEvent

    data object OnLogin : LoginFormEvent
}

@HiltViewModel
class SignUpFormViewModel @Inject constructor(
    private val signUpWithEmailAndPasswordUseCase: SignUpWithEmailAndPasswordUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val syncDataUseCase: SyncDataUseCase,
    private val signInWithCredentialUseCase : SignInWithCredentialUseCase
) : ViewModel() {

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> = _navigationState.asStateFlow()

    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState: StateFlow<LoginFormState> = _loginFormState.asStateFlow()


    fun onLoginFormEvent(
        event: LoginFormEvent,
        onErrorCode: ((Int) -> Unit)? = null
    ) {
        when (event) {
            is LoginFormEvent.OnFirstPasswordChange -> {
                _loginFormState.update { it.copy(firstPassword = event.firstPassword) }
            }

            LoginFormEvent.OnLogin -> {
                if ( // When the fields are empty
                    _loginFormState.value.firstPassword.isEmpty() ||
                    _loginFormState.value.secondPassword.isEmpty() ||
                    _loginFormState.value.email.isEmpty()
                ) {
                    onErrorCode?.invoke(R.string.email_and_password_required)
                } else if (_loginFormState.value.firstPassword != _loginFormState.value.secondPassword) {
                    onErrorCode?.invoke(R.string.invalid_same_password)
                }else {
                    onLoginHandler(onErrorCode)
                }
            }

            is LoginFormEvent.OnSecondPasswordChange -> {
                _loginFormState.update { it.copy(secondPassword = event.secondPassword) }
            }

            is LoginFormEvent.OnEmailChange -> {
                _loginFormState.update { it.copy(email = event.email) }
            }
        }
    }


    private fun onLoginHandler(
        onErrorCode: ((Int) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {

                _loginFormState.update { it.copy(submittingForm = true) }

                val userFirebase = signUpWithEmailAndPasswordUseCase(
                    _loginFormState.value.email,
                    _loginFormState.value.firstPassword
                )
                handleUserCreationAndNavigation(userFirebase)
            }catch (e: FirebaseAuthUserCollisionException){
                e.printStackTrace()
                onErrorCode?.invoke(R.string.user_exist)
            }catch (e: FirebaseAuthWeakPasswordException){
                e.printStackTrace()
                onErrorCode?.invoke(R.string.weak_password)
            }
            catch (e: Exception){
                e.printStackTrace()
                onErrorCode?.invoke(R.string.invalid_email)
            } finally {
                _loginFormState.update { it.copy(submittingForm = false) }
            }
        }
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
        createUserUseCase(user).collectLatest { success ->
            if (success) {
                // Sync data
                syncDataUseCase()
                // Navigate to home screen
                _navigationState.update { NavigationState.NavigateToHome }
            }
        }
    }


    fun onSignInWithGoogle(googleCredential: Credential){
        viewModelScope.launch {
            try {

                _loginFormState.update { it.copy(submittingForm = true) }

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
              _loginFormState.update { it.copy(submittingForm = false) }
            }
        }
    }


}