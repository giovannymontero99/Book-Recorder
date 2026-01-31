package com.castor.bookrecorder.core.presentation.pages.login.components.signupForm

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
fun SignUpForm(
    viewModel: SignUpFormViewModel = hiltViewModel(),
    onNavigateToSignUp: (Boolean) -> Unit,
    navigateToHome: () -> Unit
) {

    // Collect the login form state
    val loginFormState by viewModel.loginFormState.collectAsState()

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

    // Show loading state
    if(loginFormState.submittingForm){
        CircularProgressIndicator()
    }

    // Back to login
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = { onNavigateToSignUp(false) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to login"
            )
        }

        Text(
            text = stringResource(R.string.back_login),
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )

    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        Text(
            text = stringResource(R.string.sign_up),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight
        )
    }


    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = loginFormState.email,
        onValueChange = {
            viewModel.onLoginFormEvent(LoginFormEvent.OnEmailChange(it))
        },
        label = { Text(stringResource(R.string.email)) },
        singleLine = true,
        enabled = !loginFormState.submittingForm
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = loginFormState.firstPassword,
        onValueChange = {
            viewModel.onLoginFormEvent(LoginFormEvent.OnFirstPasswordChange(it))
        },
        label = { Text(stringResource(R.string.password)) },
        trailingIcon = {
            val visibilityIcon = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = visibilityIcon, "Visibility icon")
            }
        },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = !loginFormState.submittingForm
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = loginFormState.secondPassword,
        onValueChange = {
            viewModel.onLoginFormEvent(LoginFormEvent.OnSecondPasswordChange(it))
        },
        label = { Text(stringResource(R.string.password)) },
        trailingIcon = {
            val visibilityIcon = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = visibilityIcon, "Visibility icon")
            }
        },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = !loginFormState.submittingForm
    )

    Spacer(modifier = Modifier.height(16.dp))

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        modifier = Modifier
            .height(50.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        onClick = {
            viewModel.onLoginFormEvent(LoginFormEvent.OnLogin){
                Toast.makeText(
                    context,
                    context.getString(it),
                    Toast.LENGTH_LONG
                ).show()
            }
            //onSignInClick()
        },
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = stringResource( R.string.sign_in), fontSize = MaterialTheme.typography.titleMedium.fontSize)
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
        viewModel.onSignInWithGoogle(googleCredential)
    }

}