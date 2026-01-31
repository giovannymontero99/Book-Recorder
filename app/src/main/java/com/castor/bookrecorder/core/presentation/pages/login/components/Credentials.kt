package com.castor.bookrecorder.core.presentation.pages.login.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.presentation.ui.theme.Purple40
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import kotlinx.coroutines.launch

@Composable
fun AuthenticationButton(isSignIn: Boolean = true,buttonText: Int,onRequestResult: (Credential) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = { coroutineScope.launch { launchCredManButtonUI(isSignIn, context, onRequestResult) } },
        colors = ButtonDefaults.buttonColors(containerColor = Purple40),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.google_g),
            modifier = Modifier.padding(horizontal = 16.dp),
            contentDescription = "Google logo"
        )

        Text(
            text = stringResource(buttonText),
            fontSize = 16.sp,
            modifier = Modifier.padding(0.dp, 6.dp)
        )

    }

}

private suspend fun launchCredManButtonUI(
    isSignIn: Boolean = true,
    context: Context,
    onRequestResult: (Credential) -> Unit
) {
    try {


        val signInWithGoogleOption = if (isSignIn){
            GetSignInWithGoogleOption
                .Builder(serverClientId = context.getString(R.string.default_web_client_id))
                .build()
        }else{
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()
        }


        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )

        onRequestResult(result.credential)
    } catch (e: NoCredentialException) {
        Log.d("BOOK RECORDER APP ERROR", e.message.orEmpty())
    } catch (e: GetCredentialException) {
        Log.d("BOOK RECORDER APP ERROR", e.message.orEmpty())
    }
}