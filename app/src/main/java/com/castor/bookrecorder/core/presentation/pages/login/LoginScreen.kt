package com.castor.bookrecorder.core.presentation.pages.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.castor.bookrecorder.core.presentation.state.NavigationState
import com.castor.bookrecorder.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit
) {

    val loginResult by viewModel.loginResult.collectAsState()
    val context = LocalContext.current
    val navigationState by viewModel.navigationState.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val signUpCustomEmail = viewModel::signUpCustomEmail
    val signInCustomEmail = viewModel::signInCustomEmail
    val signInWithGoogle = viewModel::signInWithGoogle
    var passwordVisible by remember { mutableStateOf(false) }

    var isSignUp by remember { mutableStateOf(false) }

    when(navigationState){
        is NavigationState.NavigateToHome -> {
            navigateToHome()
        }
        else -> {}
    }

    when(loginResult){
        AuthResult.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        is AuthResult.Success<Boolean> -> {

        }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(
                        text = stringResource(if(isSignUp) R.string.sign_up else R.string.sign_in),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        Text(
                            text = stringResource(if(isSignUp) R.string.already_have_an_account else R.string.dont_have_an_account),
                            fontSize = MaterialTheme.typography.titleSmall.fontSize
                        )
                        Text(
                            modifier = Modifier
                                .clickable{
                                    isSignUp = !isSignUp
                                },
                            text = stringResource(if (isSignUp) R.string.sign_in else R.string.sign_up),
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
                        onValueChange = { signInCustomEmail(LoginEvent.OnEmailChange(it)){} },
                        label = { Text(stringResource(R.string.email)) },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        value = loginState.password,
                        onValueChange = {
                            signInCustomEmail(LoginEvent.OnPasswordChange(it)){ }
                        },
                        label = { Text(stringResource(R.string.password)) },
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            IconButton(onClick = {passwordVisible = !passwordVisible}){
                                Icon(imageVector  = image, "Visibility icon")
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        modifier = Modifier
                            .height(50.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            if(isSignUp){
                                signUpCustomEmail(LoginEvent.OnLogin){ errorCode ->
                                    Toast.makeText(
                                        context,
                                        context.getString(errorCode),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }else{
                                signInCustomEmail(LoginEvent.OnLogin){ errorCode ->
                                    Toast.makeText(
                                        context,
                                        context.getString(errorCode),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }


                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = stringResource(if (isSignUp) R.string.sign_up else R.string.sign_in), fontSize = MaterialTheme.typography.titleMedium.fontSize)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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

                        Text(
                            text = stringResource(R.string.continue_with_google),
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        )

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    IconButton(
                        onClick = { signInWithGoogle(context) },
                        modifier = Modifier
                            .border( 1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .size(50.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.google_icon),
                            contentDescription = "Google Icon"
                        )
                    }
                }
            }
        }
    }

}