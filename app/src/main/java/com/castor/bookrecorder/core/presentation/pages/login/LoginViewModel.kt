package com.castor.bookrecorder.core.presentation.pages.login

import android.content.Context
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.domain.model.User
import com.castor.bookrecorder.core.domain.usecase.auth.SignInWithCredentialUseCase
import com.castor.bookrecorder.core.domain.usecase.sync.SyncDataUseCase
import com.castor.bookrecorder.core.domain.usecase.user.CreateUserUseCase
import com.castor.bookrecorder.core.presentation.state.NavigationState
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface AuthResult <T> {
    object Loading: AuthResult<Nothing>
    data class Success<T>(val user: T) : AuthResult<T>
}



@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val syncDataUseCase: SyncDataUseCase
): ViewModel() {

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> = _navigationState.asStateFlow()

    private val _loginResult = MutableStateFlow<AuthResult<Boolean>?>(null)
    val loginResult: StateFlow<AuthResult<Boolean>?> = _loginResult.asStateFlow()

    fun signInWithGoogle(
        context: Context
    ) {

        viewModelScope.launch {
            try {
                // Show loading state
                _loginResult.update { AuthResult.Loading as AuthResult<Boolean> }

                // Create the Google Sign-In request
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = CredentialManager.create(context).getCredential(context, request)

                if(result.credential is CustomCredential && result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){

                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                    val idToken = googleIdTokenCredential.idToken

                    val userFirebase = signInWithCredentialUseCase(idToken)

                    val user = User(
                        id = userFirebase.uid,
                        name = userFirebase.displayName ?: "",
                        email = userFirebase.email ?: "",
                        photoUrl = userFirebase.photoUrl,
                        phoneNumber = userFirebase.phoneNumber
                    )
                    createUserUseCase(user).collectLatest { success ->
                        if(success){
                            // Sync data
                            syncDataUseCase()
                            // Navigate to home screen
                            _navigationState.update { NavigationState.NavigateToHome }
                        }
                    }

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
                // Update the login result
                _loginResult.update { null }
            }
        }
    }
}