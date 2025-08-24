package com.castor.bookrecorder.core.presentation.log_in

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavController
import com.castor.bookrecorder.core.presentation.navigation.AddBookRoute
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

suspend fun googleAuthUiClient(
    context: Context,
    navController: NavController
) {
    val credentialManager = CredentialManager.create(context)
    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId("529568803872-rf0tfqqg3oi4hf5mbm2mf2pa7a9bca0r.apps.googleusercontent.com")
        .setFilterByAuthorizedAccounts(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val result = credentialManager.getCredential(context, request)
        // Ensure the credential is of the expected type before casting
        if(result.credential is CustomCredential &&
            result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){

            // Use createFrom to safely get the GoogleIdTokenCredential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val idToken = googleIdTokenCredential.idToken
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            Firebase.auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate(AddBookRoute)
                    } else {
                        Log.d("Error", "googleAuthUiClient: Firebase SignIn failed: ${task.exception}")
                    }
                }
        } else {
            Log.d("Error", "googleAuthUiClient: Received unexpected credential type: ${result.credential.type}")
        }
    }catch (e: Exception){
        e.printStackTrace()
        Log.d("Error", "googleAuthUiClient: $e")
    }
}