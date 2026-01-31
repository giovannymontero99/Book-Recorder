package com.castor.bookrecorder.core.presentation.pages.login.components.loginForm

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.R
import com.castor.bookrecorder.core.presentation.pages.login.components.AuthenticationButton
import com.castor.bookrecorder.core.presentation.state.NavigationState

@Composable
fun SignInForm(
    viewModel: SignInFormViewModel = hiltViewModel(),
    onNavigateToSignUp: (Boolean) -> Unit,
    navigateToHome: () -> Unit
) {

    val loginState by viewModel.loginState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val navigationState by viewModel.navigationState.collectAsState()

    // Navigate to home screen
    when(navigationState){
        is NavigationState.NavigateToHome -> {
            navigateToHome()
        }
        else -> {}
    }

    if (loginState.submittingForm){
        CircularProgressIndicator()
    }

    Text(
        text = stringResource(R.string.sign_in),
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = MaterialTheme.typography.titleLarge.fontWeight
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row {
        Text(
            text = stringResource(R.string.dont_have_an_account),
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )
        Text(
            modifier = Modifier
                .clickable{
                    onNavigateToSignUp(true)
                },
            text = stringResource(R.string.sign_in ),
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = MaterialTheme.typography.titleSmall.fontWeight
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = loginState.email,
        onValueChange = { viewModel.onLoginEvent(LoginEvent.OnEmailChange(it)) },
        label = { Text(stringResource(R.string.email)) },
        singleLine = true,
        enabled = !loginState.submittingForm
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = loginState.password,
        onValueChange = {
            viewModel.onLoginEvent(LoginEvent.OnPasswordChange(it))
        },
        label = { Text(stringResource(R.string.password)) },
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = { passwordVisible = !passwordVisible }){
                Icon(imageVector  = image, "Visibility icon")
            }
        },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = !loginState.submittingForm
    )

    Spacer(modifier = Modifier.height(16.dp))

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        modifier = Modifier
            .height(50.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        onClick = {
            viewModel.onLoginEvent(LoginEvent.OnLogin){ errorCode ->
                Toast.makeText(
                    context,
                    context.getString(errorCode),
                    Toast.LENGTH_LONG
                ).show()
            }
        },
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(10.dp),
        enabled = !loginState.submittingForm
    ) {
        Text(text = stringResource(R.string.sign_up))
    }


    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )

        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )

    }

    Spacer(modifier = Modifier.height(16.dp))


    AuthenticationButton(buttonText = R.string.continue_with_google) { googleCredential ->
        viewModel.onLoginEvent(LoginEvent.OnSignInWithGoogle(googleCredential))
    }

}