package com.castor.bookrecorder.core.presentation.pages.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.core.domain.usecase.auth.SignInWithCredentialUseCase
import com.castor.bookrecorder.core.domain.usecase.sync.SyncDataUseCase
import com.castor.bookrecorder.core.domain.usecase.user.CreateUserUseCase
import com.castor.bookrecorder.core.presentation.state.NavigationState
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.domain.model.User
import com.castor.bookrecorder.core.domain.usecase.auth.SignInWithEmailAndPasswordUseCase
import com.castor.bookrecorder.core.domain.usecase.auth.SignUpWithEmailAndPasswordUseCase
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collectLatest


sealed interface AuthResult <T> {
    object Loading: AuthResult<Nothing>
    data class Success<T>(val user: T) : AuthResult<T>
}

sealed interface LoginEvent{
    data class OnEmailChange(val email: String): LoginEvent
    data class OnPasswordChange(val password: String): LoginEvent
    data object OnLogin: LoginEvent
}


data class LoginState(
    val email: String = "",
    val password: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val syncDataUseCase: SyncDataUseCase,
    private val signUpWithEmailAndPasswordUseCase: SignUpWithEmailAndPasswordUseCase,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
): ViewModel() {

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> = _navigationState.asStateFlow()

    private val _loginResult = MutableStateFlow<AuthResult<Boolean>?>(null)
    val loginResult: StateFlow<AuthResult<Boolean>?> = _loginResult.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun signInWithGoogle(
        context: Context
    ) {

        viewModelScope.launch {
            try {

                // Show loading state
                _loginResult.update { AuthResult.Loading as AuthResult<Boolean> }

                // Create the Google Sign-In request
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = CredentialManager.create(context).getCredential(context, request)

                if(result.credential is CustomCredential && result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    handleUserCreationAndNavigation(signInWithCredentialUseCase(idToken))
                }

            }catch (e: Exception){
                // Error occurred during sign-in
                val message = e.message ?: "Unknown error"
                // Show error message
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
                Log.e("LoginViewModel", "signInWithGoogle: $message")

                // Update the login result
                _loginResult.update { null }
            }
        }
    }

    // Sign up with email and password
    fun signUpCustomEmail(
        event: LoginEvent,
        onErrorCode: (Int) -> Unit
    ){
        when(event){
            is LoginEvent.OnEmailChange -> {
                this._loginState.update { it.copy(email = event.email) }
            }
            is LoginEvent.OnPasswordChange -> {
                this._loginState.update { it.copy(password = event.password) }
            }

            LoginEvent.OnLogin -> {
                if(loginState.value.email.isNotEmpty() && loginState.value.password.isNotEmpty()){
                    viewModelScope.launch {
                        try {
                            // Show loading state
                            _loginResult.update { AuthResult.Loading as AuthResult<Boolean> }

                            val userFirebase = signUpWithEmailAndPasswordUseCase(loginState.value.email, loginState.value.password)
                            handleUserCreationAndNavigation(userFirebase)
                        }catch (e: FirebaseAuthUserCollisionException){
                            e.printStackTrace()
                            onErrorCode(R.string.user_exist)
                            _loginResult.update { null }
                        }catch (e: FirebaseAuthWeakPasswordException){
                            e.printStackTrace()
                            onErrorCode(R.string.weak_password)
                            _loginResult.update { null }
                        }catch (e: FirebaseAuthInvalidCredentialsException){
                            e.printStackTrace()
                            onErrorCode(R.string.invalid_email)
                            _loginResult.update { null }
                        }
                        catch (e: Exception){
                            e.printStackTrace()
                            Log.d("LoginViewModelError", "signInCustomEmail: $e")
                            _loginResult.update { null }
                        }
                    }
                }else{
                    onErrorCode(R.string.empty_credentials)
                }
            }
        }
    }

    fun signInCustomEmail(
        event: LoginEvent,
        onErrorCode: (Int) -> Unit
    ){
        when(event){
            is LoginEvent.OnEmailChange -> {
                this._loginState.update { it.copy(email = event.email) }
            }
            LoginEvent.OnLogin -> {
                if(loginState.value.email.isNotEmpty() && loginState.value.password.isNotEmpty()){
                    viewModelScope.launch {
                        try {
                            // Show loading state
                            _loginResult.update { AuthResult.Loading as AuthResult<Boolean> }

                            val userFirebase = signInWithEmailAndPasswordUseCase(loginState.value.email, loginState.value.password)
                            handleUserCreationAndNavigation(userFirebase)
                        }catch (e: FirebaseAuthInvalidCredentialsException){
                            e.printStackTrace()
                            onErrorCode(R.string.invalid_email)
                            _loginResult.update { null }
                        }
                    }
                }else{
                    onErrorCode(R.string.empty_credentials)
                }
            }
            is LoginEvent.OnPasswordChange -> {
                this._loginState.update { it.copy(password = event.password) }
            }
        }
    }

    // Handle the user creation and navigation
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
}